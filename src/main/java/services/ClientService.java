package services;

//En esta clase están las funciones que un cliente (paciente)
// puede hacer, como registrar síntomas, ver resultados, etc.

import Network.ClientConnection;
import pojos.Client;
import pojos.MedicalHistory;

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

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("=== REGISTER SYMPTOMS ===");
        System.out.println("Please enter your symptoms one by one. \n Press Enter on an empty line to finish: \n");

        try {
            List<String> newSymptoms = new ArrayList<>();

            while (true) {
                System.out.print("Symptom: ");
                String input = reader.readLine();

                if (input == null || input.trim().isEmpty()) break;

                // Quitamos espacios innecesarios
                input = input.trim();

                // Solo añadimos el síntoma si no está vacío
                if (!input.isEmpty()) {
                    newSymptoms.add(input);
                }
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


            String payload = String.join(",", newSymptoms);
            String message = "SEND_SYMPTOMS|" + client.getClientId() + "|" + payload;
            conn.sendCommand(message);

            String reply = conn.receiveResponse(); //servers response
            System.out.println("SERVER: " + reply);

        } catch (IOException ex) {
            Logger.getLogger(ClientService.class.getName()).log(Level.SEVERE, null, ex);

        }
    }


    public void viewResults(Client client){
        System.out.println("=== VIEW RESULTS ===");
        //Comprobar que el cliente tenga una lista de historiales que no sea null
        if(client.getMedicalHistory()== null || client.getMedicalHistory().isEmpty()){
            System.out.println("No medical history records were found for this client.");
            return;
        }

        //Si sí tiene historial, recorre cada uno y muestra la info
        for (MedicalHistory history : client.getMedicalHistory()) {
            System.out.println("Date: " + history.getDate());
            System.out.println("Doctor ID: " + history.getDoctorId());
            System.out.println("Patient ID: " + history.getClientId());

            // Lista de síntomas
            if (history.getSymptomsList() != null && !history.getSymptomsList().isEmpty()) {
                System.out.println("Symptoms: " + history.getSymptomsList());
            } else {
                System.out.println("Symptoms: [none]");
            }

            //Observations and additional data
            if(history.getObservations() != null && history.getObservations().isEmpty()){
                System.out.println("Observations: " +history.getObservations());
            }
        }
        System.out.println("End of medical history.");
    }

    public void addExtraInformation(Client client) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            System.out.println("=== ADD EXTRA INFORMATION ===");

            System.out.print("Enter your height (in cm): ");
            double height = Double.parseDouble(reader.readLine());

            System.out.print("Enter your weight (in kg): ");
            double weight = Double.parseDouble(reader.readLine());

            // Guardar los datos en el objeto Client
            client.setHeight(height);
            client.setWeight(weight);

            System.out.println("Extra information added successfully!");
        } catch (IOException e) {
            System.out.println("Error reading input: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format. Please enter numeric values.");
        }
    }
}