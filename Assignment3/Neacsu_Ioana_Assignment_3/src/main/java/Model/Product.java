package Model;

/**
 * Represents a product in the inventory.
 */
public class Product {
    private int id;
    private String name;
    private int quantity;
    private Double price;

    /**
     * Constructs a new Product with default values.
     */
    public Product() {
    }

    /**
     * Constructs a new Product with the specified values.
     *
     * @param id       The unique identifier of the product.
     * @param name     The name of the product.
     * @param quantity The quantity of the product available in the inventory.
     * @param price    The price of the product.
     */
    public Product(int id, String name, int quantity, Double price) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    /**
     * Constructs a new Product with the specified values.
     *
     * @param name     The name of the product.
     * @param quantity The quantity of the product available in the inventory.
     * @param price    The price of the product.
     */
    public Product(String name, int quantity, Double price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    /**
     * Retrieves the unique identifier of the product.
     *
     * @return The unique identifier of the product.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the product.
     *
     * @param id The unique identifier of the product.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retrieves the price of the product.
     *
     * @return The price of the product.
     */
    public Double getPrice() {
        return price;
    }

    /**
     * Sets the price of the product.
     *
     * @param price The price of the product.
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * Retrieves the name of the product.
     *
     * @return The name of the product.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the product.
     *
     * @param name The name of the product.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the quantity of the product available in the inventory.
     *
     * @return The quantity of the product available in the inventory.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of the product available in the inventory.
     *
     * @param quantity The quantity of the product available in the inventory.
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Decreases the quantity of the product by the specified amount.
     *
     * @param quantity The quantity to decrease.
     */
    public void decreaseQuantity(int quantity) {
        this.quantity -= quantity;
    }

    /**
     * Returns a string representation of the product.
     *
     * @return A string representation of the product.
     */
    @Override
    public String toString() {
        return "Product [id=" + id + ", name=" + name + ", quantity=" + quantity + ", price=" + price + "]";
    }
}
