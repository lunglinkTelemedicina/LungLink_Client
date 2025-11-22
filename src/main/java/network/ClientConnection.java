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

            System.out.println("Connected to server.");
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

    /* Receive text response*/
    public String receiveResponse(){
        try{
            return dataIn.readUTF();
        }catch(IOException e){
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
            if (response == null || !response.equals("Client can send the data")) {
                System.out.println("Server did not authorize sending data.");
                return;
            }

            sendBytes(signal.toByteArray());
            System.out.println("Signal sent successfully from CSV.");

        } catch (IOException e) {
            System.out.println("Error sending data from CSV: " + e.getMessage());
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

    public Client register() {
        try {
            System.out.println("\nREGISTER ACCOUNT");

            String name = UIUtils.readString("Please enter your name: ");
            String surname = UIUtils.readString("Please enter your surname: ");
            String mail = UIUtils.readString("Please enter your email: ");
            String password = UIUtils.readString("Set your password: ");

            int day = UIUtils.readInt("Birth day (1-31): ");
            int month = UIUtils.readInt("Birth month (1-12): ");
            int year = UIUtils.readInt("Birth year (YYYY): ");
            String dob = day + "-" + month + "-" + year;

            System.out.println("Sex:");
            System.out.println("1. MALE");
            System.out.println("2. FEMALE");
            int s = UIUtils.readInt("Choose: ");

            String sex = switch (s) {
                case 1 -> "MALE";
                case 2 -> "FEMALE";
                default -> "OTHER";
            };

            String cmd = "REGISTER|" +
                    name + "|" +
                    surname + "|" +
                    dob + "|" +
                    sex + "|" +
                    mail + "|" +
                    password;

            sendCommand(cmd);

            String response = receiveResponse();

            if (response == null) {
                System.out.println("No server response.");
                return null;
            }

            if (response.startsWith("OK|")) {
                int id = Integer.parseInt(response.split("\\|")[1]);

                Client c = new Client();
                c.setClientId(id);
                c.setName(name);
                c.setSurname(surname);
                c.setMail(mail);
                c.setSex(Sex.valueOf(sex));
                c.setDob(LocalDate.of(year, month, day));

                System.out.println("Registration successful. Your ID = " + id);
                return c;
            }

            System.out.println("Register failed: " + response);
            return null;

        } catch (Exception e) {
            System.out.println("Error registering: " + e.getMessage());
            return null;
        }
    }

    public Client login() {
        try {
            System.out.println("\n--- LOGIN ---");

            String mail = UIUtils.readString("Email: ");
            String password = UIUtils.readString("Password: ");

            String cmd = "LOGIN|" + mail + "|" + password;
            sendCommand(cmd);

            String response = receiveResponse();

            if (response == null) {
                System.out.println("No answer from server.");
                return null;
            }

            // ESPERADO: OK|clientId|name|surname|dob|sex
            if (response.startsWith("OK|")) {
                String[] parts = response.split("\\|");

                int id = Integer.parseInt(parts[1]);
                String name = parts[2];
                String surname = parts[3];
                String dob = parts[4];     // "dd-mm-yyyy"
                String sex = parts[5];

                // Convert DOB
                String[] split = dob.split("-");
                int day = Integer.parseInt(split[0]);
                int month = Integer.parseInt(split[1]);
                int year = Integer.parseInt(split[2]);

                Client c = new Client();
                c.setClientId(id);
                c.setName(name);
                c.setSurname(surname);
                c.setMail(mail);
                c.setPassword(password);
                c.setDob(LocalDate.of(year, month, day));
                c.setSex(Sex.valueOf(sex));

                System.out.println("Login successful. Welcome " + name + "!");
                return c;
            }

            // Si no empieza con OK → es error del servidor.
            System.out.println("Login failed: " + response);
            return null;

        } catch (Exception e) {
            System.out.println("Error during login: " + e.getMessage());
            return null;
        }
    }




}
