package pojos;

import java.io.Serializable;

public class User {

    private int id;
    public String username;
    public String password;

    public User(int id, String username, String password) {
        this.id = id;
        this.username =getUsername();
        this.password = password;
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

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" + "username=" + username + ", password=" + password + ", userId=" + id + '}';
    }
}
