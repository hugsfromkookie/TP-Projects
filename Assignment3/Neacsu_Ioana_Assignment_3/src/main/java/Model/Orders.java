package Model;

/**
 * Represents an order made by a client.
 */
public class Orders {
    private int id;
    private int quantity;
    private Double totalPrice;
    private int clientID;

    /**
     * Constructs a new Order object with the specified quantity, total price, and client ID.
     *
     * @param quantity   The quantity of the order.
     * @param totalPrice The total price of the order.
     * @param clientID   The ID of the client who made the order.
     */
    public Orders(int quantity, Double totalPrice, int clientID) {
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.clientID = clientID;
    }

    /**
     * Gets the unique identifier of the order.
     *
     * @return The unique identifier of the order.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the order.
     *
     * @param id The unique identifier of the order.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the quantity of the order.
     *
     * @return The quantity of the order.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of the order.
     *
     * @param quantity The quantity of the order.
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Gets the total price of the order.
     *
     * @return The total price of the order.
     */
    public Double getTotalPrice() {
        return totalPrice;
    }

    /**
     * Sets the total price of the order.
     *
     * @param totalPrice The total price of the order.
     */
    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     * Gets the ID of the client who made the order.
     *
     * @return The ID of the client who made the order.
     */
    public int getClientID() {
        return clientID;
    }

}
