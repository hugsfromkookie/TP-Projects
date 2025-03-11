package Presetation;

import DataAccess.ClientDAO;
import Model.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Represents the graphical user interface for managing client information.
 */
public class ClientInfo extends JFrame{
    private JTextField usernameTextField;
    private JTextField emailTextField;
    private JTextField addressTextField;
    private JPasswordField passwordField1;
    private JButton confirmButton;
    private JButton cancelButton;
    private JPanel clientInfoPanel;

    /**
     * Constructs a new ClientInfo frame.
     *
     * @param parent The parent frame.
     * @param isEdit Flag indicating whether the information is being edited.
     * @param c      The client object containing information to be displayed or edited.
     */
    public ClientInfo(JFrame parent, boolean isEdit, Client c) {
        setTitle("Client");
        setContentPane(clientInfoPanel);
        setMinimumSize(new Dimension(500, 500));
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        if (isEdit) {
            this.usernameTextField.setText(c.getName());
            this.emailTextField.setText(c.getEmail());
            this.addressTextField.setText(c.getAddress());
            this.passwordField1.setText(c.getPassword());
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
                    ClientDAO clientDAO = new ClientDAO();

                    c.setName(usernameTextField.getText());
                    c.setAddress(addressTextField.getText());
                    c.setEmail(emailTextField.getText());
                    char[] passwordChars = passwordField1.getPassword();
                    String password = new String(passwordChars);
                    c.setPassword(password);

                    clientDAO.update(c);

                    JOptionPane.showMessageDialog(ClientInfo.this, "Client updated successfully.");
                    parent.setVisible(true);
                    dispose();
                } else {
                    ClientDAO clientDAO = new ClientDAO();
                    char[] passwordChars = passwordField1.getPassword();
                    String password = new String(passwordChars);
                    Client client = new Client(usernameTextField.getText(), emailTextField.getText(), password, addressTextField.getText());

                    clientDAO.insert(client);

                    JOptionPane.showMessageDialog(ClientInfo.this, "Client added successfully.");
                    parent.setVisible(true);
                    dispose();
                }
            }
        });

        setVisible(true);
    }
}
