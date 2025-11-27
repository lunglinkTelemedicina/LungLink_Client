package utils; // Coincide con la ruta test/java/utils/

import network.ClientConnection;
import pojos.TypeSignal; // Necesario para el método sendSignalFromCSV
import java.io.IOException;

/**
 * STUB/FAKE manual para simular el comportamiento de ClientConnection
 * en pruebas unitarias, evitando la dependencia de sockets y la red.
 */
public class FakeClientConnection extends ClientConnection {

    // --- Campos de control para las Pruebas ---

    // 1. Almacena el último comando de texto enviado (para verificar el protocolo)
    private String lastSentCommand;

    // 2. Almacena la respuesta que el Stub debe devolver en la próxima llamada a receiveResponse()
    private String nextResponse;

    // 3. Almacena los últimos bytes enviados (para verificar la señal binaria si fuera necesario)
    private byte[] lastSentBytes;

    // --- Métodos de Control (Getters y Setters) ---

    public String getLastSentCommand() {
        return lastSentCommand;
    }

    public void setNextResponse(String response) {
        this.nextResponse = response;
    }

    public byte[] getLastSentBytes() {
        return lastSentBytes;
    }

    // --- Sobrescritura de Métodos de Red ---

    @Override
    public boolean connect(String ip, int port) {
        // Simula una conexión exitosa para que los tests puedan inicializarse.
        return true;
    }

    @Override
    public void sendCommand(String msg) {
        // Captura y almacena el comando para que el test pueda verificar qué se intentó enviar.
        this.lastSentCommand = msg;
    }

    @Override
    public String receiveResponse() {
        // Devuelve la respuesta configurada por el test.
        // Esto permite simular respuestas de "OK|..." o "ERROR|...".
        return this.nextResponse;
    }

    @Override
    public void sendBytes(byte[] data) {
        // Captura los datos binarios enviados si el test de sendSignal lo requiere.
        this.lastSentBytes = data;
    }


    @Override
    public void disconnect() {
        // Simula la desconexión sin cerrar recursos de red reales.
        // La prueba principal (ClientMenuTest) puede verificar que este método fue llamado.
    }

    // No es necesario sobrescribir releaseResources a menos que se use directamente en un test.
}