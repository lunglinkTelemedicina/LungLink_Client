package main;

import network.ClientConnection;
import pojos.Client;
import utils.UIUtils;
import java.io.IOException;

public class MainClient {
    public static void main(String[] args) throws IOException {

        ClientConnection connection = new ClientConnection();

        if (!connection.connect("localhost", 9000)) {
            System.out.println("Could not connect to server.");
            return;
        }

        System.out.println("Connected to server.");
        Client client = null;

        boolean logged = false;

        while (!logged) {
            System.out.println("\n1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");

            int option = UIUtils.readInt("Choose an option:");

            switch (option) {
                case 1 -> client = connection.register();
                case 2 -> client = connection.login();
                case 3 -> {
                    connection.disconnect();
                    return;
                }
                default -> System.out.println("Invalid option.");
            }

            if (client != null) {
                logged = true;
            }
        }

        // === START CLIENT MENU ===
        ClientMenu menu = new ClientMenu(client, connection);
        menu.displayMenu();
    }
}
