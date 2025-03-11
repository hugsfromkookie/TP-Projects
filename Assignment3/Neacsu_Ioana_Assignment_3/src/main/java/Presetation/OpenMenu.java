package Presetation;

import DataAccess.ClientDAO;
import Model.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Represents the main menu for accessing different functionalities.
 */
public class OpenMenu extends JFrame {
    private JPanel openMenuPanel;
    private JButton makeOrderButton;
    private JButton operationsButton;
    private JComboBox clientComboBox;

    /**
     * Constructs a new OpenMenu frame.
     */
    public OpenMenu() {
        setTitle("Menu");
        setContentPane(openMenuPanel);
        setMinimumSize(new Dimension(500, 500));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        populateComboBox();

        operationsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAdminPanel();
            }
        });

        makeOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openClientGUIPanel();
            }
        });

        setVisible(true);
    }

    /**
     * Opens the client graphical user interface panel for making orders.
     */
    private void openClientGUIPanel() {
        if (clientComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select a client before proceeding.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ClientDAO clientDAO = new ClientDAO();
        Client client = clientDAO.findByName((String) clientComboBox.getSelectedItem());

        if (client != null) {
            ClientGUI clientGUI = new ClientGUI(this, client.getId());
            this.setVisible(false);
            clientGUI.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Selected client not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Opens the administrative panel.
     */
    private void openAdminPanel() {
        Admin admin = new Admin(this);
        this.setVisible(false);
        admin.setVisible(true);
    }

    /**
     * Populates the client combo box with client names.
     */
    private void populateComboBox() {
        ClientDAO clientDAO = new ClientDAO();
        List<Client> clients = clientDAO.findAll();

        if (clients != null) {
            for (Client client : clients) {
                clientComboBox.addItem(client.getName());
            }
        } else {
            System.out.println("No clients found.");
        }
    }

}
