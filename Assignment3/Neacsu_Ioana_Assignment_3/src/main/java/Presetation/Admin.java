package Presetation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Represents the admin interface, providing functionality for managing clients and products.
 */
public class Admin extends JFrame {
    private JPanel adminPanel;
    private JButton clientOperationsButton;
    private JButton productOperationsButton;
    private JButton logOutButton;

    /**
     * Constructs a new Admin frame.
     *
     * @param parent The parent frame.
     */
    public Admin(JFrame parent) {
        setTitle("Admin");
        setContentPane(adminPanel);
        setMinimumSize(new Dimension(500, 500));
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        clientOperationsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openClientOP();
            }
        });

        productOperationsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openProductOP();
            }
        });

        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.setVisible(true);
                dispose();
            }
        });

        setVisible(true);
    }

    /**
     * Opens the client operations interface.
     */
    private void openClientOP() {
        this.setVisible(false);
        ClientOp clientOP = new ClientOp(this);
        clientOP.setVisible(true);
    }

    /**
     * Opens the product operations interface.
     */
    private void openProductOP() {
        this.setVisible(false);
        ProductOp productOp = new ProductOp(this);
        productOp.setVisible(true);
    }
}
