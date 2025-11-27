package services;

import network.ClientConnection;
import network.CommandType;
import pojos.*;
import utils.UIUtils;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.LocalDate;

public class ClientService {

    public void registerSymptoms(Client client, ClientConnection clientConnection) throws IOException {

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

            // It takes a list of strings and joins them into one single string, separated by commas
            String symptomsString = String.join(",", newSymptoms);
            String message = CommandType.SEND_SYMPTOMS.name()+ "|" + client.getClientId() + "|" + symptomsString;
            clientConnection.sendCommand(message);

            String reply = clientConnection.receiveResponse(); //servers response "OK|Symptoms saved"
            if (reply == null) {//esto lanza un error de comunicacion o de red
            throw new IOException("No response from server after sending symptoms.");
            }

            if (reply.startsWith("ERROR|")) {//error en la aplicacion
            throw new IOException("Server application error: " + reply.split("\\|")[1]);
            }

            System.out.println("SERVER: " + reply);
    }




    public void addExtraInformation(Client client, ClientConnection clientConnection) throws IOException {
        System.out.println("ADD EXTRA INFORMATION");

        double height = UIUtils.readDouble("Enter your height (in cm): ");
        double weight = UIUtils.readDouble("Enter your weight (in kg): ");

        String message = CommandType.ADD_EXTRA_INFO.name() + "|" + client.getClientId() + "|" + height + "|" + weight;

        clientConnection.sendCommand(message);
        String reply = clientConnection.receiveResponse();

        if (reply == null) {
            throw new IOException("No response from server after sending extra info"+reply.split("\\|")[1]);
        }
        if(reply.startsWith("ERROR|")) {
            throw new IOException("Server application error while saving extra info: "+reply.split("\\|")[1]);
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
            if (reply != null && reply.startsWith("OK|Client can send data")) {
                System.out.println("SERVER: " + reply);
            } else if (reply != null) {
                // unexpected but we DO NOT return, we continue
                System.out.println("SERVER (unexpected): " + reply);
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
            Logger.getLogger(ClientService.class.getName()).log(Level.SEVERE, null, e);
            System.out.println("Error sending signal: " + e.getMessage());
        }
    }


    public void viewHistory(Client client, ClientConnection clientConnection) throws IOException {
        System.out.println("VIEW MEDICAL HISTORY");

            // 1. Send the command to the server
            String command = CommandType.GET_HISTORY.name() + "|" + client.getClientId();
            clientConnection.sendCommand(command);

            // 2. Receive server response
            String response = clientConnection.receiveResponse();

            if (response == null) {
                throw new IOException("No response from server after getting history.");
            }

            // 3. Check if server returned an error message
            if (response.startsWith("ERROR")) {
                throw new IOException("Server application error retrieving history"+response.split("\\|")[1]);
            }

            // 4. Display results
            System.out.println("\nMedical History");
            System.out.println(response);

    }

    public User loginUser(ClientConnection conn) throws IOException {
        System.out.println("\nLOGIN USER");

        String username = UIUtils.readString("Username: ");
        String password = UIUtils.readString("Password: ");

        String cmd = "LOGIN_USER|" + username + "|" + password;
        conn.sendCommand(cmd);

        String response = conn.receiveResponse();
        if (response == null) {
            throw new IOException("No response from server after login user.");
        }

        if (response.startsWith("OK|")) {
            try {
                String[] parts = response.split("\\|");
                int id = Integer.parseInt(parts[1]);
                String uname = parts[2];
                return new User(id, uname, password);
            }catch(NumberFormatException | ArrayIndexOutOfBoundsException e) {
                throw new IOException("Invalid server response format during login: " + response + ". " + e.getMessage(), e);
            }
        }
        throw new IOException("User login failed: " + response);
    }

    public User registerUser(ClientConnection conn) throws IOException {
        System.out.println("\nREGISTER USER");

        String username = UIUtils.readString("New username: ");
        String password = UIUtils.readString("New password: ");

        String cmd = "REGISTER_USER|" + username + "|" + password;
        conn.sendCommand(cmd);

        String response = conn.receiveResponse();
        if (response == null) {
           throw new IOException("No server response");
        }

        if (response.startsWith("OK|")) {
            try {
                int id = Integer.parseInt(response.split("\\|")[1]);
                System.out.println("User created. ID = " + id);
                return new User(id, username, password);
            }catch(NumberFormatException | ArrayIndexOutOfBoundsException e) {
                throw new IOException("Invalid server response format during register user: " + response + ". " + e.getMessage(), e);
            }
        }
        throw new IOException("Registration failed: " + response);

    }

    public Client createClientForUser(User user, ClientConnection conn) throws IOException {
        System.out.println("\nCREATE CLIENT PROFILE");

        String name = UIUtils.readString("Name: ");
        String surname = UIUtils.readString("Surname: ");

        String dob;
        int day;
        int month;
        int year;
        while(true) {
            try {

                day = UIUtils.readInt("Birth day: ");
                month = UIUtils.readInt("Birth month: ");
                year = UIUtils.readInt("Birth year: ");
                java.time.LocalDate.of(year, month, day);
                dob = day + "-" + month + "-" + year;
                break;

            } catch (java.time.DateTimeException e) {
                System.err.println("ERROR: Invalid date entered" + e.getLocalizedMessage() + " Please re-enter the date");
            }
        }

        System.out.println("Sex:");
        System.out.println("1. MALE");
        System.out.println("2. FEMALE");
        int s = UIUtils.readInt("Choose: ");

        String sex = (s == 1) ? "MALE" : "FEMALE";

        String mail = null;
        String response = null;
        boolean emailValid = false;

        // Bucle para validar unicidad y formato del email
        while (!emailValid) {
            mail = UIUtils.readString("Email: ");


            if (!mail.contains("@")) {
                System.out.println("ERROR: Invalid email format. Must contain '@'.");

                continue;
            }

            String cmd = "CREATE_CLIENT|" + user.getId() + "|" +
                    name + "|" + surname + "|" + dob + "|" + sex + "|" + mail;

            conn.sendCommand(cmd);
            response = conn.receiveResponse();

            if (response == null) {
                throw new IOException("Server not responding during creation");
            }
            if (response.equals("ERROR|Email already in use")) {
                System.out.println("ERROR: The email is already in use. Please enter a different email.");
            } else {
                emailValid = true;
            }
        }

        if (response.startsWith("OK|")) {
            try {
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
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                throw new IOException("Invalid server response format during creation: " + response + ". " + e.getMessage(), e);
            }
        }
        throw new IOException("Error creating profile:" + response);
    }

}

    


