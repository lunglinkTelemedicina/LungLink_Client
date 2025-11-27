package network;

import org.junit.jupiter.api.*;
import pojos.Signal;
import pojos.TypeSignal;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ClientConnectionTest {

    private static final String TEST_IP = "127.0.0.1";
    private static final int TEST_PORT = 9000;
    private ClientConnection clientConnection;

    @BeforeEach
    void setUp() {
        // Instancia de la clase real que usa Sockets
        clientConnection = new ClientConnection();
    }

    @AfterEach
    void tearDown() {
        // Asegura que la conexión siempre se cierre después de cada prueba
        clientConnection.disconnect();
    }

    // --- AYUDANTE DE INTEGRACIÓN: Servidor de Prueba Temporal ---

    /**
     * Inicia un servidor temporal, espera una conexión del cliente y lee una cadena UTF.
     * @return La cadena recibida del cliente.
     */
    private String startTestServerAndReceiveString() throws Exception {
        String[] receivedString = {null};

        // El servidor se ejecuta en un hilo para no bloquear la prueba principal
        Thread serverThread = new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(TEST_PORT);
                 Socket socket = serverSocket.accept(); // Espera la conexión del cliente
                 DataInputStream dataIn = new DataInputStream(socket.getInputStream())) {

                receivedString[0] = dataIn.readUTF(); // Lee la cadena enviada por sendCommand

                // Enviar una respuesta simple de vuelta para evitar que el cliente se bloquee
                new DataOutputStream(socket.getOutputStream()).writeUTF("TEST_ACK");

            } catch (IOException ignored) {
            }
        });
        serverThread.start();

        // Esperar un pequeño retraso para que el socket esté listo
        Thread.sleep(100);

        // Conectar el cliente
        clientConnection.connect(TEST_IP, TEST_PORT);

        // Esperar a que el servidor termine de procesar (tiempo máximo)
        serverThread.join(2000);

        return receivedString[0];
    }

    /**
     * Inicia un servidor temporal, espera la conexión y lee el protocolo binario [int: length][byte[]: data].
     * @return El array de bytes de datos recibido.
     */
    private byte[] startTestServerAndReceiveBytes() throws Exception {
        byte[][] receivedBytes = {null};

        Thread serverThread = new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(TEST_PORT);
                 Socket socket = serverSocket.accept();
                 DataInputStream dataIn = new DataInputStream(socket.getInputStream())) {

                // 1. Leer la longitud (el INT que envía sendBytes)
                int length = dataIn.readInt();
                byte[] buffer = new byte[length];

                // 2. Leer los datos completos
                dataIn.readFully(buffer);
                receivedBytes[0] = buffer;

                // Enviar respuesta simple (para que el cliente no se bloquee)
                new DataOutputStream(socket.getOutputStream()).writeUTF("TEST_ACK_BYTES");

            } catch (IOException ignored) {
            }
        });
        serverThread.start();

        Thread.sleep(100);
        clientConnection.connect(TEST_IP, TEST_PORT);

        serverThread.join(2000);

        return receivedBytes[0];
    }

    // --- PRUEBAS DE INTEGRACIÓN ---

    /**
     * Prueba si sendCommand envía la cadena correcta a través del socket.
     */
    @Test
    void sendCommand_SendsCorrectUTFString() throws Exception {
        String testMessage = "DISCONNECT";

        // El servidor se inicia, espera, recibe y se detiene.
        String receivedMessage = startTestServerAndReceiveString();

        clientConnection.sendCommand(testMessage);

        assertEquals(testMessage, receivedMessage, "El servidor debe recibir exactamente el comando enviado.");
    }

    /**
     * Prueba si sendBytes codifica y envía el protocolo [length][data] correcto.
     */
    @Test
    void sendBytes_SendsCorrectBinaryProtocol() throws Exception {
        byte[] originalData = {0x01, 0x02, 0x03, 0x04};

        // 1. Iniciar el servidor que lee el protocolo binario
        byte[] receivedData = startTestServerAndReceiveBytes();

        // 2. Enviar los bytes desde la conexión del cliente
        clientConnection.sendBytes(originalData);

        // 3. Verificar que los bytes recibidos son idénticos a los originales
        assertTrue(Arrays.equals(originalData, receivedData),
                "El servidor debe recibir el array de bytes idéntico y del tamaño correcto.");
    }

    /**
     * Prueba el método de conexión para verificar que establece la conexión TCP.
     * Requiere que el servidor de prueba real (MainServer.java) esté corriendo.
     */
    @Test
    void connect_Success_ManualVerification() {
        // ADVERTENCIA: Esta prueba requiere que MainServer.java esté ejecutándose.
        boolean connected = clientConnection.connect(TEST_IP, TEST_PORT);

        assertTrue(connected, "La conexión debe ser exitosa si el servidor real está activo.");
    }

    @Test
    void disconnect_NoException() {
        // Prueba básica para asegurar que el cierre de recursos no lanza excepciones no manejadas.
        assertDoesNotThrow(() -> clientConnection.disconnect());
    }

    @Test
    void sendSignalFromBITalino_LogicCheck() {
        // En este nivel, solo podemos verificar que la lógica de codificación interna es correcta
        // y asumir que sendCommand/sendBytes funcionan (probado arriba).
        Signal testSignal = new Signal(TypeSignal.ECG, 101);
        testSignal.addSample(512);
        testSignal.addSample(510);

        // Una muestra es 2 bytes. 2 muestras = 4 bytes.
        assertEquals(4, testSignal.toByteArray().length, "El array de bytes debe ser del tamaño correcto (2 bytes/muestra).");

        // La prueba de protocolo completo debe hacerse a nivel de ClientService o con el servidor real.
    }
}