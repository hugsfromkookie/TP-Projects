package GUI;

import Model.Task;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class SimulationDisplay extends JFrame {
    private int noOfQ;
    private int noOfTasks;
    private List<Queue<Task>> serverQueues;

    public SimulationDisplay(int noOfQ, int noOfTasks) {
        this.noOfQ = noOfQ;
        this.noOfTasks = noOfTasks;
        this.serverQueues = new ArrayList<>();
        initUI();
    }

    private void initUI() {
        setTitle("Simulation Display");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        DrawingPanel drawingPanel = new DrawingPanel();
        add(drawingPanel);
        JScrollPane scrollPane = new JScrollPane(new DrawingPanel());
        add(scrollPane);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void updateServerQueues(List<Queue<Task>> serverQueues) {
        this.serverQueues = serverQueues;
        repaint();
    }

    private class DrawingPanel extends JPanel {
        private static final int LINE_GAP = 40;
        private static final int LINE_LENGTH = 950;
        private static final int LINE_START_X = 0;
        private static final int DOT_RADIUS = 4;

        public DrawingPanel() {
            setBackground(Color.PINK);
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            int y = 50;

            for (int i = 0; i < noOfQ; i++) {
                g2d.drawLine(LINE_START_X, y, LINE_START_X + LINE_LENGTH, y);

                g2d.drawString("Q" + (i + 1), LINE_START_X + LINE_LENGTH + 10, y + 5);

                if (serverQueues.size() > i && !serverQueues.get(i).isEmpty()) {
                    int x = LINE_START_X + LINE_LENGTH - 10;
                    Set<Task> printedTasks = new HashSet<>();

                    for (Task task : serverQueues.get(i)) {
                        if (task.getServiceTime() != 0 && !printedTasks.contains(task)) {
                            g2d.fillOval(x, y - DOT_RADIUS, DOT_RADIUS * 2, DOT_RADIUS * 2);
                            g2d.drawString(Integer.toString(task.getID()), x, y - 10);
                            printedTasks.add(task);
                            x -= 30;
                        }
                    }
                }

                y += LINE_GAP;
            }
        }


        @Override
        public Dimension getPreferredSize() {
            return new Dimension(1024, 1024);
        }
    }
}
