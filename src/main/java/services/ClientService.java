package services;

//En esta clase están las funciones que un cliente (paciente)
// puede hacer, como registrar síntomas, ver resultados, etc.

import Network.ClientConnection;
import pojos.Client;
import pojos.MedicalHistory;
import utils.UIUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientService {

    /**
     * Este metodo permite registrar síntomas para un cliente concreto.
     * Cada vez que se ejecuta, se crea un nuevo objeto MedicalHistory
     * que guarda los síntomas escritos por el usuario junto con la fecha actual.
     */

    /**
     * El paciente (CLIENTE) rellena los síntomas.
     * Esos síntomas SE ENVIAN al servidor.
     * El servidor los guarda en la historia clínica (archivo o BD).
     *
     * Lo único que he hecho es añadir:
     * El envío del comando "SEND_SYMPTOMS"
     * El envío de cada síntoma por el socket
     * El envío del “END” para cerrar el bloque
     * La lectura de respuesta del servidor
     * */


    public void registerSymptoms(Client client, ClientConnection conn) {

        System.out.println("=== REGISTER SYMPTOMS ===");
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

            /*// Creamos un nuevo registro médico (historial) con la información del cliente
            MedicalHistory newHistory = new MedicalHistory(client.getClientId(), client.getDoctorId());

            // Guardamos la lista de síntomas y la fecha actual
            newHistory.setSymptomsList(newSymptoms);
            newHistory.setDate(LocalDate.now());

            // Añadimos este registro médico completo al historial del cliente
            client.getMedicalHistory().add(newHistory);

            System.out.println("\nSymptoms registered successfully on " + newHistory.getDate());
            System.out.println("Recorded symptoms: " + newSymptoms);
            */

            try {
                String payload = String.join(",", newSymptoms);
                String message = "SEND_SYMPTOMS|" + client.getClientId() + "|" + payload;
                conn.sendCommand(message);

                String reply = conn.receiveResponse(); //servers response
                System.out.println("SERVER: " + reply);
            }catch (Exception ex) {//TODO:ESTA EXCEPCION NO DEBERIA SALTAR NUNCA LA QUITAMOS??
                Logger.getLogger(ClientService.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error while sending symptoms.");
            }


    }

//En principio este metodo no lo vamos a usar porque el cliente no deberia de tocar medicalHistory
//    public void viewResults(Client client){
//        System.out.println("=== VIEW RESULTS ===");
//        //Comprobar que el cliente tenga una lista de historiales que no sea null
//        if(client.getMedicalHistory()== null || client.getMedicalHistory().isEmpty()){
//            System.out.println("No medical history records were found for this client.");
//            return;
//        }
//
//        //Si sí tiene historial, recorre cada uno y muestra la info
//        for (MedicalHistory history : client.getMedicalHistory()) {
//            System.out.println("Date: " + history.getDate());
//            System.out.println("Doctor ID: " + history.getDoctorId());
//            System.out.println("Patient ID: " + history.getClientId());
//
//            // Lista de síntomas
//            if (history.getSymptomsList() != null && !history.getSymptomsList().isEmpty()) {
//                System.out.println("Symptoms: " + history.getSymptomsList());
//            } else {
//                System.out.println("Symptoms: [none]");
//            }
//
//            //Observations and additional data
//            if(history.getObservations() != null && history.getObservations().isEmpty()){
//                System.out.println("Observations: " +history.getObservations());
//            }
//        }
//        System.out.println("End of medical history.");
//    }

    public void addExtraInformation(Client client, ClientConnection conn) {
        //BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))
        // try {
        System.out.println("=== ADD EXTRA INFORMATION ===");
        double height = UIUtils.readDouble("Enter your height (in cm): ");
        double weight = UIUtils.readDouble("Enter your weight (in kg): ");

        String command = "ADD_EXTRA_INFO|" + client.getClientId() + "|" + height + "|" + weight;

        conn.sendCommand(command);

        String reply = conn.receiveResponse();

        if (reply == null) {
            System.out.println("No response from server.");
            return;
        }

        System.out.println("SERVER: " + reply);

//            System.out.print("Enter your height (in cm): ");
//            double height = Double.parseDouble(reader.readLine());

//            System.out.print("Enter your weight (in kg): ");
//            double weight = Double.parseDouble(reader.readLine());

                // Guardar los datos en el objeto Client
                //client.setHeight(height);
                //client.setWeight(weight);

                //System.out.println("Extra information added successfully!");
//        } catch (IOException e) {
//            System.out.println("Error reading input: " + e.getMessage());
//        } catch (NumberFormatException e) {
//            System.out.println("Invalid number format. Please enter numeric values.");
//        }

    }

    public void sendECG(Client client, ClientConnection conn) {
        // TODO: convertir datos a string, enviar comando tipo SEND_SIGNAL|clientId|ECG|12 13 15 20 18...
    }

    public void sendEMG(Client client, ClientConnection conn) {
        // TODO: convertir datos a string, enviar comando tipo SEND_SIGNAL|clientId|EMG|12 13 15 20 18...
    }

    public void viewHistory(Client client, ClientConnection conn) {
        System.out.println("=== VIEW MEDICAL HISTORY ===");

        try {
            // 1. Send the command to the server
            String command = "GET_HISTORY|" + client.getClientId();
            conn.sendCommand(command);

            // 2. Receive server response
            String response = conn.receiveResponse();

            if (response == null) {
                System.out.println("No response received from the server.");
                return;
            }

            // 3. Check if server returned an error message
            if (response.startsWith("ERROR")) {
                System.out.println("Server error: " + response);
                return;
            }

            // 4. Display results
            System.out.println("\n--- Medical History ---");
            System.out.println(response);
            System.out.println("------------------------");

        } catch (Exception ex) {
            Logger.getLogger(ClientService.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("An error occurred while retrieving history.");
        }
    }

    //El cliente elegirá entre ECG, EMG o las dos
    public void viewSignals(Client client, ClientConnection conn) {
        System.out.println("=== VIEW SIGNALS ===");

        System.out.println("1. View ECG signals");
        System.out.println("2. View EMG signals");
        System.out.println("3. View all signals");

        int option = UIUtils.readInt("Choose an option: ");

        String command = "";

        switch (option) {
            case 1:
                command = "GET_SIGNALS|ECG|" + client.getClientId();
                break;

            case 2:
                command = "GET_SIGNALS|EMG|" + client.getClientId();
                break;

            case 3:
                command = "GET_SIGNALS|ALL|" + client.getClientId();
                break;

            default:
                System.out.println("Invalid option.");
                return;
        }

        try {
            // Send command to server
            conn.sendCommand(command);

            // Receive server response
            String response = conn.receiveResponse();

            if (response == null) {
                System.out.println("No response received from server.");
                return;
            }

            if (response.startsWith("ERROR")) {
                System.out.println("Server error: " + response);
                return;
            }

            // Display raw data (then you can format it better)
            System.out.println("\n--- Signals ---");
            System.out.println(response);
            System.out.println("----------------\n");

        } catch (Exception ex) {
            Logger.getLogger(ClientService.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("An error occurred while receiving signals.");
        }
    }




}