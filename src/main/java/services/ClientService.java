package services;

import network.ClientConnection;
import network.CommandType;
import pojos.*;
import utils.UIUtils;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.LocalDate;

public class ClientService {

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

        //If the user did not enter any symptoms, we exit the method
        if (newSymptoms.isEmpty()) {
            System.out.println("No symptoms were added.");
            return;
        }

        try {
            // It takes a list of strings and joins them into one single string, separated by commas
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

            String header = command + "|" + signal.getClientId() + "|" + values.size();
            conn.sendCommand(header);

            //Read confirmation from the server (if it sends one)
            String reply = conn.receiveResponse();
            if (reply != null) {
                System.out.println("SERVER: " + reply);
                return;
            }

            //Send the signals as bytes
            byte[] data = signal.toByteArray();
            conn.sendBytes(data);

            //Receive confirmation
            String finalReply = conn.receiveResponse();
            System.out.println("SERVER: " + finalReply);

            System.out.println("Signal " + signal.getType() +
                    " sent (" + values.size() + " samples).");


        } catch (Exception e) {
            System.out.println("Error sending signal: " + e.getMessage());
        }
    }


    public void viewHistory(Client client, ClientConnection clientConnection) {
        System.out.println("VIEW MEDICAL HISTORY");

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

            // 4. Display results
            System.out.println("\nMedical History");
            System.out.println(response);


        } catch (Exception ex) {
            Logger.getLogger(ClientService.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("An error occurred while retrieving history.");
        }
    }

    public User loginUser(ClientConnection conn) {
        System.out.println("\nLOGIN USER");

        String username = UIUtils.readString("Username: ");
        String password = UIUtils.readString("Password: ");

        String cmd = "LOGIN_USER|" + username + "|" + password;
        conn.sendCommand(cmd);

        String response = conn.receiveResponse();
        if (response == null) {
            System.out.println("No response from server.");
            return null;
        }

        if (response.startsWith("OK|")) {
            String[] parts = response.split("\\|");
            int id = Integer.parseInt(parts[1]);
            String uname = parts[2];
            return new User(id, uname, password);
        }

        System.out.println("User login failed: " + response);
        return null;
    }

    public User registerUser(ClientConnection conn) {
        System.out.println("\nREGISTER USER");

        String username = UIUtils.readString("New username: ");
        String password = UIUtils.readString("New password: ");

        String cmd = "REGISTER_USER|" + username + "|" + password;
        conn.sendCommand(cmd);

        String response = conn.receiveResponse();
        if (response == null) {
            System.out.println("No server response");
            return null;
        }

        if (response.startsWith("OK|")) {
            int id = Integer.parseInt(response.split("\\|")[1]);
            System.out.println("User created. ID = " + id);
            return new User(id, username, password);
        }

        System.out.println("Registration failed: " + response);
        return null;
    }

    public Client createClientForUser(User user, ClientConnection conn) {
        System.out.println("\nCREATE CLIENT PROFILE");

        String name = UIUtils.readString("Name: ");
        String surname = UIUtils.readString("Surname: ");

        int day = UIUtils.readInt("Birth day: ");
        int month = UIUtils.readInt("Birth month: ");
        int year = UIUtils.readInt("Birth year: ");

        String dob = day + "-" + month + "-" + year;

        System.out.println("Sex:");
        System.out.println("1. MALE");
        System.out.println("2. FEMALE");
        int s = UIUtils.readInt("Choose: ");

        String sex = (s == 1) ? "MALE" : "FEMALE";

        String mail = UIUtils.readString("Email: ");

        String cmd = "CREATE_CLIENT|" + user.getId() + "|" +
                name + "|" + surname + "|" + dob + "|" + sex + "|" + mail;

        conn.sendCommand(cmd);

        String response = conn.receiveResponse();
        if (response == null) {
            System.out.println("Server not responding.");
            return null;
        }

        if (response.startsWith("OK|")) {
            int clientId = Integer.parseInt(response.split("\\|")[1]);

            Client c = new Client();
            c.setClientId(clientId);
            c.setName(name);
            c.setSurname(surname);
            c.setMail(mail);
            c.setSex(Sex.valueOf(sex));
            c.setDob(LocalDate.of(year, month, day));
            c.setUserId(user.getId());

            System.out.println("Client profile created. ID = " + clientId);

            return c;
        }

        System.out.println("Error creating profile: " + response);
        return null;
    }

}

    


