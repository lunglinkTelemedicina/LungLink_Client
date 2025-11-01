package pojos;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class MedicalHistory {

    //private String clientName;
    //private String clientSurname;
    //private LocalDate dob;
    private int recordId;          // identificador único del historial
    private LocalDate date;        // fecha de la medición o visita
    private int patientId;
    private int doctorId;
    private Signal signalEMG;
    private Signal signalECG;
    //private double height;         // altura registrada en ese momento
    //private double weight;         // peso registrado
    //private Status status;         // enum: HEALTHY, UNHEALTHY, UNKNOWN
    private String data;          // observaciones médicas o comentarios
    //private Signal signalEMG;      // señal electromiográfica
    //private Signal signalECG;      // señal electrocardiográfica
    //cuando sepamos cuales son hay que añadir las señales del bitalino
    //tambien los doctors cuando toque
    private List<Symptoms> symptomsList;

    public MedicalHistory() {
        //constructor vacio
    }

/*
    public MedicalHistory(String clientName, String clientSurname, LocalDate dob, double height, double weight) {
        this.clientName = clientName;
        this.clientSurname = clientSurname;
        this.dob = dob;
        this.height = height;
        this.weight = weight;
    }


    public String getClientName() {
        return clientName;
    }
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientSurname() {
        return clientSurname;
    }
    public void setClientSurname(String clientSurname) {
        this.clientSurname = clientSurname;
    }

    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }*/

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    /*public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }*/

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
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

    public List<Symptoms> getSymptomsList() {
        return symptomsList;
    }

    public void setSymptomsList(List<Symptoms> symptomsList) {
        this.symptomsList = symptomsList;
    }

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
