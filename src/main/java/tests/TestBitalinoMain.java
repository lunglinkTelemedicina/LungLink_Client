package tests;

import Bitalino.BITalino;
import Bitalino.Frame;
import javax.bluetooth.RemoteDevice;
import java.util.Vector;

public class TestBitalinoMain {

    public static void main(String[] args) {

        System.out.println("BITALINO REAL COMMUNICATION TEST");

        BITalino b = new BITalino();

        try {
            System.out.println("Searching for BITalino devices...");
            Vector<RemoteDevice> devices = b.findDevices();

            if (devices.isEmpty()) {
                System.out.println("No BITalino devices found!");
                return;
            }

            for (RemoteDevice d : devices) {
                try {
                    System.out.println("Found device: " + d.getBluetoothAddress() +
                            " - Name: " + d.getFriendlyName(true));
                } catch (Throwable ignored) {}
            }

            String macAddress = "84:BA:20:5E:FD:7B";
            System.out.println("Connecting to BITalino: " + macAddress);

            b.open(macAddress, 1000);
            System.out.println("Connected!");

            int[] channels = {0};
            b.start(channels);
            System.out.println("Acquisition started...");

            System.out.println("Reading 10 samples...");
            Frame[] frames = b.read(10);

            for (int i = 0; i < frames.length; i++) {
                System.out.println("Sample " + i + ": " + frames[i].analog[0]);
            }

            b.stop();
            b.close();

            System.out.println("BITalino test finished!");

        } catch (Throwable t) {
            System.out.println("BITalino test FAILED: " + t.getMessage());
            t.printStackTrace();
        }
    }
}
