package tests;

import Bitalino.BITalino;

public class TestBitalinoMain {

    private static final String MAC = "84:BA:20:5E:FD:7B";

    public static void main(String[] args) {

        System.out.println("BITALINO CONNECTION TEST");

        BITalino bitalino = new BITalino();

        try {

            System.out.println("Connecting to: " + MAC);
            bitalino.open(MAC, 1000);
            System.out.println("Connected via Bluetooth");

            int[] channels = {0};
            bitalino.start(channels);
            System.out.println("Device responded to START");

            bitalino.stop();
            System.out.println("Device responded to STOP");

            bitalino.close();
            System.out.println("Connection closed");

            System.out.println("TEST PASSED");

        } catch (Throwable t) {
            System.err.println("\nERROR DURING TEST:");
            System.err.println("Message: " + t.getMessage());
            t.printStackTrace();
        }
    }
}
