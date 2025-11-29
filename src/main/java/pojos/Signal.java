package pojos;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a biological signal (ECG or EMG) with its associated metadata and sample values.
 * This class is used to store and manage signal data from medical monitoring devices.
 */
public class Signal {

    /**
     * The type of biological signal (ECG or EMG)
     */
    private TypeSignal type;
    /**
     * The unique identifier of the patient/client
     */
    private int clientId;
    /**
     * The record identifier used by the server for storage
     */
    private int recordId;
    /**
     * The list of signal sample values
     */
    private List<Integer> values;

    /**
     * Default constructor. Initializes an empty signal with no type or client association.
     */
    public Signal() {
        this.values = new ArrayList<>();
    }

    /**
     * Creates a new signal for a specific client with a defined type.
     *
     * @param type     The type of biological signal (ECG or EMG)
     * @param clientId The unique identifier of the client/patient
     */
    public Signal(TypeSignal type, int clientId) {
        this.type = type;
        this.clientId = clientId;
        this.values = new ArrayList<>();
    }


    /**
     * @return The type of biological signal
     */
    public TypeSignal getType() {
        return type;
    }

    /**
     * @param type The type of biological signal to set
     */
    public void setType(TypeSignal type) {
        this.type = type;
    }

    /**
     * @return The client/patient identifier
     */
    public int getClientId() {
        return clientId;
    }

    /**
     * @param clientId The client/patient identifier to set
     */
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    /**
     * @return The record identifier used by the server
     */
    public int getRecordId() {
        return recordId;
    }

    /**
     * @param recordId The record identifier to set
     */
    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    /**
     * @return The list of signal sample values
     */
    public List<Integer> getValues() {
        return values;
    }

    /**
     * @param values The list of signal sample values to set
     */
    public void setValues(List<Integer> values) {
        this.values = values;
    }

    /**
     * Adds a new sample value to the signal.
     *
     * @param sample The sample value to add
     */
    public void addSample(int sample) {
        values.add(sample);
    }

    /**
     * Converts the signal values to a byte array for transmission.
     * Each sample is converted to a 16-bit value (2 bytes).
     *
     * @return A byte array containing all signal values
     */


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

