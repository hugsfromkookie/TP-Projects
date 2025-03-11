package Presetation;

import BusinessLogic.TableGenerator;
import DataAccess.ClientDAO;
import DataAccess.ProductDAO;
import Model.Client;
import Model.Product;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Represents the graphical user interface for client operations.
 */
public class ClientOp extends JFrame{
    private JPanel clientOPPanel;
    private JButton addClientButton;
    private JButton deleteClientButton;
    private JButton viewClientsButton;
    private JComboBox<Client> deleteComboBox;
    private JButton exitButton;
    private JButton editClientButton;
    private JComboBox editComboBox;

    /**
     * Constructs a new ClientOp frame.
     *
     * @param parent The parent frame.
     */
    public ClientOp(JFrame parent) {
        setTitle("Client Operations");
        setContentPane(clientOPPanel);
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

        deleteClientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedClient();
            }
        });

        viewClientsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewClients();
            }
        });

        addClientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openClientInfo(false);
            }
        });
        editClientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openClientInfo(true);
            }
        });

        setVisible(true);
    }

    /**
     * Opens the client information frame for adding or editing a client.
     *
     * @param isEdit Flag indicating whether editing an existing client.
     */
    private void openClientInfo(boolean isEdit) {
        this.setVisible(false);
        if (isEdit) {
            Client c = (Client) editComboBox.getSelectedItem();
            ClientInfo clientInfo = new ClientInfo(this, isEdit, c);
            clientInfo.setVisible(true);
            dispose();
        } else {
            ClientInfo clientInfo = new ClientInfo(this, isEdit, null);
            clientInfo.setVisible(true);
            dispose();
        }
    }

    /**
     * Populates the delete and edit combo boxes with client information.
     */
    private void populateComboBox() {
        ClientDAO clientDAO = new ClientDAO();
        List<Client> clients = clientDAO.findAll();

        if (clients != null) {
            for (Client client : clients) {
                deleteComboBox.addItem(client);
                editComboBox.addItem(client);
            }
        } else {
            System.out.println("No clients found.");
        }
    }

    /**
     * Deletes the selected client from the database.
     */
    private void deleteSelectedClient() {
        Client selectedClient = (Client) deleteComboBox.getSelectedItem();
        if (selectedClient != null) {
            ClientDAO clientDAO = new ClientDAO();
            boolean success = clientDAO.delete(selectedClient.getId());
            if (success) {
                JOptionPane.showMessageDialog(this, "Client deleted successfully.");
                deleteComboBox.removeItem(selectedClient);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete client.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "No client selected.");
        }
    }

    /**
     * Displays a table showing all clients in the database.
     */
    private void viewClients() {
        ClientDAO clientDAO = new ClientDAO();
        TableGenerator<Client> tableGenerator = new TableGenerator<>(clientDAO, Client.class);
        JTable clientTable = tableGenerator.generateTable();
        JScrollPane scrollPane = new JScrollPane(clientTable);
        JFrame tableFrame = new JFrame("View Clients");
        tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        tableFrame.add(scrollPane);
        tableFrame.setSize(500, 500);
        tableFrame.setVisible(true);
    }
}
