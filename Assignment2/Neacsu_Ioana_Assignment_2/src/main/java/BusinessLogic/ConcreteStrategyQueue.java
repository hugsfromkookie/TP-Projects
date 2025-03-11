package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.List;

public class ConcreteStrategyQueue implements Strategy{
    @Override
    public synchronized Server addTask(List<Server> servers, Task t) {
        Server shortestQueueServer = servers.getFirst();
        for (Server server : servers) {
            if (server.getTasks().size() < shortestQueueServer.getTasks().size()) {
                shortestQueueServer = server;
            }
        }
        shortestQueueServer.addTask(t);
        return shortestQueueServer;
    }
}
