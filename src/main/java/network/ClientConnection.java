package network;

import utils.UIUtils;
import pojos.*;
import file.SignalFileManager;
import java.io.*;
import java.net.Socket;
import java.time.LocalDate;

/**
 * Connects to the server on the specified IP and port.
 * Returns true if the connection was successful.
 */

public class ClientConnection {
    private Socket socket;
    private DataOutputStream dataOut;
    private DataInputStream dataIn;

    public boolean connect(String ip, int port){
        try{
            socket = new Socket(ip, port);
            dataOut = new DataOutputStream(socket.getOutputStream());
            dataIn = new DataInputStream(socket.getInputStream());
            return true;

        }catch(IOException e){
            System.out.println("Connection failed." + e.getMessage());
            return false;
        }
    }

    public void sendCommand(String msg){
        try{
            dataOut.writeUTF(msg);
            dataOut.flush();
        }catch(IOException e){
            System.out.println("Send failed." + e.getMessage());
        }
    }

    public String receiveResponse() {
        try {
            String reply = dataIn.readUTF();

            if (reply.equals("SERVER_SHUTDOWN")) {
                System.out.println("Server is shutting down. Closing client...");
                releaseResources();
                System.exit(0);
            }

            return reply;

        } catch (IOException e) {
            System.out.println("Lost connection to server.");
            releaseResources();
            System.exit(0);
            return null;
        }
    }


    //Send binary samples (BITalino)
    public void sendBytes(byte[] data){
        try{
            dataOut.writeInt(data.length);
            dataOut.write(data);
            dataOut.flush();
        }catch(IOException e){
            System.out.println("Error sending bytes." + e.getMessage());
        }
    }

    public void sendSignalFromCSV(String filePath, int clientId, TypeSignal type){
        try{
            Signal signal = SignalFileManager.getSignalFromCSV(filePath, type, clientId);

            // el servidor espera SEND_ECG o SEND_EMG
            sendCommand("SEND_" + type.name() + "|" + clientId + "|" + signal.getValues().size());

            String response = receiveResponse();
            if (response == null || !response.contains("Client can send the data")) {
                System.out.println("Server did not authorize sending data.");
                return;
            }

            sendBytes(signal.toByteArray());
            System.out.println("Signal sent successfully from CSV.");

        } catch (IOException e) {
            System.out.println("Error sending data from CSV: " + e.getMessage());
        }
    }
    public void sendSignalFromBITalino(Signal signal) {
        try {
            sendCommand("SEND_" + signal.getType().name() + "|" + signal.getClientId() + "|" + signal.getValues().size());

            String response = receiveResponse();
            if (response == null || !response.contains("Client can send the data")) {
                System.out.println("Server did not authorize sending data.");
                return;
            }

            sendBytes(signal.toByteArray());
            System.out.println("Signal sent successfully (BITalino).");

        } catch (Exception e) {
            System.out.println("Error sending BITalino signal: " + e.getMessage());
        }
    }




    public void disconnect() {
        sendCommand("DISCONNECT");
        releaseResources();
        System.out.println("Disconnected from server.");
    }

    private void releaseResources() {
        try {
            if (dataIn != null) {
                dataIn.close();
            }
        } catch (IOException e) {
            System.out.println("Error closing input stream: " + e.getMessage());
        }
        try {
            if (dataOut != null) {
                dataOut.close();
            }
        } catch (Exception e) {
            System.out.println("Error closing output stream: " + e.getMessage());
        }
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.out.println("Error closing socket: " + e.getMessage());
        }
    }

//    public User loginUser() {
//        try {
//            System.out.println("\nLOGIN USER");
//
//            String username = UIUtils.readString("Username: ");
//            String password = UIUtils.readString("Password: ");
//
//            String cmd = "LOGIN_USER|" + username + "|" + password;
//            sendCommand(cmd);
//
//            String response = receiveResponse();
//            if (response == null) {
//                System.out.println("No response from server.");
//                return null;
//            }
//
//            if (response.startsWith("OK|")) {
//                String[] parts = response.split("\\|");
//                int id = Integer.parseInt(parts[1]);
//                String uname = parts[2];
//
//                User user = new User(id, uname, password);
//
//                System.out.println("User login successful.");
//
//                return user;
//            }
//
//            System.out.println("User login failed: " + response);
//            return null;
//
//        } catch (Exception e) {
//            System.out.println("Error during user login: " + e.getMessage());
//            return null;
//        }
//    }
//
//    public User registerUser() {
//        try {
//            System.out.println("\nREGISTER USER ACCOUNT");
//
//            String username = UIUtils.readString("New username: ");
//            String password = UIUtils.readString("New password: ");
//
//            String cmd = "REGISTER_USER|" + username + "|" + password;
//            sendCommand(cmd);
//
//            String response = receiveResponse();
//
//            if (response == null) {
//                System.out.println("No response from server.");
//                return null;
//            }
//
//            if (response.startsWith("OK|")) {
//                int id = Integer.parseInt(response.split("\\|")[1]);
//
//                User user = new User(id, username, password);
//
//                System.out.println("User account created. Your ID = " + id);
//                return user;
//            }
//
//            System.out.println("User registration failed: " + response);
//            return null;
//
//        } catch (Exception e) {
//            System.out.println("Error during registerUser: " + e.getMessage());
//            return null;
//        }
//    }
//
//    public Client createClientForUser(User user) {
//        try {
//            System.out.println("\nCREATE CLIENT PROFILE");
//
//            String name = UIUtils.readString("Name: ");
//            String surname = UIUtils.readString("Surname: ");
//
//            int day = UIUtils.readInt("Birth day (1-31): ");
//            int month = UIUtils.readInt("Birth month (1-12): ");
//            int year = UIUtils.readInt("Birth year (YYYY): ");
//            String dob = day + "-" + month + "-" + year;
//
//            System.out.println("Sex:");
//            System.out.println("1. MALE");
//            System.out.println("2. FEMALE");
//            int s = UIUtils.readInt("Choose: ");
//
//            String sex = switch (s) {
//                case 1 -> "MALE";
//                case 2 -> "FEMALE";
//                default -> throw new IllegalStateException("Unexpected value: " + s);
//            };
//
//            String mail = UIUtils.readString("Patient email: ");
//
//            String cmd = "CREATE_CLIENT|" + user.getId() + "|" +
//                    name + "|" + surname + "|" +
//                    dob + "|" + sex + "|" + mail;
//
//            sendCommand(cmd);
//
//            String response = receiveResponse();
//
//            if (response == null) {
//                System.out.println("No response from server.");
//                return null;
//            }
//
//            if (response.startsWith("OK|")) {
//                int clientId = Integer.parseInt(response.split("\\|")[1]);
//
//                Client c = new Client();
//                c.setClientId(clientId);
//                c.setName(name);
//                c.setSurname(surname);
//                c.setMail(mail);
//                c.setSex(Sex.valueOf(sex));
//                c.setDob(LocalDate.of(year, month, day));
//                c.setUserId(user.getId());
//
//                System.out.println("Client profile created, ID = " + clientId);
//
//
//                return c;
//            }
//
//
//            System.out.println("Client creation failed: " + response);
//            return null;
//
//        } catch (Exception e) {
//            System.out.println("Error creating client: " + e.getMessage());
//            return null;
//        }
//    }

}
