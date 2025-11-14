package Network;

import java.io.*;
import java.net.Socket;

/**
 * Connects to the server on the specified IP and port.
 * Returns true if the connection was successful.
 */

public class ClientConnection {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public boolean connect(String ip, int port){
        try{
            socket = new Socket(ip, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Connected to server.");
            return true;
        }catch(IOException e){
            System.out.println("Connection failed.");
            return false;
        }
    }

    public void send(String line){
        out.println(line);
    }

    public String receive(){
        try{
            return in.readLine();
        }catch(IOException e){
            return null;
        }
    }

    public void disconnect() {
        send("DISCONNECT");
        releaseResources();
        System.out.println("Disconnected from server.");
    }

    private void releaseResources() {
        try {
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            System.out.println("Error closing input stream: " + e.getMessage());
        }
        try {
            if (out != null) {
                out.close();
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
