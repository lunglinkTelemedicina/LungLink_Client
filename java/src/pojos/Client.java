package src.pojos;

import java.util.List;

public class Client {

    private int clientId;
    private String name;
    private String surname;
    private int age;
    private String mail;
    private Sex sex;
    private User user;
    private List<MedicalHistory> medicalHistory;
    //private List<Doctor> doctors;

    public Client(){

    }

    public Client(int clientId, String name, String surname, int age, String mail, Sex sex, User user, List<MedicalHistory> medicalHistory) {
        this.clientId = clientId;
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.mail = mail;
        this.sex = sex;
        this.user = user;
        this.medicalHistory = medicalHistory;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<MedicalHistory> getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(List<MedicalHistory> medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    @Override
    public String toString() {
        return "Client{" +
                "clientId=" + clientId +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", age=" + age +
                ", mail='" + mail + '\'' +
                ", sex=" + sex +
                ", user=" + user +
                ", medicalHistory=" + medicalHistory +
                '}';
    }
}
