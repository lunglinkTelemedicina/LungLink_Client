package Bitalino;

import network.ClientConnection;
import pojos.Signal;
import pojos.TypeSignal;
import utils.UIUtils;     // PARA LEER LA MAC DESDE TERMINAL

import java.util.ArrayList;
import java.util.List;

public class BitalinoDemo {

    private static final int SAMPLING_RATE = 100;
    private static final int NUM_SAMPLES = 200;


    private BITalino connectToBitalino() throws Exception {

        System.out.println("BITalino Connection");

        // PEDIR LA MAC AL USUARIO
        String mac = UIUtils.readString("Enter BITalino MAC Address (Ej: 98:D3:41:FD:4E:E8): ");
        mac = mac.trim();
        BITalino device = new BITalino();

        System.out.println("Connecting to BITalino at MAC: " + mac);
        device.open(mac, SAMPLING_RATE);

        return device;
    }

    public void acquireECGfromBITalino(ClientConnection connection, int clientId) {

        try {
            // 1. Conectar BITalino
            BITalino device = connectToBitalino();

            // 2. Canal ECG = canal físico 2 → índice interno 2
            int[] channelsToAcquire = {1};
            device.start(channelsToAcquire);

            List<Integer> samples = new ArrayList<>();

            // 3. Leer 200 muestras
            int remaining = NUM_SAMPLES;
            while (remaining > 0) {

                Frame[] frames = device.read(Math.min(10, remaining));

                for (Frame f : frames) {
                    samples.add(f.analog[0]); // 1 canal → analog[0]
                }

                remaining -= frames.length;
            }

            // 4. Parar
            device.stop();
            device.close();

            // 5. Crear señal para el servidor
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

            // EMG = canal físico 1
            int[] channelsToAcquire = {0};
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

        } catch (Exception e) {
            System.out.println("Error acquiring EMG: " + e.getMessage());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
