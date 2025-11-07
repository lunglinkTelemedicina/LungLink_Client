package pojos;

import java.util.List;

public class Doctor { //TODO class
    private int doctorId;
    private String name;
    private String surname;
    private String email;
    private DoctorSpecialty specialty;
    private List<Client> patients;


    public Doctor(String name,String surname, DoctorSpecialty specialty,String email, int doctorId ) {
        this.name = name;
        this.surname=surname;
        this.specialty = specialty;
        this.email=email;
        this.doctorId = doctorId;
    }


    public String getName() {
        return name;
    }
    public String getSurname() {return surname; }
    public DoctorSpecialty getSpecialty() {
        return specialty;
    }
    public String getEmail() { return email; }
    public int getDoctorId() {return doctorId; }

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
