package tests;

import network.ClientConnection;

public class TestSocketsMain {

    public static void main(String[] args) {
        System.out.println("SOCKET COMMUNICATION TEST");

        ClientConnection clientConnection = new ClientConnection();

        try {
            boolean connected = clientConnection.connect("localhost", 9000);

            if (!connected) {
                System.out.println("Failed to connect to server.");
                return;
            }

            System.out.println("Connected to server!");

            clientConnection.sendCommand("DISCONNECT");
            System.out.println("Sent: DISCONNECT");

            String response = clientConnection.receiveResponse();
            System.out.println("Received: " + response);

            clientConnection.disconnect();

            System.out.println("Socket communication test completed.");

        } catch (Exception e) {
            System.out.println("Socket test failed: " + e.getMessage());
        }
    }
}
