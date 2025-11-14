package pojos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MedicalHistory {

    private String recordId;          // identificador único del historial
    private LocalDate date;        // fecha de la medición o visita
    private int clientId;
    private int doctorId;
    private Signal signalEMG;
    private Signal signalECG;
    private String observations;          // observaciones médicas o comentarios
    private List<String> symptomsList;

    public MedicalHistory() {
        //constructor vacio
    }

    public MedicalHistory(int clientId, int doctorId, String observations) {
        this.recordId = "RECORD_"+clientId;
        this.date=LocalDate.now();
        this.clientId = clientId;
        this.doctorId = doctorId;
        this.observations = observations;
        this.symptomsList = new ArrayList<String>();
    }

    public MedicalHistory(int clientId, int doctorId) {
        this.recordId = "RECORD_" + clientId;
        this.date = LocalDate.now();
        this.clientId = clientId;
        this.doctorId = doctorId;
        this.observations = ""; // se deja vacío por defecto
        this.symptomsList = new ArrayList<>();
    }
    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int patientId) {
        this.clientId = patientId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public Signal getSignalEMG() {
        return signalEMG;
    }

    public void setSignalEMG(Signal signalEMG) {
        this.signalEMG = signalEMG;
    }

    public Signal getSignalECG() {
        return signalECG;
    }

    public void setSignalECG(Signal signalECG) {
        this.signalECG = signalECG;
    }

    public List<String> getSymptomsList() {
        return symptomsList;
    }

    public void setSymptomsList(List<String> symptomsList) {
        this.symptomsList = symptomsList;
    }

    /*
    TODO ESTE METODO TIENE QUE IR A UNA CLASE DE SERVICIO DEL SERVIDOR

    public void recordSignals(){
        System.out.println("elige si queires ECG o EMG");
        //switch
        //TODO el resto
    }*/

    /*
    @Override
    public String toString() {
        return "MedicalHistory{" +
                "clientName='" + clientName + '\'' +
                ", clientSurname='" + clientSurname + '\'' +
                ", age=" + dob +
                ", height=" + height +
                ", weight=" + weight +
                '}';
    }*/
}
