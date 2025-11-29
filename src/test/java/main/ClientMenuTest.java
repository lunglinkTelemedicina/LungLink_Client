package main;

import Bitalino.BitalinoDemo;
import network.ClientConnection;
import pojos.Client;
import services.ClientService;
import utils.UIUtils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import java.io.IOException;
import static org.mockito.Mockito.*;


class ClientMenuTest {

    private ClientMenu clientMenu;
    private Client mockClient;
    private ClientConnection mockConnection;
    private ClientService mockService;

    private static final int EXIT_OPTION = 6;
    private static final int CLIENT_ID = 42;

    @BeforeEach
    void setUp() {
        mockClient = mock(Client.class);
        mockConnection = mock(ClientConnection.class);
        mockService = mock(ClientService.class);

        when(mockClient.getClientId()).thenReturn(CLIENT_ID);

        clientMenu = new ClientMenu(mockClient, mockConnection);

        }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(mockService);
        verifyNoMoreInteractions(mockConnection);
    }


    @Test
    void displayMenu_ExitOption_DisconnectsAndExits() throws IOException{
        try (MockedStatic<UIUtils> mockedUIUtils = mockStatic(UIUtils.class)) {

            mockedUIUtils.when(() -> UIUtils.readInt(anyString())).thenReturn(EXIT_OPTION);

            clientMenu.displayMenu();

            verify(mockConnection).sendCommand("DISCONNECT");
            verify(mockConnection).disconnect();
        }
    }


    @Test
    void displayMenu_RegisterSymptoms_CallsService() throws IOException {
        try (MockedStatic<UIUtils> mockedUIUtils = mockStatic(UIUtils.class)) {

            mockedUIUtils.when(() -> UIUtils.readInt(anyString()))
                    .thenReturn(1)
                    .thenReturn(EXIT_OPTION);

            clientMenu.displayMenu();


            verify(mockService).registerSymptoms(eq(mockClient), eq(mockConnection));

            verify(mockConnection).disconnect();
        }
    }

    @Test
    void displayMenu_SendECGSignal_CallsBitalinoDemo() throws IOException{
        try (MockedConstruction<BitalinoDemo> mockedConstruction = mockConstruction(BitalinoDemo.class);
             MockedStatic<UIUtils> mockedUIUtils = mockStatic(UIUtils.class)) {

            mockedUIUtils.when(() -> UIUtils.readInt(anyString()))
                    .thenReturn(2)
                    .thenReturn(EXIT_OPTION);

            clientMenu.displayMenu();

            BitalinoDemo constructedDemo = mockedConstruction.constructed().get(0);
            verify(constructedDemo).acquireECGfromBITalino(eq(mockConnection), eq(CLIENT_ID));

            verify(mockConnection).disconnect();
        }
    }


    @Test
    void displayMenu_InvalidThenExit_Recovers() throws IOException {
        try (MockedStatic<UIUtils> mockedUIUtils = mockStatic(UIUtils.class)) {

            mockedUIUtils.when(() -> UIUtils.readInt(anyString()))
                    .thenReturn(99)
                    .thenReturn(EXIT_OPTION);

            clientMenu.displayMenu();


            mockedUIUtils.verify(() -> UIUtils.readInt(anyString()), times(2));

            verify(mockService, never()).viewHistory(any(), any());

            verify(mockConnection).disconnect();
        }
    }
}