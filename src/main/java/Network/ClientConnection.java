package Network;

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


    /* Send binary samples (BITalino) */
    public void sendBytes(byte[] data){
        try{
            dataOut.writeInt(data.length);
            dataOut.write(data);
            dataOut.flush();
        }catch(IOException e){
            System.out.println("Error sendign bytes." + e.getMessage());
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
