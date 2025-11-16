package main;

import Network.ClientConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainClient {
    public static void main(String[] args) throws IOException {
        ClientConnection client = new ClientConnection();

        if (!client.connect("localhost", 9000)) {
            System.out.println("Could not connect to server.");
            return;
        }

        System.out.println("Connected. Type a message to the server.");
        System.out.println("Type 'x' or 'DISCONNECT' to exit.");

        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        String line;

        while ((line = console.readLine()) != null) {

            client.sendCommand(line);

            if (line.equalsIgnoreCase("x") || line.equalsIgnoreCase("DISCONNECT")) {
                client.disconnect();
                break;
            }
        }
    }
}
