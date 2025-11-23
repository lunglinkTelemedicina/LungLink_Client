package Bitalino;

import network.ClientConnection;
import pojos.Signal;
import pojos.TypeSignal;

import java.util.ArrayList;
import java.util.List;

public class BitalinoDemo {

    private static final String MAC_ADDRESS = "98:D3:41:FD:4E:E8";
    private static final int SAMPLING_RATE = 100;
    private static final int NUM_SAMPLES = 200;

    public void acquireECGfromBITalino(ClientConnection connection, int clientId) {

        try {
            BITalino device = new BITalino();
            device.open(MAC_ADDRESS, SAMPLING_RATE);

            // ECG = canal 2
            int[] channelsToAcquire = {2};
            device.start(channelsToAcquire);

            List<Integer> samples = new ArrayList<>();

            int remaining = NUM_SAMPLES;
            while (remaining > 0) {

                Frame[] frames = device.read(Math.min(10, remaining));

                for (Frame f : frames) {
                    // IMPORTANTE: como solo pedimos 1 canal,
                    // analog[0] corresponde al canal 2
                    samples.add(f.analog[0]);
                }

                remaining -= frames.length;
            }

            device.stop();
            device.close();

            Signal s = new Signal(TypeSignal.ECG, clientId);
            samples.forEach(s::addSample);

            connection.sendSignalFromBITalino(s);

            System.out.println("ECG sent to server (" + samples.size() + " samples)");

        } catch (BITalinoException e) {
            System.out.println("BITalino error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error sending ECG to server: " + e.getMessage());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }


    public void acquireEMGfromBITalino(ClientConnection connection, int clientId) {

        try {
            BITalino device = new BITalino();
            device.open(MAC_ADDRESS, SAMPLING_RATE);

            // EMG = canal 1
            int[] channelsToAcquire = {1};
            device.start(channelsToAcquire);

            List<Integer> samples = new ArrayList<>();

            int remaining = NUM_SAMPLES;
            while (remaining > 0) {

                Frame[] frames = device.read(Math.min(10, remaining));

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

        } catch (BITalinoException e) {
            System.out.println("BITalino error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error sending EMG to server: " + e.getMessage());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
