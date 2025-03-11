package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Scheduler {
    private List<Server> servers;
    private int maxNoServers;
    private int maxTasksPerServer;
    private Strategy strategy;
    private BlockingQueue<Task> tasksQueue;
    private AtomicInteger waitingPeriod;

    public Scheduler(int maxNoServers, int maxTasksPerServer) {
        this.maxNoServers = maxNoServers;
        this.maxTasksPerServer = maxTasksPerServer;
        this.servers = new CopyOnWriteArrayList<>();
        for(int i = 0; i < maxNoServers; i++) {
            servers.add(new Server(i));
        }
        for (int i = 0; i < maxNoServers; i++) {
            Thread serverThread = new Thread(this.servers.get(i));
            serverThread.start();
        }
    }

    public Scheduler(int maxNoServers, int maxTasksPerServer, Strategy strategy) {
        this.maxNoServers = maxNoServers;
        this.maxTasksPerServer = maxTasksPerServer;
        this.servers = new CopyOnWriteArrayList<>();
        this.strategy = strategy;
        for(int i = 0; i < maxNoServers; i++) {
            servers.add(new Server(i));
        }
        for (int i = 0; i < maxNoServers; i++) {
            Thread serverThread = new Thread(this.servers.get(i));
            serverThread.start();
        }
    }

    public void dispatchTask(Task t) {
        synchronized (servers) {
            if(strategy != null){
                Server server = strategy.addTask(servers, t);
                if(server != null) {
                    if (server.getTasksAmount() < maxTasksPerServer) {
                        server.addTask(t);
                    }
                }
            }
        }
    }

    public List<Server> getServers() {
        return servers;
    }
}
