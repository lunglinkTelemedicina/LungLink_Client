package main;

import Bitalino.BitalinoDemo;
import network.ClientConnection;
import network.CommandType;
import pojos.Client;
import pojos.TypeSignal;
import services.ClientService;
import utils.UIUtils;

import java.io.InputStreamReader;

public class ClientMenu {

    private final Client client;
    private final ClientConnection connection;
    private final ClientService service;

    public ClientMenu(Client client, ClientConnection connection) {
            this.client = client;
            this.connection = connection;
            this.service = new ClientService();
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
//                        String ecgPath = UIUtils.readString("Enter ECG CSV file path: ");
//                        connection.sendSignalFromCSV(ecgPath, client.getClientId(), TypeSignal.ECG);
                        new BitalinoDemo().acquireECGfromBITalino(connection, client.getClientId());
                        break;

                    case 3:
//                        String emgPath = UIUtils.readString("Enter EMG CSV file path: ");
//                        connection.sendSignalFromCSV(emgPath, client.getClientId(), TypeSignal.EMG);
                        new BitalinoDemo().acquireEMGfromBITalino(connection, client.getClientId());
                        break;

                    case 4:
                        service.viewHistory(client, connection);
                        break;

                    case 5:
                        service.addExtraInformation(client, connection);
                        break;

                    case 6:
                        connection.sendCommand("DISCONNECT");
                        connection.disconnect();
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


