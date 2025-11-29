package pojos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a client in the medical system with their personal and medical information.
 * This class stores all relevant data about a client including their identification,
 * personal details, and medical records.
 */
public class Client {

    /**
     * Unique identifier for the client
     */
    private int clientId;
    /**
     * First name of the client
     */
    private String name;
    /**
     * Last name of the client
     */
    private String surname;
    /**
     * Date of birth of the client
     */
    private LocalDate dob;
    /**
     * Email address of the client
     */
    private String mail;
    /**
     * Biological sex of the client
     */
    private Sex sex;
    /**
     * List containing the client's medical history records
     */
    private List<MedicalHistory> medicalHistory;
    /**
     * Current weight of the client in kilograms
     */
    private double weight;
    /**
     * Height of the client in meters
     */
    private double height;
    /**
     * Identifier of the assigned doctor
     */
    private int doctorId;
    /**
     * Associated user account identifier
     */
    private int userId;

    /**
     * Default constructor for Client class.
     */
    public Client() {

    }

    /**
     * Constructs a new Client with all fields initialized.
     *
     * @param clientId       Unique identifier for the client
     * @param name           First name of the client
     * @param surname        Last name of the client
     * @param dob            Date of birth of the client
     * @param mail           Email address of the client
     * @param sex            Biological sex of the client
     * @param medicalHistory List of medical history records
     * @param weight         Weight in kilograms
     * @param height         Height in meters
     * @param doctorId       Assigned doctor's identifier
     * @param userId         Associated user account identifier
     */
    public Client(int clientId, String name, String surname, LocalDate dob, String mail, Sex sex, List<MedicalHistory> medicalHistory, double weight, double height, int doctorId, int userId) {
        this.clientId = clientId;
        this.name = name;
        this.surname = surname;
        this.dob = dob;
        this.mail = mail;
        this.sex = sex;
        this.medicalHistory = medicalHistory;
        this.weight = weight;
        this.height = height;
        this.doctorId = doctorId;
        this.userId = userId;
    }

    /**
     * @return The client's unique identifier
     */
    public int getClientId() {
        return clientId;
    }

    /**
     * @param clientId The client's unique identifier to set
     */
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    /**
     * @return The client's first name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The client's first name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The client's last name
     */
    public String getSurname() {
        return surname;
    }

    /**
     * @param surname The client's last name to set
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * @return The client's date of birth
     */
    public LocalDate getDob() {
        return dob;
    }

    /**
     * @param dob The client's date of birth to set
     */
    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    /**
     * @return The client's email address
     */
    public String getMail() {
        return mail;
    }

    /**
     * @param mail The client's email address to set
     */
    public void setMail(String mail) {
        this.mail = mail;
    }

    /**
     * @return The client's biological sex
     */
    public Sex getSex() {
        return sex;
    }

    /**
     * @param sex The client's biological sex to set
     */
    public void setSex(Sex sex) {
        this.sex = sex;
    }

    /**
     * @return The client's height in meters
     */
    public double getHeight() {
        return height;
    }

    /**
     * @param height The client's height in meters to set
     */
    public void setHeight(double height) {
        this.height = height;
    }

    /**
     * @return The client's weight in kilograms
     */
    public double getWeight() {
        return weight;
    }

    /**
     * @param weight The client's weight in kilograms to set
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     * @return The list of client's medical history records
     */
    public List<MedicalHistory> getMedicalHistory() {
        return medicalHistory;
    }

    /**
     * @param medicalHistory The list of medical history records to set
     */
    public void setMedicalHistory(List<MedicalHistory> medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    /**
     * @return The identifier of the assigned doctor
     */
    public int getDoctorId() {
        return doctorId;
    }

    /**
     * @param doctorId The identifier of the assigned doctor to set
     */
    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    /**
     * @return The associated user account identifier
     */
    public int getUserId() {
        return userId;
    }

    /**
     * @param userId The associated user account identifier to set
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Client{" +
                "clientId=" + clientId +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", dob=" + dob +
                ", mail='" + mail + '\'' +
                ", sex=" + sex +
                ", medicalHistory=" + medicalHistory +
                ", weight=" + weight +
                ", height=" + height +
                ", doctorId=" + doctorId +
                ", userId=" + userId +
                '}';
    }
}
