package BusinessLogic;

import GUI.SimulationDisplay;
import GUI.SimulationFrame;
import Model.Server;
import Model.Task;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class SimulationManager implements Runnable {
    //data read from UI
    public int timeLimit; //maximum processing time - read from UI
    public int maxProcessingTime;
    public int minProcessingTime;
    public int numberOfServers;
    public int numberOfClients;
    public int minArrivalTime;
    public int maxArrivalTime;
    //entity responsible with queue management and client distribution
    private Scheduler scheduler;
    //frame for displaying simulation
    private SimulationDisplay display;
    private SimulationFrame frame;
    //pool of tasks (client shopping in the store)
    private List<Task> generatedTasks;
    private Strategy strategy;
    private FileWriter fileWriter;
    private int currentTime = 0;

    public SimulationManager() {
        frame = new SimulationFrame();
        frame.setVisible(true);
        frame.getStartButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                readUIData();
                generatedTasks = generateNRandomTasks();
                try {
                    fileWriter = new FileWriter("log.txt");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                scheduler = new Scheduler(numberOfServers, numberOfClients, strategy);
                display = new SimulationDisplay(numberOfServers, numberOfClients);
                display.setSize(1024, 1024);
                display.setVisible(true);
                frame.getStartButton().setVisible(false);
                Thread t = new Thread(SimulationManager.this);
                t.start();

            }
        });
    }

    private void readUIData() {
        timeLimit = frame.getSimulationInterval();
        minProcessingTime = frame.getMinServiceTime();
        maxProcessingTime = frame.getMaxServiceTime();
        numberOfClients = frame.getNoOfClients();
        numberOfServers = frame.getNoOfQ();
        minArrivalTime = frame.getMinArrivalTime();
        maxArrivalTime = frame.getMaxArrivalTime();
        selectStrategy();
    }

    public void selectStrategy(){
        if(frame.getComboBox1() == SelectionPolicy.SHORTEST_QUEUE){
            strategy = new ConcreteStrategyQueue();
        }
        if(frame.getComboBox1() == SelectionPolicy.SHORTEST_TIME){
            strategy = new ConcreteStrategyTime();
        }
    }

    private List<Task> generateNRandomTasks() {
        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < numberOfClients; i++) {
            int processingTime = (int) (Math.random() * (maxProcessingTime - minProcessingTime + 1)) + minProcessingTime;
            int arrivalTime = (int) (Math.random() * (maxArrivalTime - minArrivalTime + 1)) + minArrivalTime;
            tasks.add(new Task(i + 1, arrivalTime, processingTime));
        }
        tasks.sort(Comparator.comparingInt(Task::getArrivalTime));
        return tasks;
    }

    @Override
    public synchronized void run() {
        currentTime = 0;
        while (currentTime < timeLimit) {
            dispatchTask();
            updateServer();
            writeLog();
            currentTime++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private synchronized void updateServer() {
        List<Queue<Task>> serverQueues = new ArrayList<>();
        for (Server server : scheduler.getServers()) {
            serverQueues.add(server.getTasks());
        }
        display.updateServerQueues(serverQueues);
    }


    private synchronized void writeLog() {
        try {
            fileWriter.write("Time: " + currentTime + "\n");
            fileWriter.write("Waiting clients: ");
            for (int i = 0; i < generatedTasks.size(); i++) {
                Task task = generatedTasks.get(i);
                fileWriter.write(task.toString());
            }
            fileWriter.write("\n");
            List<Server> servers = scheduler.getServers();
            for (int i = 0; i < servers.size() - 1; i++) {
                Server server = servers.get(i);
                fileWriter.write(server.toString() + "\n");
            }
            if (!servers.isEmpty()) {
                Server lastServer = servers.getLast();
                fileWriter.write(lastServer.toString() + "\n");
            }
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private synchronized void dispatchTask() {
        List<Task> toBeRemoved = new ArrayList<>();
        synchronized (generatedTasks) {
            Iterator<Task> iterator = generatedTasks.iterator();
            while (iterator.hasNext()) {
                Task task = iterator.next();
                if(task.getArrivalTime() == currentTime) {
                    scheduler.dispatchTask(task);
                    toBeRemoved.add(task);
                }
            }
            generatedTasks.removeAll(toBeRemoved);
        }
    }

    public static void main(String[] args) {
        SimulationManager simManager = new SimulationManager();
        Thread t = new Thread(simManager);
        t.start();
    }
}