package network;

import org.junit.jupiter.api.*;
import pojos.Signal;
import pojos.TypeSignal;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

// Aseguramos el orden de ejecución para las pruebas de integración que usan el mismo puerto.
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ClientConnectionTest {

    private static final String TEST_IP = "127.0.0.1";
    private static final int TEST_PORT = 9000;
    private ClientConnection clientConnection;

    @BeforeEach
    void setUp() {
        clientConnection = new ClientConnection();
    }

    // NOTA: Eliminamos el @AfterEach que causaba el NullPointerException
    // e integramos la desconexión dentro de los métodos de prueba exitosos.


    // ====================================================================
    // AYUDANTES DE INTEGRACIÓN CON SINCRONIZACIÓN BÁSICA
    // ====================================================================

    /**
     * Inicia un servidor temporal, realiza la conexión/envío de un String y lee la respuesta.
     */
    private String startTestServerAndReceiveString(String messageToSend) throws Exception {
        String[] receivedString = {null};

        // El servidor se ejecuta en un hilo para no bloquear la prueba principal
        Thread serverThread = new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(TEST_PORT);
                 Socket socket = serverSocket.accept(); // Espera la conexión del cliente
                 DataInputStream dataIn = new DataInputStream(socket.getInputStream());
                 DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream())) {

                receivedString[0] = dataIn.readUTF(); // Lee la cadena enviada por sendCommand

                // Envía una respuesta simple de vuelta (para evitar que el cliente se bloquee)
                dataOut.writeUTF("TEST_ACK");

            } catch (IOException ignored) {
            }
        });
        serverThread.start();

        // Esperar un pequeño retraso para que el socket del servidor esté listo
        Thread.sleep(200);

        // 1. Conectar y verificar
        boolean connected = clientConnection.connect(TEST_IP, TEST_PORT);
        assertTrue(connected, "La conexión al servidor temporal falló.");

        // 2. Enviar el comando
        clientConnection.sendCommand(messageToSend);
        clientConnection.receiveResponse(); // Consumir el ACK

        // 3. Desconectar y esperar al hilo del servidor para obtener el resultado
        clientConnection.disconnect();
        serverThread.join(2000);

        return receivedString[0];
    }

    /**
     * Inicia un servidor temporal, realiza la conexión/envío binario y lee el protocolo [int][byte[]].
     */
    private byte[] startTestServerAndReceiveBytes(byte[] bytesToSend) throws Exception {
        byte[][] receivedBytes = {null};

        Thread serverThread = new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(TEST_PORT);
                 Socket socket = serverSocket.accept();
                 DataInputStream dataIn = new DataInputStream(socket.getInputStream());
                 DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream())) {

                // 1. Leer la longitud (el INT que envía sendBytes)
                int length = dataIn.readInt();
                byte[] buffer = new byte[length];

                // 2. Leer los datos completos
                dataIn.readFully(buffer);
                receivedBytes[0] = buffer;

                // Enviar respuesta simple (para que el cliente no se bloquee)
                dataOut.writeUTF("TEST_ACK_BYTES");

            } catch (IOException ignored) {
            }
        });
        serverThread.start();

        Thread.sleep(200);

        // 1. Conectar y verificar
        boolean connected = clientConnection.connect(TEST_IP, TEST_PORT);
        assertTrue(connected, "La conexión al servidor temporal falló.");

        // 2. Enviar los bytes
        clientConnection.sendBytes(bytesToSend);
        clientConnection.receiveResponse(); // Consumir el ACK

        // 3. Desconectar y esperar al hilo del servidor
        clientConnection.disconnect();
        serverThread.join(2000);

        return receivedBytes[0];
    }

    // ====================================================================
    // PRUEBAS DE INTEGRACIÓN DE CLIENTCONNECTION
    // ====================================================================

    /**
     * Prueba si sendCommand envía la cadena correcta (UTF) y es leída por el servidor.
     */
    @Test
    @Order(1)
    void sendCommand_SendsCorrectUTFString() throws Exception {
        String testMessage = "DISCONNECT_TEST_CMD";

        // Inicia el ciclo de comunicación y devuelve lo que el servidor leyó
        String receivedMessage = startTestServerAndReceiveString(testMessage);

        assertEquals(testMessage, receivedMessage, "El servidor debe recibir exactamente el comando enviado.");
    }

    /**
     * Prueba si sendBytes codifica y envía el protocolo binario correcto ([length][data]).
     */
    @Test
    @Order(2)
    void sendBytes_SendsCorrectBinaryProtocol() throws Exception {
        byte[] originalData = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06};

        // Inicia el ciclo de comunicación y devuelve los bytes leídos
        byte[] receivedData = startTestServerAndReceiveBytes(originalData);

        // Verificación 1: La longitud debe coincidir
        assertEquals(originalData.length, receivedData.length,
                "El tamaño de los datos recibidos debe coincidir con la longitud original.");

        // Verificación 2: El contenido de los bytes debe ser idéntico
        assertTrue(Arrays.equals(originalData, receivedData),
                "El servidor debe recibir el array de bytes idéntico.");
    }

    /**
     * Prueba conceptual del protocolo de envío de señal BITalino.
     * Verifica que la codificación interna del array de bytes es correcta (2 bytes/muestra).
     */
    @Test
    @Order(3)
    void sendSignalFromBITalino_DataPreparationCheck() {
        Signal testSignal = new Signal(TypeSignal.EMG, 101);
        testSignal.addSample(512); // Muestra 1
        testSignal.addSample(1023); // Muestra 2

        // 2 muestras * 2 bytes/muestra = 4 bytes esperados
        assertEquals(4, testSignal.toByteArray().length,
                "La señal debe codificarse en 4 bytes (2 muestras de 10-bit en 2 bytes/muestra).");

        // No se puede probar el flujo completo sin el servidor, pero esta lógica interna se verifica aquí.
    }

    /**
     * Prueba si la conexión es rechazada cuando el servidor no está disponible.
     */
    @Test
    @Order(4)
    void connect_FailsWhenServerIsDown() {
        // Asumiendo que no hay ningún servidor activo en este momento en el puerto 9000
        boolean connected = clientConnection.connect(TEST_IP, TEST_PORT);

        assertFalse(connected, "La conexión debe fallar (false) si el servidor no está activo.");
    }
}