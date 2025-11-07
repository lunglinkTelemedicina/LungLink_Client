package main;

import pojos.Client;
import pojos.MedicalHistory;
import pojos.Sex;
import services.ClientService;

import java.time.LocalDate;

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
                "preyero@mail.com"
        );


        ClientService service = new ClientService();
        service.registerSymptoms(paula);

        //Muestra historial médico
        System.out.println("\n--- CLIENT MEDICAL HISTORY ---");
        for (MedicalHistory history : paula.getMedicalHistory()) {
            System.out.println("Date: " + history.getDate());
            System.out.println("Symptoms: " + history.getSymptomsList());
            System.out.println("-------------------------------");
        }

    }
}
