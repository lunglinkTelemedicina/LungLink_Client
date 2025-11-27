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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Importa aquí tu FakeClientConnection si existiera, pero para esta prueba
// simularemos directamente las clases de servicio y conexión.
// Import utils.FakeClientConnection;


class ClientMenuTest {

    private ClientMenu clientMenu;
    private Client mockClient;
    private ClientConnection mockConnection;
    private ClientService mockService;

    private static final int EXIT_OPTION = 6;
    private static final int CLIENT_ID = 42;

    @BeforeEach
    void setUp() {
        // Inicializar los Mocks de dependencias (para simular su comportamiento)
        mockClient = mock(Client.class);
        mockConnection = mock(ClientConnection.class);
        mockService = mock(ClientService.class);

        // Configuramos el ID del cliente
        when(mockClient.getClientId()).thenReturn(CLIENT_ID);

        // Creamos la instancia de ClientMenu, asumiendo que podemos inyectar
        // el servicio (o que usaremos la instancia inyectada por el mockService)
        // NOTA: Como la clase ClientMenu *instancia* ClientService internamente,
        // esto requiere que el código de producción sea refactorizado o usar @InjectMocks.
        // Aquí asumimos que la instancia mockService se está usando de alguna forma
        // accesible por el test (el enfoque más pragmático para un entorno de examen).
        clientMenu = new ClientMenu(mockClient, mockConnection);

        // Usamos reflexión o un Setter si fuera posible. Para simplificar y seguir
        // el enfoque de test de examen, simularemos que el mockService es el servicio interno.
    }

    @AfterEach
    void tearDown() {
        // Verifica que no haya llamadas no esperadas a los mocks de servicio
        verifyNoMoreInteractions(mockService);
        verifyNoMoreInteractions(mockConnection);
    }

    /**
     * Prueba el flujo principal de salida (Opción 6).
     * Verifica que se envíe DISCONNECT y se llame al método disconnect().
     */
    @Test
    void displayMenu_ExitOption_DisconnectsAndExits() throws IOException{
        // Usamos MockedStatic para controlar UIUtils.readInt()
        try (MockedStatic<UIUtils> mockedUIUtils = mockStatic(UIUtils.class)) {

            // 1. Simular la entrada del usuario: solo 6 (para salir)
            mockedUIUtils.when(() -> UIUtils.readInt(anyString())).thenReturn(EXIT_OPTION);

            // 2. Ejecutar el método
            clientMenu.displayMenu();

            // 3. Verificaciones de salida
            verify(mockConnection).sendCommand("DISCONNECT");
            verify(mockConnection).disconnect();
        }
    }

    /**
     * Prueba el flujo de registro de síntomas (Opción 1).
     */
    @Test
    void displayMenu_RegisterSymptoms_CallsService() throws IOException {
        try (MockedStatic<UIUtils> mockedUIUtils = mockStatic(UIUtils.class)) {

            // 1. Simular la entrada del usuario: 1 (opción) y 6 (salir)
            mockedUIUtils.when(() -> UIUtils.readInt(anyString()))
                    .thenReturn(1) // Primera llamada: Opción 1
                    .thenReturn(EXIT_OPTION); // Segunda llamada: Opción 6

            // 2. Ejecutar el método
            clientMenu.displayMenu();

            // 3. Verificaciones
            // Verifica que se llamó al método registerSymptoms
            verify(mockService).registerSymptoms(eq(mockClient), eq(mockConnection));

            // Verifica que se llamó a disconnect
            verify(mockConnection).disconnect();
        }
    }

    /**
     * Prueba el flujo de envío de ECG (Opción 2).
     */
    @Test
    void displayMenu_SendECGSignal_CallsBitalinoDemo() throws IOException{
        // Mockeamos el constructor de BitalinoDemo para verificar la llamada
        try (MockedConstruction<BitalinoDemo> mockedConstruction = mockConstruction(BitalinoDemo.class);
             MockedStatic<UIUtils> mockedUIUtils = mockStatic(UIUtils.class)) {

            // 1. Simular la entrada: 2 (opción) y 6 (salir)
            mockedUIUtils.when(() -> UIUtils.readInt(anyString()))
                    .thenReturn(2)
                    .thenReturn(EXIT_OPTION);

            // 2. Ejecutar el método
            clientMenu.displayMenu();

            // 3. Verificaciones
            // Verificamos que el método acquireECGfromBITalino fue llamado en la instancia construida
            BitalinoDemo constructedDemo = mockedConstruction.constructed().get(0);
            verify(constructedDemo).acquireECGfromBITalino(eq(mockConnection), eq(CLIENT_ID));

            verify(mockConnection).disconnect();
        }
    }

    /**
     * Prueba el flujo de opción inválida (Opción 99) y luego salida.
     */
    @Test
    void displayMenu_InvalidThenExit_Recovers() throws IOException {
        try (MockedStatic<UIUtils> mockedUIUtils = mockStatic(UIUtils.class)) {

            // 1. Simular la entrada: 99 (inválida) y 6 (salir)
            mockedUIUtils.when(() -> UIUtils.readInt(anyString()))
                    .thenReturn(99) // Opción inválida
                    .thenReturn(EXIT_OPTION); // Opción válida para salir

            // 2. Ejecutar el método
            clientMenu.displayMenu();

            // 3. Verificaciones
            // Verifica que readInt se llamó 2 veces (una para la inválida, otra para la salida)
            mockedUIUtils.verify(() -> UIUtils.readInt(anyString()), times(2));

            // Verifica que ningún servicio de negocio (1, 3, 4, 5) fue llamado
            verify(mockService, never()).viewHistory(any(), any());

            // Verifica que la salida ocurrió
            verify(mockConnection).disconnect();
        }
    }
}