package GUI;

import BusinessLogic.SelectionPolicy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimulationFrame extends JFrame{
    protected JPanel simulationF;
    protected JSpinner noOfQ;
    protected JSpinner simulationInterval;
    protected JSpinner noOfClients;
    protected JSpinner minArrivalTime;
    protected JSpinner maxArrivalTime;
    protected JSpinner minServiceTime;
    protected JSpinner maxServiceTime;
    protected JButton startButton;
    protected JButton clearButton;
    protected JButton validateButton;
    protected JComboBox comboBox1;

    public SelectionPolicy getComboBox1() {
        return (SelectionPolicy) comboBox1.getSelectedItem();
    }

    public JButton getStartButton() {
        return startButton;
    }

    public SimulationFrame() {
        this.setTitle("Simulation");
        this.setContentPane(this.simulationF);
        this.setMinimumSize(new Dimension(500, 500));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        startButton.setVisible(false);
        comboBox1.addItem(SelectionPolicy.SHORTEST_QUEUE);
        comboBox1.addItem(SelectionPolicy.SHORTEST_TIME);
        validateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(validateData()){
                    startButton.setVisible(true);
                }
                else {
                    JOptionPane.showMessageDialog(simulationF, "Invalid data", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               minServiceTime.setValue(0);
               maxServiceTime.setValue(0);
               minArrivalTime.setValue(0);
               maxArrivalTime.setValue(0);
               noOfClients.setValue(0);
               noOfQ.setValue(0);
            }
        });

        simulationF.setVisible(true);
    }

    private boolean validateData(){
        if (((Integer)minArrivalTime.getValue() > (Integer)maxArrivalTime.getValue()) || minServiceTime.getValue().equals(0) || maxServiceTime.getValue().equals(0)) {
            return false;
        } else if (((Integer)minServiceTime.getValue() > (Integer)maxServiceTime.getValue()) || minArrivalTime.getValue().equals(0) || maxArrivalTime.getValue().equals(0)) {
            return  false;
        } else if (noOfClients.getValue().equals(0) || noOfQ.getValue().equals(0) || simulationInterval.getValue().equals(0)) {
            return false;
        }

        return true;
    }

    public int getNoOfQ() {
        return (Integer)noOfQ.getValue();
    }

    public int getSimulationInterval() {
        return (Integer)simulationInterval.getValue();
    }

    public int getNoOfClients() {
        return (Integer)noOfClients.getValue();
    }

    public int getMinArrivalTime() {
        return (Integer)minArrivalTime.getValue();
    }

    public int getMaxArrivalTime() {
        return (Integer)maxArrivalTime.getValue();
    }

    public int getMinServiceTime() {
        return (Integer)minServiceTime.getValue();
    }

    public int getMaxServiceTime() {
        return (Integer)maxServiceTime.getValue();
    }

}
