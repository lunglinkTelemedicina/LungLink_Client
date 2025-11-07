package services;

//En esta clase están las funciones que un cliente (paciente)
// puede hacer, como registrar síntomas, ver resultados, etc.

import pojos.Client;
import pojos.MedicalHistory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
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
    public void registerSymptoms(Client client) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("=== REGISTER SYMPTOMS ===");
        System.out.println("Please enter your symptoms one by one. Press Enter on an empty line to finish:");

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

            // Creamos un nuevo registro médico (historial) con la información del cliente
            MedicalHistory newHistory = new MedicalHistory(client.getClientId(), client.getDoctorId());

            // Guardamos la lista de síntomas y la fecha actual
            newHistory.setSymptomsList(newSymptoms);
            newHistory.setDate(LocalDate.now());

            // Añadimos este registro médico completo al historial del cliente
            client.getMedicalHistory().add(newHistory);

            System.out.println("\nSymptoms registered successfully on " + newHistory.getDate());
            System.out.println("Recorded symptoms: " + newSymptoms);

        } catch (IOException ex) {
            Logger.getLogger(ClientService.class.getName()).log(Level.SEVERE, null, ex);

        }
    }
}
