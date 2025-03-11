package Model;

/**
 * Represents a client with basic information.
 */
public class Client {
    private int id;
    private String name;
    private String email;
    private String password;
    private String address;

    /**
     * Constructs a new Client object with default values.
     */
    public Client() {
    }

    /**
     * Constructs a new Client object with specified values.
     *
     * @param id      The unique identifier of the client.
     * @param name    The name of the client.
     * @param email   The email address of the client.
     * @param password The password of the client.
     * @param address The address of the client.
     */
    public Client(int id, String name, String email, String password, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
    }

    /**
     * Constructs a new Client object with specified values.
     *
     * @param name    The name of the client.
     * @param email   The email address of the client.
     * @param password The password of the client.
     * @param address The address of the client.
     */
    public Client(String name, String email, String password, String address) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
    }

    /**
     * Gets the unique identifier of the client.
     *
     * @return The unique identifier of the client.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the client.
     *
     * @param id The unique identifier of the client.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the name of the client.
     *
     * @return The name of the client.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the client.
     *
     * @param name The name of the client.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the email address of the client.
     *
     * @return The email address of the client.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the client.
     *
     * @param email The email address of the client.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the password of the client.
     *
     * @return The password of the client.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the client.
     *
     * @param password The password of the client.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the address of the client.
     *
     * @return The address of the client.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address of the client.
     *
     * @param address The address of the client.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Returns a string representation of the client.
     *
     * @return A string representation of the client.
     */
    @Override
    public String toString() {
        return "Client:" + "\n name = " + name + "\n address = " + address + "\n email = " + email;
    }
}
