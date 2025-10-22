package src.pojos;

public class Client {

    private String name;
    private String surname;
    private User user;
    private List<MedicalHistory> medicalHistory;

    public class Client(){

    }

    public Client(String name, String surname, User user){
        this.name = name;
        this.surname = surname;
        this.medicalHistory = new ArrayList<MedicalHistory>();
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }

    public User getUser(){
        return user;
    }
    public void setUser(User user){
        this.user = user;
    }

    public List<MedicalHistory> getMedicalHistory(){
        return medicalHistory;
    }
    public void setMedicalHistory(List<MedicalHistory> medicalHistory){
        this.medicalHistory = medicalHistory;
    }

    @Override
    public String toString() {
        return "- Name: " + name + '\'' +
                "- Surname: " + surname + '\'';
    }

}
