package services;
import Bitalino.BITalino;
import Bitalino.Frame;
import pojos.Signal;
import pojos.TypeSignal;

import javax.bluetooth.RemoteDevice;
import java.util.Vector;

public class BitalinoService{

    private final String macAddress;
    private final int samplingRate;

    public BitalinoService(String macAddress, int samplingRate) {
        this.macAddress = macAddress;
        this.samplingRate = samplingRate;
    }

    public Signal acquireSignal(TypeSignal type, int clientId, int seconds) throws Throwable {

        Signal signal = new Signal(type, clientId);
        BITalino bitalino = null;

        try {
            bitalino = new BITalino();

            // Optional: to find devices
            Vector<RemoteDevice> devices = bitalino.findDevices();
            System.out.println("Devices: " + devices);

            //Open connection
            bitalino.open(macAddress, samplingRate);

            //Select channel
            int channel = (type == TypeSignal.ECG) ? 1 : 5;
            int[] channels = { channel };

            bitalino.start(channels);

            int blockSize = 10;
            int totalSamples = samplingRate * seconds;
            int iterations = totalSamples / blockSize;

            for (int j = 0; j < iterations; j++) {

                Frame[] frames = bitalino.read(blockSize);

                for (Frame f : frames) {
                    int sample = f.analog[0];
                    signal.addSample(sample);
                }
            }

            bitalino.stop();

        } finally {
            if (bitalino != null) bitalino.close();
        }

        return signal;
    }
}
