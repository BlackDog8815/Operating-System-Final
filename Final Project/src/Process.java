public class Process {
    public int arrivalTime, serviceTime, burstTime, completionTime, responseTime;

    public Process(int arrivalTime, int burstTime) {
        //this.pid = pid; // unique
        this.arrivalTime = arrivalTime;
        this.responseTime = -1;
        this.burstTime = burstTime;
        this.serviceTime = 0;
    }

}
