package Presetation;

import BusinessLogic.TableGenerator;
import DataAccess.ProductDAO;
import Model.Product;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


/**
 * Represents the graphical user interface for product operations.
 */
public class ProductOp extends JFrame{
    private JButton addProductButton;
    private JPanel productOpPanel;
    private JButton deleteProductButton;
    private JComboBox<Product> deleteComboBox;
    private JButton viewProductsButton;
    private JButton exitButton;
    private JButton editProductButton;
    private JComboBox editComboBox;

    /**
     * Constructs a new ProductOp frame.
     *
     * @param parent The parent frame.
     */
    public ProductOp(JFrame parent) {
        setTitle("Product Operations");
        setContentPane(productOpPanel);
        setMinimumSize(new Dimension(500, 500));
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        populateComboBox();

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.setVisible(true);
                dispose();
            }
        });

        deleteProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedProduct();
            }
        });

        viewProductsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewProducts();
            }
        });

        addProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openProductInfo(false);
            }
        });
        editProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openProductInfo(true);
            }
        });

        setVisible(true);
    }

    /**
     * Opens the product information frame for adding or editing a product.
     *
     * @param isEdit Flag indicating whether editing an existing product.
     */
    private void openProductInfo(boolean isEdit) {
        this.setVisible(false);
        if (isEdit) {
            Product p = (Product) editComboBox.getSelectedItem();
            ProductInfo productInfo = new ProductInfo(this, isEdit, p);
            productInfo.setVisible(true);
            dispose();
        } else {
            ProductInfo productInfo = new ProductInfo(this, isEdit, null);
            productInfo.setVisible(true);
            dispose();
        }
    }

    /**
     * Populates the delete and edit combo boxes with product information.
     */
    private void populateComboBox() {
        ProductDAO productDAO = new ProductDAO();
        List<Product> products = productDAO.findAll();

        if (products != null) {
            for (Product p : products) {
                deleteComboBox.addItem(p);
                editComboBox.addItem(p);
            }
        } else {
            System.out.println("No products found.");
        }
    }

    /**
     * Deletes the selected product from the database.
     */
    private void deleteSelectedProduct() {
        Product selectedProduct = (Product) deleteComboBox.getSelectedItem();
        if (selectedProduct != null) {
            ProductDAO productDAO = new ProductDAO();
            boolean success = productDAO.delete(selectedProduct.getId());
            if (success) {
                JOptionPane.showMessageDialog(this, "Product deleted successfully.");
                deleteComboBox.removeItem(selectedProduct);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete product.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "No product selected.");
        }
    }

    /**
     * Displays a table showing all products in the database.
     */
    private void viewProducts() {
        ProductDAO productDAO = new ProductDAO();
        TableGenerator<Product> tableGenerator = new TableGenerator<>(productDAO, Product.class);
        JTable clientTable = tableGenerator.generateTable();
        JScrollPane scrollPane = new JScrollPane(clientTable);
        JFrame tableFrame = new JFrame("View Products");
        tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        tableFrame.add(scrollPane);
        tableFrame.setSize(500, 500);
        tableFrame.setVisible(true);
    }

}
