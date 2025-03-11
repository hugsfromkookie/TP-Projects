package Presetation;

import DataAccess.ProductDAO;
import Model.Product;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Represents the graphical user interface for managing product information.
 */
public class ProductInfo extends JFrame{
    private JTextField nameTextField;
    private JTextField quantityTextField;
    private JTextField priceTextField;
    private JButton confirmButton;
    private JButton cancelButton;
    private JPanel productInfoPanel;

    /**
     * Constructs a new ProductInfo frame.
     *
     * @param parent The parent frame.
     * @param isEdit Flag indicating whether the information is being edited.
     * @param p      The product object containing information to be displayed or edited.
     */
    public ProductInfo(JFrame parent, boolean isEdit, Product p) {
        setTitle("Product");
        setContentPane(productInfoPanel);
        setMinimumSize(new Dimension(500, 500));
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        if (isEdit) {
            this.nameTextField.setText(p.getName());
            this.quantityTextField.setText(String.valueOf(p.getQuantity()));
            this.priceTextField.setText(String.valueOf(p.getPrice()));
        }

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.setVisible(true);
                dispose();
            }
        });

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isEdit) {
                    ProductDAO productDAO = new ProductDAO();

                    p.setName(nameTextField.getText());
                    p.setQuantity(Integer.parseInt(quantityTextField.getText()));
                    p.setPrice(Double.parseDouble(priceTextField.getText()));

                    productDAO.update(p);

                    JOptionPane.showMessageDialog(ProductInfo.this, "Product updated successfully.");
                    parent.setVisible(true);
                    dispose();
                } else {
                    ProductDAO productDAO = new ProductDAO();
                    Product product = new Product(nameTextField.getText(), Integer.parseInt(quantityTextField.getText()), Double.parseDouble(priceTextField.getText()));

                    productDAO.insert(product);

                    JOptionPane.showMessageDialog(ProductInfo.this, "Product added successfully.");
                    parent.setVisible(true);
                    dispose();
                }
            }
        });

        setVisible(true);
    }

}
