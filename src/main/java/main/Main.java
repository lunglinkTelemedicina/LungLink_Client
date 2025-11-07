package main;

import pojos.Client;
import pojos.Doctor;
import pojos.MedicalHistory;
import pojos.Sex;
import services.ClientService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Clase principal para probar las funciones de ClientService.
 */
public class Main {
    public static void main(String[] args) {

        // 1. Creamos un cliente de ejemplo con algunos datos
        Client paula = new Client(
                "Paula",
                "Reyero",
                Sex.FEMALE,
                LocalDate.of(2004, 9, 14),
                "preyero@gmail.com"
        );


        // Crear dos doctores
        Doctor doctor1 = new Doctor("Laura", "Cardiology");
        Doctor doctor2 = new Doctor("Miguel", "Pulmonology");
        Doctor doctor3 = new Doctor("Juan", "Radiology");

        // Mostrar que los IDs se generan automáticamente
        System.out.println("Doctor 1 ID: " + doctor1.getDoctorId());
        System.out.println("Doctor 2 ID: " + doctor2.getDoctorId());
        System.out.println("Doctor 3 ID: " + doctor3.getDoctorId());

        // Crear un cliente
        Client client = new Client();
        client.setClientId(1);
        client.setName("Paula");
        client.setSurname("Sánchez");
        client.setSex(Sex.FEMALE);
        client.setDob(LocalDate.of(2000, 5, 12));
        client.setMail("paula@example.com");
        client.setDoctorId(doctor1.getDoctorId()); // asignamos el doctor que la atiende
        client.setMedicalHistory(new ArrayList<>()); // inicializamos la lista

        // Crear un historial médico manualmente
        MedicalHistory history1 = new MedicalHistory(client.getClientId(), client.getDoctorId());
        history1.setDate(LocalDate.of(2025, 11, 7));
        history1.setSymptomsList(Arrays.asList("Cough", "Fever", "Fatigue"));
        history1.setObservations("Possible viral infection");

        // 📋 Segundo historial médico (otro día, otro doctor)
        MedicalHistory history2 = new MedicalHistory(client.getClientId(), doctor2.getDoctorId());
        history2.setDate(LocalDate.of(2025, 11, 10));
        history2.setSymptomsList(Arrays.asList("Chest pain", "Shortness of breath"));
        history2.setObservations("Recommended ECG and rest");

        // Añadimos ambos historiales al cliente
        client.getMedicalHistory().add(history1);
        client.getMedicalHistory().add(history2);

        // Crear el servicio y mostrar resultados
        ClientService clientService = new ClientService();
        clientService.viewResults(client);
    }
}
