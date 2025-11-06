package sendData;

import pojos.Client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SendDataViaNetwork {
    public static void main(String[] args) {
        OutputStream outputStream = null;
        ObjectOutputStream objectOutputStream = null;
        Socket socket = null;

        //Probando a enviar objetos tipo Client
        Client[] patients = new Client[2];
        //TODO crear pacientes para enviar
        //patients[0] = new Client("");
       // patients[1] = new Client("");

        try{
            socket = new Socket("localhost", 9000);
            outputStream = socket.getOutputStream();
        } catch (IOException e) {
            System.out.println("It was not possible to connect to the server.");
            System.exit(-1);
            Logger.getLogger(SendDataViaNetwork.class.getName()).log(Level.SEVERE,null,e);
        }
        try{
            objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(patients[0]);
            objectOutputStream.writeObject(patients[1]);
            objectOutputStream.flush();
        } catch (IOException e) {
            System.out.println("Unable to write the objects on the server.");
            Logger.getLogger(SendDataViaNetwork.class.getName()).log(Level.SEVERE,null,e);
        } finally {
            releaseResources(objectOutputStream, socket);
        }
    }

    public static void releaseResources(ObjectOutputStream objectOutputStream, Socket socket) {
        try{
            objectOutputStream.close();
        } catch (IOException e) {
            Logger.getLogger(SendDataViaNetwork.class.getName()).log(Level.SEVERE,null,e);
        }
        try{
            socket.close();
        } catch (IOException e) {
            Logger.getLogger(SendDataViaNetwork.class.getName()).log(Level.SEVERE,null,e);
        }

    }
}