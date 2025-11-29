package pojos;


/**
 * Represents a user in the system with basic authentication information.
 * Contains user identification, username and password fields.
 */
public class User {

    private int id;
    public String username;
    public String password;

    /**
     * Default constructor for User class.
     */
    public User() {}

    /**
     * Creates a new User with all fields specified.
     *
     * @param id       The unique identifier for the user
     * @param username The username for authentication
     * @param password The password for authentication
     */
    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    /**
     * Creates a new User with username and password.
     *
     * @param username The username for authentication
     * @param password The password for authentication
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Gets the user's ID.
     *
     * @return The user's unique identifier
     */
    public int  getId() {
        return id;
    }

    /**
     * Sets the user's ID.
     *
     * @param id The unique identifier to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the user's username.
     *
     * @return The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the user's username.
     *
     * @param username The username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the user's password.
     *
     * @return The password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user's password.
     *
     * @param password The password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns a string representation of the User.
     *
     * @return A string containing the username, password, and user ID
     */
    @Override
    public String toString() {
        return "User{" + "username=" + username + ", password=" + password + ", userId=" + id + '}';
    }
}
