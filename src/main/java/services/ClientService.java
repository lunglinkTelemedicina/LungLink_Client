package services;

import network.ClientConnection;
import network.CommandType;
import pojos.*;
import utils.UIUtils;

import java.io.IOException;
import java.util.*;
import java.time.LocalDate;

public class ClientService {

    /**
     * Registers symptoms for a client by prompting for input and sending to server.
     *
     * @param client           The client for whom symptoms are being registered
     * @param clientConnection The connection to the server
     * @throws IOException If there is an error in communication with the server
     */
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
            if (reply == null) {//communication error
            throw new IOException("No response from server after sending symptoms.");
            }

            if (reply.startsWith("ERROR|")) { //application error
            throw new IOException("Server application error: " + reply.split("\\|")[1]);
            }

            System.out.println("SERVER: " + reply);
    }

    /**
     * Adds extra information (height and weight) for a client.
     *
     * @param client           The client for whom extra information is being added
     * @param clientConnection The connection to the server
     * @throws IOException If there is an error in communication with the server
     */
    public void addExtraInformation(Client client, ClientConnection clientConnection) throws IOException {
        System.out.println("ADD EXTRA INFORMATION");

        double height = UIUtils.readDouble("Enter your height (in cm): ");
        double weight = UIUtils.readDouble("Enter your weight (in kg): ");

        String message = CommandType.ADD_EXTRA_INFO.name() + "|" + client.getClientId() + "|" + height + "|" + weight;

        clientConnection.sendCommand(message);
        String reply = clientConnection.receiveResponse();

        if (reply == null) {
            throw new IOException("No response from server after sending extra info");
        }
        if(reply.startsWith("ERROR|")) {
            System.out.println("The server could not save the extra information, please try again: " + reply.split("\\|")[1]);
        }

        System.out.println("SERVER: " + reply); //client must receive form server: "OK|Extra info saved"
    }

    /**
     * Retrieves and displays the medical history for a client.
     *
     * @param client The client whose history is being requested
     * @param clientConnection The connection to the server
     * @throws IOException If there is an error in communication with the server
     */
    public void viewHistory(Client client, ClientConnection clientConnection) throws IOException {
        System.out.println("VIEW MEDICAL HISTORY");

            // Send the command to the server
            String command = CommandType.GET_HISTORY.name() + "|" + client.getClientId();
            clientConnection.sendCommand(command);

            // Receive server response
            String response = clientConnection.receiveResponse();

            if (response == null) {
                throw new IOException("No response from server after getting history.");
            }

            // Check if server returned an error message
            if (response.startsWith("ERROR")) {
                //throw new IOException("Server application error retrieving history"+response.split("\\|")[1]);
                System.out.println("\nYou do not have any medical history yet.");
                return;
            }

            // Results
            System.out.println("\nMedical History");
            System.out.println(response);

    }

    /**
     * Authenticates a user with the system.
     *
     * @param conn The connection to the server
     * @return The authenticated User object, or null if authentication fails
     * @throws IOException If there is an error in communication with the server
     */
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

        if (response.startsWith("ERROR|")) {
            System.out.println("\nInvalid credentials. Please try again.");
            return null;
        }
        throw new IOException("Unexpected response from server: " + response);
    }

    /**
     * Registers a new user in the system.
     *
     * @param conn The connection to the server
     * @return The newly created User object, or null if registration fails
     * @throws IOException If there is an error in communication with the server
     */
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
                throw new IOException("\nInvalid server response format during register user: " + response + ". " + e.getMessage(), e);
            }
        }
        if (response.startsWith("ERROR|")) {
            System.out.println("\nRegistration failed: " + response.split("\\|")[1]);
            return null;
        }

        throw new IOException("Unexpected server response: " + response);

    }

    /**
     * Creates a new client profile associated with a user.
     *
     * @param user The user for whom to create the client profile
     * @param conn The connection to the server
     * @return The newly created Client object
     * @throws IOException If there is an error in communication with the server
     */
    public Client createClientForUser(User user, ClientConnection conn) throws IOException {
        System.out.println("\nCREATE CLIENT PROFILE");

        String name = UIUtils.readString("Name: ");
        String surname = UIUtils.readString("Surname: ");

        String dob;
        int day,month, year;
                while (true) {
                    day = UIUtils.readInt("Birth day: ");
                    if (day < 1 || day > 31) {
                        System.out.println("\nERROR: Day must be between 1 and 31.");
                    } else {
                        break;
                    }
                }
                while (true) {
                    month = UIUtils.readInt("Birth month: ");
                    if (month < 1 || month > 12) {
                        System.out.println("\nERROR: Month must be between 1 and 12.");
                    } else {
                        break;
                    }
                }
                while (true) {
                    year = UIUtils.readInt("Birth year: ");
                    int currentYear = java.time.LocalDate.now().getYear();
                    if (year > currentYear) {
                        System.out.println("\nERROR: Year must be previous to the current one.");
                    } else {
                        break;
                    }
                }
                while (true) {
                    try{
                        java.time.LocalDate.of(year, month, day);
                        dob = day + "-" + month + "-" + year;
                        break;
                    }catch (java.time.DateTimeException e) {
                        System.out.println("ERROR: Invalid date entered" + e.getLocalizedMessage());
                }
            }
        Sex selectedSex = null;
        int s;

        while (true) {
            System.out.println("Gender:");
            System.out.println("1. MALE");
            System.out.println("2. FEMALE");
            s = UIUtils.readInt("Choose (1-2): ");

            if (s == 1){
                selectedSex = Sex.MALE;
                break;
            } else if (s == 2) {
                selectedSex = Sex.FEMALE;
                break;
            }
            System.out.println("\nERROR: Invalid option. Enter 1 for MALE or 2 for FEMALE.");
        }

        String sex = selectedSex.name();

        String email = null;
        String response = null;
        boolean emailValid = false;

        while (!emailValid) {
            email = UIUtils.readString("Email: ");


            if (!email.contains("@") || !(email.endsWith(".com") || email.endsWith(".es"))) {
                System.out.println("\nERROR: Invalid email. Please enter a valid address.");

                continue;
            }

            String cmd = "CREATE_CLIENT|" + user.getId() + "|" +
                    name + "|" + surname + "|" + dob + "|" + sex + "|" + email;

            conn.sendCommand(cmd);
            response = conn.receiveResponse();

            if (response == null) {
                throw new IOException("Server not responding during creation");
            }
            if (response.equals("ERROR|Email already in use")) {
                System.out.println("\nERROR: The email is already in use. Please enter a different email.");
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
                c.setMail(email);
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

