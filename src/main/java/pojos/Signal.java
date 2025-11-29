package pojos;

import java.util.ArrayList;
import java.util.List;

public class Signal {

    private TypeSignal type;         // ECG or EMG
    private int clientId;            // Patient
    private int recordId;            // Used only by the server
    private List<Integer> values;    // Signal samples

    public Signal() {
        this.values = new ArrayList<>();
    }

    public Signal(TypeSignal type, int clientId) {
        this.type = type;
        this.clientId = clientId;
        this.values = new ArrayList<>();
    }


    public TypeSignal getType() {
        return type;
    }

    public void setType(TypeSignal type) {
        this.type = type;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public List<Integer> getValues() {
        return values;
    }

    public void setValues(List<Integer> values) {
        this.values = values;
    }

    public void addSample(int sample) {
        values.add(sample);
    }




    public byte[] toByteArray() {
        byte[] data = new byte[values.size() * 2]; //each sample=2 bytes; 1200 bytes
        int pos = 0;

        for (int v : values) {
            short s = (short) v; // short(16-bit)
            //storing the most significant byte
            data[pos++] = (byte) (s >> 8); // high byte. Shifts the short 8 bytes to the right
            //storing the least significant byte
            data[pos++] = (byte) s; // low byte
            //[|(OR)]

        }
        return data;
    }
}

