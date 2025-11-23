package services;

//TODO probar que funciona bien

//En esta clase están las funciones que un cliente (paciente)
// puede hacer, como registrar síntomas, ver resultados, etc.

import network.ClientConnection;
import network.CommandType;
import pojos.Client;
import pojos.Signal;
import pojos.TypeSignal;
import utils.UIUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientService {

    /**
     * Este metodo permite registrar síntomas para un cliente concreto.
     * Cada vez que se ejecuta, se crea un nuevo objeto MedicalHistory
     * que guarda los síntomas escritos por el usuario junto con la fecha actual.
     */

    public void registerSymptoms(Client client, ClientConnection clientConnection) {

        System.out.println("REGISTER SYMPTOMS");
        System.out.println("Please enter your symptoms one by one.");
        System.out.println("Press Enter on an empty line to finish: \n");

        List<String> newSymptoms = new ArrayList<>();

        while (true) {
            String symptom = UIUtils.readString("Symptom: ");

            if (symptom.isEmpty()){
                    break;
            }
            newSymptoms.add(symptom);
        }

        // Si el usuario no ha introducido ningún síntoma, salimos del metodo
        if (newSymptoms.isEmpty()) {
            System.out.println("No symptoms were added.");
            return;
        }

        try {
            //Coge una lista de Strings y las junta en una única cadena, separadas por comas
            String symptomsString = String.join(",", newSymptoms);
            String message = CommandType.SEND_SYMPTOMS.name()+ "|" + client.getClientId() + "|" + symptomsString;
            clientConnection.sendCommand(message);

            String reply = clientConnection.receiveResponse(); //servers response "OK|Symptoms saved"
            System.out.println("SERVER: " + reply);

        }catch (Exception ex) {
            Logger.getLogger(ClientService.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error while sending symptoms.");
        }
    }

    public void addExtraInformation(Client client, ClientConnection clientConnection) {
        //BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))
        // try {
        System.out.println("ADD EXTRA INFORMATION");

        double height = UIUtils.readDouble("Enter your height (in cm): ");
        double weight = UIUtils.readDouble("Enter your weight (in kg): ");

        String message = CommandType.ADD_EXTRA_INFO.name() + "|" + client.getClientId() + "|" + height + "|" + weight;

        clientConnection.sendCommand(message);
        String reply = clientConnection.receiveResponse();

        if (reply == null) {
            System.out.println("No response from server.");
            return;
        }

        System.out.println("SERVER: " + reply); //client must receive form server: "OK|Extra info saved"
    }

    public void sendSignal(Signal signal, ClientConnection conn) {

        List<Integer> values = signal.getValues();
        if (values == null || values.isEmpty()) {
            System.out.println("No values in signal.");
            return;
        }

        try {
            int samplesNumber = values.size();
            String command = (signal.getType() == TypeSignal.ECG)
                    ? CommandType.SEND_ECG.name()
                    : CommandType.SEND_EMG.name();

            // Cabecera: comando | clientId | numberOfSamples
            String header = command + "|" + signal.getClientId() + "|" + values.size();
            conn.sendCommand(header);

            // Leer confirmación del servidor (si la envía)
            String reply = conn.receiveResponse();
            if (reply != null) {
                System.out.println("SERVER: " + reply);
                return;
            }

            // Datos binarios: enviar bytes de la señal
            byte[] data = signal.toByteArray();
            conn.sendBytes(data);

            // Recibir confirmación
            String finalReply = conn.receiveResponse();
            System.out.println("SERVER: " + finalReply);

            System.out.println("Signal " + signal.getType() +
                    " sent (" + values.size() + " samples).");


        } catch (Exception e) {
            System.out.println("Error sending signal: " + e.getMessage());
        }
    }


    public void viewHistory(Client client, ClientConnection clientConnection) {
        System.out.println("=== VIEW MEDICAL HISTORY ===");

        try {
            // 1. Send the command to the server
            String command = CommandType.GET_HISTORY.name() + "|" + client.getClientId();
            clientConnection.sendCommand(command);

            // 2. Receive server response
            String response = clientConnection.receiveResponse();

            if (response == null) {
                System.out.println("No response received from the server.");
                return;
            }

            // 3. Check if server returned an error message
            if (response.startsWith("ERROR")) {
                System.out.println("Server error: " + response);
                return;
            }
//            // HISTORY|contenido
//            if (response.startsWith("HISTORY|")) {
//                String historyBody = response.substring("HISTORY|".length());
//
//                System.out.println("\n--- Medical History ---");
//                System.out.println(historyBody);
//                System.out.println("------------------------");
//            }
                // 4. Display results
            System.out.println("\nMedical History");
            System.out.println(response);


        } catch (Exception ex) {
            Logger.getLogger(ClientService.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("An error occurred while retrieving history.");
        }
    }
}

    


