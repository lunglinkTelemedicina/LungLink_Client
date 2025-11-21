package network;

import pojos.Signal;
import pojos.TypeSignal;
import file.SignalFileManager;
import java.io.*;
import java.net.Socket;

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
}
