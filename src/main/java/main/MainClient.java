package main;

import network.ClientConnection;
import pojos.Client;
import pojos.Sex;

import java.io.IOException;

public class MainClient {
    public static void main(String[] args) throws IOException {
        ClientConnection connection = new ClientConnection();

        if (!connection.connect("localhost", 9000)) {
            System.out.println("Could not connect to server.");
            return;
        }

        // TEMPORARY TEST CLIENT (luego viene login/register)
        Client testClient = new Client();
        testClient.setClientId(1);
        testClient.setName("Florinda");
        testClient.setSex(Sex.FEMALE);

        // Start menu
        System.out.println("Connected.");
        ClientMenu menu = new ClientMenu(testClient, connection);
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
    }}

