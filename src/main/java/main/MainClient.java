package main;

import network.ClientConnection;
import pojos.*;
import services.ClientService;
import utils.UIUtils;
import java.io.IOException;

public class MainClient {
    public static void main(String[] args) throws IOException {

        ClientConnection connection = new ClientConnection();
        ClientService service = new ClientService();

        if (!connection.connect("10.60.96.46", 9000)) {
            System.out.println("Could not connect to server.");
            return;
        }

        System.out.println("\nConnected to server.\n");

        User user = null;
        Client client = null;

        while (user == null) {
            System.out.println("\nWELCOME TO LUNGLINK");
            System.out.println("1. Register user");
            System.out.println("2. Login user");
            System.out.println("3. Exit");

            int option = UIUtils.readInt("Choose an option: ");

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

        if (response.contains("NO_CLIENT")) {

            System.out.println("No patient profile found. Let's create one.");
            System.out.println("DEBUG >>> user_id enviado al servidor = " + user.getId());
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


        ClientMenu menu = new ClientMenu(client, connection);
        menu.displayMenu();
    }
}
