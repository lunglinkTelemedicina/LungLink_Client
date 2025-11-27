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

    /**
     * Prueba el flujo completo de inicio de la aplicación cliente:
     * 1. Conexión exitosa.
     * 2. El usuario elige 'Login'.
     * 3. El servidor responde 'NO_CLIENT' (No tiene perfil de paciente).
     * 4. Se llama a 'createClientForUser' para crear un nuevo perfil.
     * 5. Se llama a 'ClientMenu.displayMenu()'.
     */
    @Test
    void main_SuccessfulFlow_StartsMenu() throws Exception {

        // 1. Simular Mocks de Construcción y Métodos Estáticos
        // Interceptamos la creación de ClientConnection, ClientService y ClientMenu
        try (MockedConstruction<ClientConnection> mockConnectionCtor = mockConstruction(ClientConnection.class);
             MockedConstruction<ClientService> mockServiceCtor = mockConstruction(ClientService.class);
             MockedConstruction<ClientMenu> mockMenuCtor = mockConstruction(ClientMenu.class);
             MockedStatic<UIUtils> mockedUIUtils = mockStatic(UIUtils.class)) {

            // --- Configuración de Mocks ---

            // a) Interceptar ClientConnection (para simular connect y receiveResponse)
            ClientConnection mockConnection = mockConnectionCtor.constructed().get(0);
            when(mockConnection.connect(anyString(), anyInt())).thenReturn(true);

            // Configurar la entrada de usuario: elige 2 (Login) en el menú de inicio.
            mockedUIUtils.when(() -> UIUtils.readInt(anyString())).thenReturn(2);

            // b) Interceptar ClientService (para simular login y creación)
            ClientService mockService = mockServiceCtor.constructed().get(0);
            User mockUser = new User(USER_ID, USERNAME, PASSWORD);
            when(mockService.loginUser(mockConnection)).thenReturn(mockUser);

            // Simular la creación exitosa del cliente por el servicio
            Client mockClient = new Client();
            mockClient.setClientId(101);
            mockClient.setUserId(USER_ID);
            when(mockService.createClientForUser(eq(mockUser), eq(mockConnection))).thenReturn(mockClient);

            // Simular la respuesta del servidor para CHECK_CLIENT: NO_CLIENT encontrado
            when(mockConnection.receiveResponse())
                    .thenReturn("OK|" + USER_ID + "|NO_CLIENT");

            // --- Ejecución del Método Main ---

            // Ejecutamos el método main.
            try {
                MainClient.main(new String[]{});
            } catch (Exception ignored) {
                // Ignoramos excepciones de salida forzada.
            }

            // --- Verificaciones (El Flujo Correcto) ---

            // 1. Verificación de conexión/login
            verify(mockConnection).connect(anyString(), anyInt());
            verify(mockService).loginUser(mockConnection);

            // 2. Verificación del chequeo y creación de perfil
            verify(mockConnection).sendCommand("CHECK_CLIENT|" + USER_ID); // Verifica que chequeó el perfil
            verify(mockService).createClientForUser(eq(mockUser), eq(mockConnection)); // Verifica que se llamó a crear perfil

            // 3. Verificación del menú final
            ClientMenu mockMenu = mockMenuCtor.constructed().get(0);
            verify(mockMenu).displayMenu(); // Verifica que el menú se inició
        }
    }
}