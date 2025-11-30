package main;

import network.ClientConnection;
import pojos.*;
import services.ClientService;
import utils.UIUtils;
import java.io.IOException;

/**
 * Main client application class for the LungLink system.
 * Handles user authentication, client profile management, and connection to the server.
 */
public class MainClient {
    /**
     * Entry point of the client application.
     * Manages server connection, user authentication, and client profile operations.
     *
     * @param args Command line arguments (not used)
     * @throws IOException If there is an error in network communication
     */
    public static void main(String[] args) throws IOException {

        ClientConnection connection = new ClientConnection();
        ClientService service = new ClientService();

        boolean connected = false;

        while (!connected) {
            String serverIp = UIUtils.readString("Enter Server IP (for example 192.168.1.44 or localhost): " .trim());

            //this does not allow connection to server if there is no previous answer
            if (serverIp.isEmpty()) {
                System.out.println("ERROR: You must enter a valid IP. It cannot be empty.");
                continue;
            }

            System.out.println("Attempting connection to " + serverIp + "...");

            connected = connection.connect(serverIp, 9000);

            if (!connected) {
                System.out.println("ERROR: Could not connect to server at " + serverIp + ". Please re-enter the IP.");
            }
        }

        System.out.println("\nConnected to server.\n");

        User user = null;
        Client client;

        while (user == null) {
            System.out.println("\nWELCOME TO LUNGLINK");
            System.out.println("1. Register user");
            System.out.println("2. Login user");
            System.out.println("3. Exit");

            int option = UIUtils.readInt("Choose an option: ");
            try {
                switch (option) {
                    case 1:
                        user = service.registerUser(connection);
                        break;
                    case 2:
                        user = service.loginUser(connection);
                        break;
                    case 3:
                        connection.disconnect();
                        return;
                    default:
                        System.out.println("Invalid option.");
                }
            }catch(Exception e) {
                System.err.println("Operation failed"+e.getMessage());
            }
        }

        // checking if the client exists
        System.out.println("\nChecking if you already have a patient profile...");

        connection.sendCommand("CHECK_CLIENT|" + user.getId());
        String response = connection.receiveResponse();

        if (response == null) {
            System.out.println("Server did not respond.");
            connection.disconnect();
            return;
        }
        try {

            if (response.contains("NO_CLIENT")) {

                System.out.println("No patient profile found. Let's create one.");
                client = service.createClientForUser(user, connection);

                if (client == null) {
                    System.out.println("Error creating client profile.");
                    connection.disconnect();
                    return;
                }

            } else if (response.startsWith("OK|")) {

                // Expected: OK|userId|clientId|name|surname|dob|sex|mail
                String[] parts = response.split("\\|");

                int clientId = Integer.parseInt(parts[2]);
                String name = parts[3];
                String surname = parts[4];
                String[] date = parts[5].split("-");
                int day = Integer.parseInt(date[0]);
                int month = Integer.parseInt(date[1]);
                int year = Integer.parseInt(date[2]);
                String sex = parts[6];
                String mail = parts[7];

                client = new Client();
                client.setClientId(clientId);
                client.setName(name);
                client.setSurname(surname);
                client.setDob(java.time.LocalDate.of(year, month, day));
                client.setSex(pojos.Sex.valueOf(sex));
                client.setMail(mail);
                client.setUserId(user.getId());

                System.out.println("Loaded patient profile for: " + name + " " + surname);

            } else {
                System.out.println("Unexpected server response: " + response);
                connection.disconnect();
                return;
            }
        }catch(Exception e) {
            System.err.println("Error creating client profile." + e.getMessage());
            connection.disconnect();
            return;

        }


        ClientMenu menu = new ClientMenu(client, connection);
        menu.displayMenu();
    }
}
