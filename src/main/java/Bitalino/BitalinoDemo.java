package Bitalino;

import network.ClientConnection;
import pojos.Signal;
import pojos.TypeSignal;
import utils.UIUtils;     // PARA LEER LA MAC DESDE TERMINAL

import javax.bluetooth.RemoteDevice;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class BitalinoDemo {

    private static final int SAMPLING_RATE = 100; // 100 muestras/segundo
    private static final int NUM_SAMPLES = 600;


    private BITalino connectToBitalino() throws Exception {

        System.out.println("BITalino Connection");

        BITalino device = new BITalino();
        String mac = UIUtils.readString("Enter BITalino MAC (Example: 84:BA:20:5E:FD:7B): ");
        mac = mac.trim();

        System.out.println("Connecting to BITalino at MAC: " + mac);
        device.open(mac, SAMPLING_RATE);

        return device;
    }

    public void acquireECGfromBITalino(ClientConnection connection, int clientId) {

        try {
            // Conectar BITalino
            BITalino device = connectToBitalino();

            // ECG = canal físico 2 -> indice 1
            int[] channelsToAcquire = {1};
            device.start(channelsToAcquire);

            List<Integer> samples = new ArrayList<>();

            // Lee 200 muestras
            int remaining = NUM_SAMPLES;
            while (remaining > 0) {

                Frame[] frames = device.read(Math.min(10, remaining));

                for (Frame f : frames) {
                    samples.add(f.analog[0]); // 1 canal → analog[0]
                }

                remaining -= frames.length;
            }

            // Para
            device.stop();
            device.close();

            // Crea la señal para el servidor
            Signal s = new Signal(TypeSignal.ECG, clientId);
            samples.forEach(s::addSample);

            connection.sendSignalFromBITalino(s);

            System.out.println("ECG sent to server (" + samples.size() + " samples)");

        } catch (Exception e) {
            System.out.println("Error acquiring ECG: " + e.getMessage());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }


    public void acquireEMGfromBITalino(ClientConnection connection, int clientId) {

        try {
            BITalino device = connectToBitalino();

            // EMG = canal físico 1 -> indice 0
            int[] channelsToAcquire = {0};
            device.start(channelsToAcquire);

            List<Integer> samples = new ArrayList<>();

            int remaining = NUM_SAMPLES;
            while (remaining > 0) { //lee de 10 en 10 frames hasta llegar a 200 muestras

                Frame[] frames = device.read(Math.min(10, remaining)); //lee en bloques de 10, cada frame es una muestra

                for (Frame f : frames) {
                    samples.add(f.analog[0]);
                }

                remaining -= frames.length;
            }

            device.stop();
            device.close();

            Signal s = new Signal(TypeSignal.EMG, clientId);
            samples.forEach(s::addSample);

            connection.sendSignalFromBITalino(s);

            System.out.println("EMG sent to server (" + samples.size() + " samples)");

        } catch (Exception e) {
            System.out.println("Error acquiring EMG: " + e.getMessage());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
