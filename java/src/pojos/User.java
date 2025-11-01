package pojos;

import java.io.Serializable;

public class User implements Serializable{

    private int id;
    public String username;
    public byte[] password;

    public User(int id, byte[] password) {
        this.id = id;
        this.username =getUsername();//TODO funcion que coja todo lo que esta antes del @ del mail
        this.password = password; //TODO mas adelante crear encryption
    }

    public int  getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public byte[] getPassword() {
        return password;
    }
    public void setPassword(byte[] password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" + "username=" + username + ", password=" + password + ", userId=" + id + '}';
    }
}
