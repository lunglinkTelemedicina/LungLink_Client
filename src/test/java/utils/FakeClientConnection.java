package utils; // Coincide con la ruta test/java/utils/

import network.ClientConnection;
import pojos.TypeSignal; // Necesario para el método sendSignalFromCSV
import java.io.IOException;


public class FakeClientConnection extends ClientConnection {

    private String lastSentCommand;

    private String nextResponse;

    private byte[] lastSentBytes;


    public String getLastSentCommand() {
        return lastSentCommand;
    }

    public void setNextResponse(String response) {
        this.nextResponse = response;
    }

    public byte[] getLastSentBytes() {
        return lastSentBytes;
    }


    @Override
    public boolean connect(String ip, int port) {
        return true;
    }

    @Override
    public void sendCommand(String msg) {
        this.lastSentCommand = msg;
    }

    @Override
    public String receiveResponse() {
        return this.nextResponse;
    }

    @Override
    public void sendBytes(byte[] data) {
        this.lastSentBytes = data;
    }


    @Override
    public void disconnect() {
    }

}