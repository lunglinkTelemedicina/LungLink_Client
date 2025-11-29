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
        clientConnection = new ClientConnection();
    }


    private String startTestServerAndReceiveString(String messageToSend) throws Exception {
        String[] receivedString = {null};

        Thread serverThread = new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(TEST_PORT);
                 Socket socket = serverSocket.accept(); // Espera la conexión del cliente
                 DataInputStream dataIn = new DataInputStream(socket.getInputStream());
                 DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream())) {

                receivedString[0] = dataIn.readUTF(); // Lee la cadena enviada por sendCommand

                dataOut.writeUTF("TEST_ACK");

            } catch (IOException ignored) {
            }
        });
        serverThread.start();

        Thread.sleep(200);

        boolean connected = clientConnection.connect(TEST_IP, TEST_PORT);
        assertTrue(connected, "La conexión al servidor temporal falló.");

        clientConnection.sendCommand(messageToSend);
        clientConnection.receiveResponse();

        clientConnection.disconnect();
        serverThread.join(2000);

        return receivedString[0];
    }


    private byte[] startTestServerAndReceiveBytes(byte[] bytesToSend) throws Exception {
        byte[][] receivedBytes = {null};

        Thread serverThread = new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(TEST_PORT);
                 Socket socket = serverSocket.accept();
                 DataInputStream dataIn = new DataInputStream(socket.getInputStream());
                 DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream())) {

                int length = dataIn.readInt();
                byte[] buffer = new byte[length];

                dataIn.readFully(buffer);
                receivedBytes[0] = buffer;

                dataOut.writeUTF("TEST_ACK_BYTES");

            } catch (IOException ignored) {
            }
        });
        serverThread.start();

        Thread.sleep(200);

        boolean connected = clientConnection.connect(TEST_IP, TEST_PORT);
        assertTrue(connected, "La conexión al servidor temporal falló.");

        clientConnection.sendBytes(bytesToSend);
        clientConnection.receiveResponse(); // Consumir el ACK

        clientConnection.disconnect();
        serverThread.join(2000);

        return receivedBytes[0];
    }

    @Test
    @Order(1)
    void sendCommand_SendsCorrectUTFString() throws Exception {
        String testMessage = "DISCONNECT_TEST_CMD";

        String receivedMessage = startTestServerAndReceiveString(testMessage);

        assertEquals(testMessage, receivedMessage, "El servidor debe recibir exactamente el comando enviado.");
    }


    @Test
    @Order(2)
    void sendBytes_SendsCorrectBinaryProtocol() throws Exception {
        byte[] originalData = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06};

        byte[] receivedData = startTestServerAndReceiveBytes(originalData);

        assertEquals(originalData.length, receivedData.length,
                "El tamaño de los datos recibidos debe coincidir con la longitud original.");

        assertTrue(Arrays.equals(originalData, receivedData),
                "El servidor debe recibir el array de bytes idéntico.");
    }


    @Test
    @Order(3)
    void sendSignalFromBITalino_DataPreparationCheck() {
        Signal testSignal = new Signal(TypeSignal.EMG, 101);
        testSignal.addSample(512); // Muestra 1
        testSignal.addSample(1023); // Muestra 2

        assertEquals(4, testSignal.toByteArray().length,
                "La señal debe codificarse en 4 bytes (2 muestras de 10-bit en 2 bytes/muestra).");

    }


    @Test
    @Order(4)
    void connect_FailsWhenServerIsDown() {
        boolean connected = clientConnection.connect(TEST_IP, TEST_PORT);

        assertFalse(connected, "La conexión debe fallar (false) si el servidor no está activo.");
    }
}