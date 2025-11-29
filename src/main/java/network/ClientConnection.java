package network;

import utils.UIUtils;
import pojos.*;
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

    public void sendCommand(String msg)throws IOException{
            dataOut.writeUTF(msg);
            dataOut.flush();

    }

    public String receiveResponse()throws IOException {
            String reply = dataIn.readUTF();

            if (reply.equals("SERVER_SHUTDOWN")) {
                System.out.println("Server is shutting down. Closing client...");
                releaseResources();
                System.exit(0);
            }

            return reply;
    }


    /**
     * Sends binary data to the server. Used primarily for BITalino signal data transmission.
     * The method first sends the length of the data array followed by the actual bytes.
     *
     * @param data The byte array containing the data to be sent
     * @throws IOException If an error occurs while writing to the output stream
     */
    public void sendBytes(byte[] data)throws IOException{
            dataOut.writeInt(data.length);
            dataOut.write(data);
            dataOut.flush();

    }

    public void sendSignalFromBITalino(Signal signal) throws IOException{
        try {
            sendCommand("SEND_" + signal.getType().name() + "|" + signal.getClientId() + "|" + signal.getValues().size());

            String response = receiveResponse();
            if (response == null || !response.contains("Client can send the data")) {
                System.out.println("Server did not authorize sending data.");
                return;
            }

            sendBytes(signal.toByteArray());


            String finalResponse = receiveResponse();
            System.out.println("Server final response: " + finalResponse);

            System.out.println("Signal sent successfully (BITalino).");

        } catch (Exception e) {
            System.out.println("Error sending BITalino signal: " + e.getMessage());
        }
    }


    public void disconnect() throws IOException{
        try {
            sendCommand("DISCONNECT");
        }catch (IOException ignored){
            // (Connection reset by peer)
        }
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
