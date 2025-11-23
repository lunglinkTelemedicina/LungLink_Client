package main;

import network.ClientConnection;
import network.CommandType;
import pojos.Client;
import services.ClientService;
import utils.UIUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ClientMenu {

    private final Client client;
    private final ClientConnection connection;
    private final ClientService service;
    private final BufferedReader reader;

    public ClientMenu(Client client, ClientConnection connection) {
            this.client = client;
            this.connection = connection;
            this.service = new ClientService();
            this.reader = new BufferedReader(new InputStreamReader(System.in));
    }


        public void displayMenu() {
            boolean exit = false;

            while (!exit) {
                showMenu();
                int option = UIUtils.readInt("Choose an option: ");

                switch (option) {
                    case 1:
                        service.registerSymptoms(client, connection);
                        break;

                    case 2:
                        // TODO: enviar señal ECG
                      //  service.sendSignal(signal, connection);
                        break;

                    case 3:
                        // TODO: enviar señal EMG
                     //   service.sendSignal(signal, connection);
                        break;

                    case 4:
                        service.viewHistory(client, connection);
                        break;

                    case 5:
                        service.addExtraInformation(client, connection);
                        break;

                    case 6:
                        connection.sendCommand("DISCONECT");
                        connection.receiveResponse();
                        connection.disconnect();
                        System.out.println("Disconnected from server. \n");
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid option, please try again.");
                        break;
                }
            }
        }



    private void showMenu() {
        System.out.println("\n PATIENT MENU");
        System.out.println("1. Register symptoms");
        System.out.println("2. Send ECG signal");
        System.out.println("3. Send EMG signal");
        System.out.println("4. View medical history");
        System.out.println("5. Add extra information");
        System.out.println("6. Disconnect");
    }
}


