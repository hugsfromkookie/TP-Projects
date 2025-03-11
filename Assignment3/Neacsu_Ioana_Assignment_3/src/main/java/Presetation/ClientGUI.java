package Presetation;

import DataAccess.*;
import DataAccess.BillDAO;
import Model.Bill;
import Model.Client;
import Model.Orders;
import Model.Product;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents the graphical user interface for the client, allowing them to place orders for products.
 */
public class ClientGUI extends JFrame {
    private JPanel clientPanel;
    private JComboBox quantityComboBox;
    private JComboBox productComboBox;
    private JButton placeOrderButton;
    private JButton cancelButton;
    private JTextArea infoTextArea;
    private JTextField priceTextField;

    private Client client;

    /**
     * Constructs a new ClientGUI frame.
     *
     * @param parent   The parent frame.
     * @param idClient The ID of the client using the interface.
     */
    public ClientGUI(JFrame parent, int idClient) {
        setTitle("Client");
        setContentPane(clientPanel);
        setMinimumSize(new Dimension(500, 500));
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        infoTextArea.setEditable(false);
        priceTextField.setEditable(false);

        ClientDAO clientDAO = new ClientDAO();
        this.client = clientDAO.findById(idClient);
        infoTextArea.setText(client.toString());

        populateProductComboBox();

        productComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateQuantityComboBox();
                updatePriceField();
            }
        });

        quantityComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePriceField();
            }
        });

        placeOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placeOrder();
                parent.setVisible(true);
                dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.setVisible(true);
                dispose();
            }
        });

        setVisible(true);
    }

    /**
     * Places an order for the selected product.
     */
    private void placeOrder() {
        ProductDAO productDAO = new ProductDAO();
        String selectedProduct = (String) productComboBox.getSelectedItem();
        Product product = productDAO.findByName(selectedProduct);
        int quantity = (int) quantityComboBox.getSelectedItem();
        double totalPrice = product.getPrice() * quantity;

        product.decreaseQuantity(quantity);
        productDAO.update(product);

        Orders orders = new Orders(quantity, totalPrice, client.getId());
        OrderDAO orderDAO = new OrderDAO();
        orderDAO.insert(orders);

        Bill bill = new Bill(client.getId(), selectedProduct, quantity, totalPrice, LocalDateTime.now());
        BillDAO billDAO = new BillDAO();
        billDAO.insertBill(bill);

        JOptionPane.showMessageDialog(this, "Order placed successfully!");
    }

    /**
     * Updates the quantity combo box based on the selected product.
     */
    private void updateQuantityComboBox() {
        String selectedProduct = (String) productComboBox.getSelectedItem();
        if (selectedProduct != null) {
            ProductDAO productDAO = new ProductDAO();
            Product product = productDAO.findByName(selectedProduct);

            if (product != null) {
                quantityComboBox.removeAllItems();
                for (int i = 1; i <= product.getQuantity(); i++) {
                    quantityComboBox.addItem(i);
                }
            }
        }
    }

    /**
     * Updates the price field based on the selected product and quantity.
     */
    private void updatePriceField() {
        String selectedProduct = (String) productComboBox.getSelectedItem();
        if (selectedProduct != null) {
            ProductDAO productDAO = new ProductDAO();
            Product product = productDAO.findByName(selectedProduct);

            if (product != null) {
                Double productPrice = product.getPrice();
                Integer quantity = (Integer) quantityComboBox.getSelectedItem();
                if (quantity != null) {
                    priceTextField.setText(String.valueOf(productPrice * quantity));
                }
            }
        }
    }

    /**
     * Populates the product combo box with available products.
     */
    private void populateProductComboBox() {
        ProductDAO productDAO = new ProductDAO();
        List<Product> products = productDAO.findAll();

        if (products != null) {
            for (Product product : products) {
                productComboBox.addItem(product.getName());
            }
        } else {
            System.out.println("No products found.");
        }
    }

}
