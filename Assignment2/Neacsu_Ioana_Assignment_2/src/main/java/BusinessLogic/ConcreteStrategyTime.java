package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.List;

public class ConcreteStrategyTime implements Strategy {

    @Override
    public synchronized Server addTask(List<Server> servers, Task t) {
        Server shortestServer = servers.getFirst();
        for (Server server : servers) {
            if (server.getWaitingPeriod().get() < shortestServer.getWaitingPeriod().get()) {
                shortestServer = server;
            }
        }
        shortestServer.addTask(t);
        return shortestServer;
    }
}
