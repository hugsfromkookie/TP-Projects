package Model;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;


public class Server implements Runnable {
    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;
    private int ID;

    public Server(int ID) {
        this.ID = ID;
        this.waitingPeriod = new AtomicInteger(0);
        this.tasks = new LinkedBlockingQueue<>();
    }

    public void addTask(Task newTask) {
        try{
            tasks.put(newTask);
            waitingPeriod.addAndGet(newTask.getServiceTime());
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            try {
                Task nextTask = tasks.take();
                while(nextTask.getServiceTime() > 0) {
                    Thread.sleep(1000);
                    nextTask.decreaseServiceTime();
                    waitingPeriod.decrementAndGet();
                }
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public BlockingQueue<Task> getTasks() {
        return tasks;
    }

    public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
    }

    public int getID() {
        return ID;
    }

    public int getTasksAmount() {
        return tasks.size();
    }

    @Override
    public String toString() {
        StringBuilder message = new StringBuilder("Queue " + (this.getID() + 1)+ ": ");
        Set<Task> printedTasks = new HashSet<>();

        if (this.getTasksAmount() > 0) {
            for (Task task : tasks) {
                if (task.getServiceTime() != 0 && !printedTasks.contains(task)) {
                    message.append(task.toString());
                    printedTasks.add(task);
                }
            }
        } else {
            message.append("closed");
        }

        return message.toString();
    }


    public boolean isClosed() {
        return tasks.isEmpty();
    }
}

