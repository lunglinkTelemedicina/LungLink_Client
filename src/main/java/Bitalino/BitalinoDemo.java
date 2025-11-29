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

    private static final int SAMPLING_RATE = 100; // 100 samples/sec
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
            // connect the BITalino
            BITalino device = connectToBitalino();

            // ECG = channel 2 -> index 1
            int[] channelsToAcquire = {1};
            device.start(channelsToAcquire);

            List<Integer> samples = new ArrayList<>();

            // read samples
            int remaining = NUM_SAMPLES;
            while (remaining > 0) {

                Frame[] frames = device.read(Math.min(10, remaining));

                for (Frame f : frames) {
                    samples.add(f.analog[0]); // 1 chanel → analog[0]
                }

                remaining -= frames.length;
            }

            // stops
            device.stop();
            device.close();

            // creates the signal for the server
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

            // EMG = channel 1 -> index 0
            int[] channelsToAcquire = {0};
            device.start(channelsToAcquire);

            List<Integer> samples = new ArrayList<>();

            int remaining = NUM_SAMPLES;
            while (remaining > 0) { //reads from 10 to 10 till it gets to 600 samples

                Frame[] frames = device.read(Math.min(10, remaining)); // each frame is a sample

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
