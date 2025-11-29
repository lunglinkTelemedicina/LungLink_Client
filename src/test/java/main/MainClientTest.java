package main;

import network.ClientConnection;
import pojos.Client;
import pojos.User;
import services.ClientService;
import utils.UIUtils;

import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MainClientTest {

    private static final int USER_ID = 42;
    private static final String USERNAME = "testuser";
    private static final String PASSWORD = "password123";
    private static final String CLIENT_NAME = "Jimena";
    private static final String CLIENT_SURNAME = "Gomez";
    private static final String CLIENT_EMAIL = "valid@email.com";
    private static final int CLIENT_ID = 101;


    @Test
    void main_SuccessfulFlow_StartsMenu() throws Exception {

        User mockUser = new User(USER_ID, USERNAME, PASSWORD);
        Client mockClient = new Client();
        mockClient.setClientId(CLIENT_ID);
        mockClient.setUserId(USER_ID);

        try (MockedConstruction<ClientConnection> mockConnectionCtor = mockConstruction(ClientConnection.class, (mock, context) -> {
            when(mock.connect(anyString(), anyInt())).thenReturn(true);
            when(mock.receiveResponse())
                    .thenReturn("OK|" + USER_ID + "|NO_CLIENT") // Respuesta para CHECK_CLIENT
                    .thenReturn("OK|" + CLIENT_ID); // Respuesta para el comando CREATE_CLIENT dentro de createClientForUser
        });

             MockedConstruction<ClientService> mockServiceCtor = mockConstruction(ClientService.class, (mock, context) -> {
                 when(mock.loginUser(any(ClientConnection.class))).thenReturn(mockUser);
                 when(mock.createClientForUser(any(User.class), any(ClientConnection.class))).thenReturn(mockClient);
             });

             MockedConstruction<ClientMenu> mockMenuCtor = mockConstruction(ClientMenu.class);

             MockedStatic<UIUtils> mockedUIUtils = mockStatic(UIUtils.class)) {

            mockedUIUtils.when(() -> UIUtils.readInt(anyString()))
                    .thenReturn(2)      // 1 Menú principal Login
                    .thenReturn(15)     // 2 createClientForUser Birth day
                    .thenReturn(10)     // 3 createClientForUser Birth month
                    .thenReturn(1998)   // 4 createClientForUser Birth year
                    .thenReturn(2);     // 5 createClientForUser Sex (Opción 2: FEMALE)

            mockedUIUtils.when(() -> UIUtils.readString(anyString()))
                    .thenReturn(USERNAME)       // 1 loginUser Username
                    .thenReturn(PASSWORD)       // 2 loginUser Password
                    .thenReturn(CLIENT_NAME)    // 3 createClientForUser Name
                    .thenReturn(CLIENT_SURNAME) // 4 createClientForUser Surname
                    .thenReturn(CLIENT_EMAIL);  // 5 createClientForUser Email

            try {
                MainClient.main(new String[]{});
            } catch (Exception ignored) {
            }


            ClientConnection mockConnection = mockConnectionCtor.constructed().get(0);
            ClientService mockService = mockServiceCtor.constructed().get(0);
            ClientMenu mockMenu = mockMenuCtor.constructed().get(0);

            verify(mockService).loginUser(mockConnection);

            verify(mockConnection).sendCommand("CHECK_CLIENT|" + USER_ID);
            verify(mockService).createClientForUser(any(User.class), eq(mockConnection));

            assertEquals(1, mockMenuCtor.constructed().size(), "Debe haberse construido exactamente una ClientMenu.");
            verify(mockMenu).displayMenu();
        }
    }
}