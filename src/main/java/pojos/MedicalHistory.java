package pojos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a medical history record containing patient information, signals, and observations.
 * This class stores medical data including EMG and ECG signals, symptoms, and doctor observations.
 */
public class MedicalHistory {

    private String recordId;
    private LocalDate date;
    private int clientId;
    private int doctorId;
    private Signal signalEMG;
    private Signal signalECG;
    private String observations;
    private List<String> symptomsList;

    /**
     * Default constructor for MedicalHistory.
     * Initializes an empty medical history record.
     */
    public MedicalHistory() {
        //empty constructor
    }

    /**
     * Constructs a MedicalHistory with specified client, doctor, and observations.
     *
     * @param clientId     The ID of the client/patient
     * @param doctorId     The ID of the doctor
     * @param observations Medical observations or notes
     */
    public MedicalHistory(int clientId, int doctorId, String observations) {
        this.recordId = "RECORD_"+clientId;
        this.date=LocalDate.now();
        this.clientId = clientId;
        this.doctorId = doctorId;
        this.observations = observations;
        this.symptomsList = new ArrayList<String>();
    }

    /**
     * Constructs a MedicalHistory with specified client and doctor.
     * Initializes with empty observations and symptoms list.
     *
     * @param clientId The ID of the client/patient
     * @param doctorId The ID of the doctor
     */
    public MedicalHistory(int clientId, int doctorId) {
        this.recordId = "RECORD_" + clientId;
        this.date = LocalDate.now();
        this.clientId = clientId;
        this.doctorId = doctorId;
        this.observations = "";
        this.symptomsList = new ArrayList<>();
    }

    /**
     * Gets the record identifier.
     *
     * @return The record ID string
     */
    public String getRecordId() {
        return recordId;
    }

    /**
     * Sets the record identifier.
     *
     * @param recordId The record ID to set
     */
    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    /**
     * Gets the date of the medical record.
     *
     * @return The date of the record
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the date of the medical record.
     *
     * @param date The date to set
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Gets the medical observations.
     * @return The medical observations string
     */
    public String getObservations() {
        return observations;
    }

    /**
     * Sets the medical observations.
     *
     * @param observations The observations to set
     */
    public void setObservations(String observations) {
        this.observations = observations;
    }

    /**
     * Gets the client/patient ID.
     * @return The client ID
     */
    public int getClientId() {
        return clientId;
    }

    /**
     * Sets the client/patient ID.
     *
     * @param patientId The client ID to set
     */
    public void setClientId(int patientId) {
        this.clientId = patientId;
    }

    /**
     * Gets the doctor ID.
     * @return The doctor ID
     */
    public int getDoctorId() {
        return doctorId;
    }

    /**
     * Sets the doctor ID.
     *
     * @param doctorId The doctor ID to set
     */
    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    /**
     * Gets the EMG signal data.
     * @return The EMG signal
     */
    public Signal getSignalEMG() {
        return signalEMG;
    }

    /**
     * Sets the EMG signal data.
     *
     * @param signalEMG The EMG signal to set
     */
    public void setSignalEMG(Signal signalEMG) {
        this.signalEMG = signalEMG;
    }

    /**
     * Gets the ECG signal data.
     * @return The ECG signal
     */
    public Signal getSignalECG() {
        return signalECG;
    }

    /**
     * Sets the ECG signal data.
     *
     * @param signalECG The ECG signal to set
     */
    public void setSignalECG(Signal signalECG) {
        this.signalECG = signalECG;
    }

    /**
     * Gets the list of symptoms.
     * @return The list of symptoms
     */
    public List<String> getSymptomsList() {
        return symptomsList;
    }

    /**
     * Sets the list of symptoms.
     *
     * @param symptomsList The list of symptoms to set
     */
    public void setSymptomsList(List<String> symptomsList) {
        this.symptomsList = symptomsList;
    }

    /**
     * Returns a string representation of the medical history.
     * @return A string containing all medical history details
     */
    @Override
    public String toString() {
        return "MedicalHistory{" +
                "recordId='" + recordId + '\'' +
                ", date=" + date +
                ", clientId=" + clientId +
                ", doctorId=" + doctorId +
                ", signalEMG=" + signalEMG +
                ", signalECG=" + signalECG +
                ", observations='" + observations + '\'' +
                ", symptomsList=" + symptomsList +
                '}';
    }
}
