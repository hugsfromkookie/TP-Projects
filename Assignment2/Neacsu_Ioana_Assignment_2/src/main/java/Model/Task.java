package Model;

public class Task {
    private int ID;
    private int arrivalTime;
    private int serviceTime;

    public Task(int ID, int arrivalTime, int serviceTime) {
        this.ID = ID;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
    }

    public int getID() {
        return ID;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void decreaseServiceTime() {
        this.serviceTime--;
    }

    @Override
    public String toString() {
        return "(" + this.getID() + "," + this.getArrivalTime() + "," + this.getServiceTime()
                + ")" + ";";
    }
}
