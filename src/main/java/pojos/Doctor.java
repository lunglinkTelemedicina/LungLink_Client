package pojos;

import java.util.List;

/**
 * Represents a medical doctor in the healthcare system.
 * Contains personal information and professional details of the doctor,
 * including their specialty and list of assigned patients.
 */
public class Doctor {
    private int doctorId;
    private String name;
    private String surname;
    private String email;
    private DoctorSpecialty specialty;
    private List<Client> patients;


    /**
     * Constructs a new Doctor with the specified details.
     *
     * @param name      The first name of the doctor
     * @param surname   The last name of the doctor
     * @param specialty The medical specialty of the doctor
     * @param email     The email address of the doctor
     * @param doctorId  The unique identifier for the doctor
     */
    public Doctor(String name,String surname, DoctorSpecialty specialty,String email, int doctorId ) {
        this.name = name;
        this.surname=surname;
        this.specialty = specialty;
        this.email=email;
        this.doctorId = doctorId;
    }


    /**
     * @return The doctor's unique identifier
     */
    public int getDoctorId() {
        return doctorId;
    }

    /**
     * @param doctorId The doctor's unique identifier to set
     */
    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    /**
     * @return The doctor's first name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The doctor's first name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The doctor's last name
     */
    public String getSurname() {
        return surname;
    }

    /**
     * @param surname The doctor's last name to set
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * @return The doctor's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email The doctor's email address to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return The doctor's medical specialty
     */
    public DoctorSpecialty getSpecialty() {
        return specialty;
    }

    /**
     * @param specialty The doctor's medical specialty to set
     */
    public void setSpecialty(DoctorSpecialty specialty) {
        this.specialty = specialty;
    }

    /**
     * @return List of patients assigned to this doctor
     */
    public List<Client> getPatients() {
        return patients;
    }

    /**
     * @param patients List of patients to assign to this doctor
     */
    public void setPatients(List<Client> patients) {
        this.patients = patients;
    }

    /**
     * Returns a string representation of the Doctor object.
     *
     * @return A string containing all doctor's details
     */
    @Override
    public String toString() {
        return "Doctor{" +
                "doctorId=" + doctorId +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", specialty=" + specialty +
                ", patients=" + patients +
                '}';
    }
}
