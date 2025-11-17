package main;

import Network.ClientConnection;
import Network.CommandType;
import pojos.Client;
import services.ClientService;
import utils.UIUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class ClientMenu {

    private final Client client;
    private final ClientConnection conn;
    private final ClientService service;
    private final BufferedReader reader;

    public ClientMenu(Client client, ClientConnection conn) {
        this.client = client;
        this.conn = conn;
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
                    // Envía al servidor:
                    // SEND_SYMPTOMS|clientId|symptom1,symptom2,
                    service.registerSymptoms(client, conn);
                    break;

                case 2:
                    // TODO: enviar señal ECG
                    service.sendECG(client, conn);
                    break;

                case 3:
                    // TODO: enviar señal EMG
                    service.sendEMG(client, conn);
                    break;

                case 4:
                    // Envía al servidor:
                    // GET_HISTORY|clientId
                    service.viewHistory(client, conn);
                    break;

                case 5://TODO queremos que el paciente vea las señales?
                    // Envía al servidor:
                    // GET_SIGNALS|ECG/EMG/ALL|clientId
//                    service.viewSignals(client, conn);
                    break;

                case 6:
                    // Envía al servidor:
                    // ADD_EXTRA_INFO|clientId|height|weight
                    service.addExtraInformation(client, conn);
                    break;

                case 7:
                    conn.sendCommand(CommandType.DISCONNECT.name());
                    conn.disconnect();
                    exit = true;
                    break;

                default:
                    System.out.println("Invalid option, please try again.");
                    break;
            }
        }

        System.out.println("Exiting patient menu...");
    }

    private void showMenu() {
        System.out.println("\n=== PATIENT MENU ===");
        System.out.println("1. Register symptoms");
        System.out.println("2. Send ECG signal");
        System.out.println("3. Send EMG signal");
        System.out.println("4. View medical history");
        System.out.println("5. View signals");
        System.out.println("6. Add extra information");
        System.out.println("7. Disconnect");
    }
}
