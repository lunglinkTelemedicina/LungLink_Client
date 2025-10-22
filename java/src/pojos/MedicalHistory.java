package src.pojos;

public class MedicalHistory {

    private String clientName;
    private String clientSurname;
    private int age;
    private double height;
    private double weight;
    //cuando sepamos cuales son hay que añadir las señales del bitalino
    //tambien los doctors cuando toque


    public MedicalHistory(String clientName, String clientSurname, int age, double height, double weight) {
        this.clientName = clientName;
        this.clientSurname = clientSurname;
        this.age = age;
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

    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }

    public double getHeight() {
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

    @Override
    public String toString() {
        return "MedicalHistory{" +
                "clientName='" + clientName + '\'' +
                ", clientSurname='" + clientSurname + '\'' +
                ", age=" + age +
                ", height=" + height +
                ", weight=" + weight +
                '}';
    }
}
