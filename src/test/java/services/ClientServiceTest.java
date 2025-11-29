package services;

import pojos.Client;
import pojos.User;
import pojos.Sex;
import pojos.Signal;
import pojos.TypeSignal;
import utils.UIUtils;
import utils.FakeClientConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientServiceTest {

    private static final int CLIENT_ID = 101;
    private static final int USER_ID = 42;
    private ClientService clientService;
    private FakeClientConnection fakeConnection;
    private Client testClient;
    private User testUser;

    @BeforeEach
    void setUp() {
        clientService = new ClientService();

        fakeConnection = new FakeClientConnection();

        testClient = new Client();
        testClient.setClientId(CLIENT_ID);
        testClient.setUserId(USER_ID);

        testUser = new User(USER_ID, "testUser", "password123");
    }

    @Test
    void registerSymptoms_Success_ValidCommandSent() throws IOException {
        String symptom1 = "cough";
        String symptom2 = "fever";
        String serverReply = "OK|Symptoms saved. AssignedDoctor=Alfredo";
        String expectedCommand = "SEND_SYMPTOMS|" + CLIENT_ID + "|" + symptom1 + "," + symptom2;

        try (MockedStatic<UIUtils> mockedUIUtils = mockStatic(UIUtils.class)) {
            mockedUIUtils.when(() -> UIUtils.readString(anyString()))
                    .thenReturn(symptom1)
                    .thenReturn(symptom2)
                    .thenReturn("");

            fakeConnection.setNextResponse(serverReply);
            clientService.registerSymptoms(testClient, fakeConnection);

            assertEquals(expectedCommand, fakeConnection.getLastSentCommand(),
                    "El comando enviado no coincide con el protocolo esperado.");
        }
    }

    @Test
    void registerSymptoms_ServerReturnsError_ThrowsIOException() {
        String serverError = "ERROR|User not authorized";

        try (MockedStatic<UIUtils> mockedUIUtils = mockStatic(UIUtils.class)) {
            mockedUIUtils.when(() -> UIUtils.readString(anyString()))
                    .thenReturn("pain")
                    .thenReturn("");

            fakeConnection.setNextResponse(serverError);

            IOException exception = assertThrows(IOException.class, () -> {
                clientService.registerSymptoms(testClient, fakeConnection);
            });

            assertTrue(exception.getMessage().contains("Server application error: User not authorized"));
        }
    }

    @Test
    void addExtraInformation_Success_ValidCommandSent() throws IOException {
        double height = 180.5;
        double weight = 75.2;
        String serverReply = "OK|Extra info saved";
        String expectedCommand = String.format(Locale.US, "ADD_EXTRA_INFO|%d|%.1f|%.1f", CLIENT_ID, height, weight);
        try (MockedStatic<UIUtils> mockedUIUtils = mockStatic(UIUtils.class)) {
            mockedUIUtils.when(() -> UIUtils.readDouble(anyString()))
                    .thenReturn(height)
                    .thenReturn(weight);

            fakeConnection.setNextResponse(serverReply);
            clientService.addExtraInformation(testClient, fakeConnection);

            assertEquals(expectedCommand, fakeConnection.getLastSentCommand(),
                    "El comando enviado no coincide con el protocolo esperado.");
        }
    }


    @Test
    void sendSignal_Success_CommandAndBytesSent() {
        Signal signal = new Signal(TypeSignal.EMG, CLIENT_ID);
        signal.addSample(10);
        signal.addSample(20);

        int expectedBytesLength = 4;
        String successReply = "OK|Client can send the data";

        String expectedCommand = "SEND_EMG|" + CLIENT_ID + "|" + signal.getValues().size();

        fakeConnection.setNextResponse(successReply);

        assertDoesNotThrow(() -> clientService.sendSignal(signal, fakeConnection),
                "El envío de señal no debe lanzar excepción en flujo exitoso.");

        assertEquals(expectedCommand, fakeConnection.getLastSentCommand(),
                "La cabecera del comando no es correcta.");
        assertEquals(expectedBytesLength, fakeConnection.getLastSentBytes().length,
                "El número de bytes binarios enviados es incorrecto.");
    }

    @Test
    void sendSignal_NoValues_NoCommandSent() {
        Signal emptySignal = new Signal(TypeSignal.ECG, CLIENT_ID);

        clientService.sendSignal(emptySignal, fakeConnection);

        assertNull(fakeConnection.getLastSentCommand(),
                "No debe enviar comando si la señal está vacía.");
    }

    @Test
    void viewHistory_Success_ValidCommandSent() throws IOException {
        String history = "Medical History\nDATE: 2025-11-20\nSYMPTOMS: headache\n";
        String expectedCommand = "GET_HISTORY|" + CLIENT_ID;

        fakeConnection.setNextResponse(history);

        assertDoesNotThrow(() -> clientService.viewHistory(testClient, fakeConnection),
                "El método no debe fallar al recibir una respuesta exitosa.");

        assertEquals(expectedCommand, fakeConnection.getLastSentCommand(),
                "El comando enviado no coincide con el protocolo GET_HISTORY.");
    }

    @Test
    void viewHistory_NoHistory_HandledGracefully() {
        String serverError = "ERROR|No history found";
        String expectedCommand = "GET_HISTORY|" + CLIENT_ID;

        fakeConnection.setNextResponse(serverError);

        assertDoesNotThrow(() -> {
            clientService.viewHistory(testClient, fakeConnection);
        }, "El método NO debe lanzar una excepción para un error de negocio esperado (No history).");

        assertEquals(expectedCommand, fakeConnection.getLastSentCommand(),
                "El comando enviado no coincide con el protocolo GET_HISTORY.");
    }

    @Test
    void loginUser_Success_ReturnsUser() throws IOException {
        String username = "testUser";
        String password = "password123";
        String serverReply = String.format("OK|%d|%s", USER_ID, username);
        String expectedCommand = "LOGIN_USER|" + username + "|" + password;

        try (MockedStatic<UIUtils> mockedUIUtils = mockStatic(UIUtils.class)) {
            mockedUIUtils.when(() -> UIUtils.readString(anyString()))
                    .thenReturn(username)
                    .thenReturn(password);

            fakeConnection.setNextResponse(serverReply);
            User user = clientService.loginUser(fakeConnection);

            assertEquals(expectedCommand, fakeConnection.getLastSentCommand(), "El comando enviado es incorrecto.");
            assertNotNull(user, "El objeto User no debe ser nulo.");
            assertEquals(USER_ID, user.getId(), "El ID del usuario debe ser el devuelto por el servidor.");
        }
    }

    @Test
    void registerUser_Success_ReturnsUser() throws IOException {
        String username = "newUser";
        String password = "newPassword";
        int newId = 15;

        String serverReply = "OK|" + newId;
        String expectedCommand = "REGISTER_USER|" + username + "|" + password;

        try (MockedStatic<UIUtils> mockedUIUtils = mockStatic(UIUtils.class)) {
            mockedUIUtils.when(() -> UIUtils.readString(anyString()))
                    .thenReturn(username)
                    .thenReturn(password);

            fakeConnection.setNextResponse(serverReply);
            User user = clientService.registerUser(fakeConnection);

            assertEquals(expectedCommand, fakeConnection.getLastSentCommand(), "El comando enviado es incorrecto.");
            assertEquals(newId, user.getId(), "El ID del usuario debe ser el devuelto por el servidor.");
        }
    }

    @Test
    void createClientForUser_Success_ReturnsClient() throws IOException {
        String name = "Jimena";
        String surname = "Gomez";
        String mail = "test@mail.com";
        int newClientId = 200;

        String expectedCommandPrefix = "CREATE_CLIENT|" + USER_ID + "|" + name + "|" + surname + "|15-10-1998|FEMALE|" + mail;
        String serverReply = "OK|" + newClientId;

        try (MockedStatic<UIUtils> mockedUIUtils = mockStatic(UIUtils.class)) {
            mockedUIUtils.when(() -> UIUtils.readString(anyString()))
                    .thenReturn(name) // Nombre
                    .thenReturn(surname); // Apellido

            mockedUIUtils.when(() -> UIUtils.readInt(anyString()))
                    .thenReturn(15).thenReturn(10).thenReturn(1998) // DOB
                    .thenReturn(2); // Sex (2 -> FEMALE)

            mockedUIUtils.when(() -> UIUtils.readString(eq("Email: "))).thenReturn(mail);
            fakeConnection.setNextResponse(serverReply);

            Client client = clientService.createClientForUser(testUser, fakeConnection);


            assertTrue(fakeConnection.getLastSentCommand().startsWith("CREATE_CLIENT"),
                    "El comando de creación no se construyó correctamente.");
            assertEquals(newClientId, client.getClientId(), "El ID del cliente debe ser el devuelto.");
            assertEquals(Sex.FEMALE, client.getSex(), "El Sexo debe ser FEMALE.");
        }
    }
}