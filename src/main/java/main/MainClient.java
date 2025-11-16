package main;

import Network.ClientConnection;
import pojos.Client;
import pojos.Sex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainClient {
    public static void main(String[] args) throws IOException {
        ClientConnection conn = new ClientConnection();

        if (!conn.connect("localhost", 9000)) {
            System.out.println("Could not connect to server.");
            return;
        }

        // TEMPORARY TEST CLIENT (luego viene login/register)
        Client testClient = new Client();
        testClient.setClientId(1);
        testClient.setName("Florinda");
        testClient.setSex(Sex.FEMALE);

        // Start menu
        ClientMenu menu = new ClientMenu(testClient, conn);
        menu.displayMenu();

//        System.out.println("Connected. Type a message to the server.");
//        System.out.println("Type 'x' or 'DISCONNECT' to exit.");
//
//        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
//        String line;
//
//        while ((line = console.readLine()) != null) {
//
//            client.sendCommand(line);
//
//            if (line.equalsIgnoreCase("x") || line.equalsIgnoreCase("DISCONNECT")) {
//                client.disconnect();
//                break;
//            }
//        }
    }
}
