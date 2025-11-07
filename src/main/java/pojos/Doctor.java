package pojos;

public class Doctor { //TODO class
    private static int nextId = 0;
    private int doctorId;
    private String name;
    private String specialty;
    //....
    public Doctor(String name, String specialty /*...*/) {
        this.name = name;
        this.specialty = specialty;
        //...
        this.doctorId = generateDoctorId();
    }
    private static int generateDoctorId() {
        int id = nextId;
        nextId++;
        return id;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public String getName() {
        return name;
    }

    public String getSpecialty() {
        return specialty;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "doctorId=" + doctorId +
                ", name='" + name + '\'' +
                ", specialty='" + specialty + '\'' +
                '}';
    }
}
