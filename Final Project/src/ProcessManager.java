import java.util.*;
public class ProcessManager {
    ArrayList<PCB> listOfPCB;
    private final Random randomTime;
    private final int minTime;
    private final int maxTime;
    private int nextPID;
    private final int timeQuantum;
    List<Integer> timeList = new ArrayList<>();
    public ProcessManager() {
        listOfPCB = new ArrayList<>();
        this.nextPID = 0;
        this.minTime = 1000;
        this.maxTime = 5000;
        this.timeQuantum = 1000;
        this.randomTime = new Random();
    }
    public void createProcess(String name){
        int pid = nextPID++;
        PCB process = new PCB(pid, name);
        listOfPCB.add(process);
        int burstTime = minTime + randomTime.nextInt(maxTime - minTime);
        timeList.add(burstTime);
        System.out.println("Created Process: " + process.getName() + " with PID " + process.getPid());
    }
    public void listProcesses(){
        for(int i = 0; i<listOfPCB.size(); i++){
            System.out.println("PID " + listOfPCB.get(i).pid + " " + listOfPCB.get(i).name + " " + listOfPCB.get(i).state);
        }
    }
    public void schedule() {
        List<PCB> readyProcesses = new ArrayList<>();
        for (PCB process : listOfPCB) {
            /*
            Checking if the process is active is technically useless for this code.
            Since we are just implementing semaphore but not actually using it, the PCB will always remain active.
            I left it because it's older code but if you were to take this project farther, you would need to check
            if the process is active or not.
             */
            if (process.isActive() && process.getState() == PCB.State.Ready) {
                readyProcesses.add(process); //processes that are ready and have been allocated
            }
        }

        if (readyProcesses.isEmpty()) {//Probably unnecessary but I'm gonna leave it.
            System.out.println("No ready processes.");
            return;
        }

        for (int i = 0; i < readyProcesses.size(); i++) {
            PCB process = readyProcesses.get(i);
            System.out.println("Process " + process.getPid() + " " + process.getName() + " assigned run time of " + timeList.get(i) + " milliseconds");
        }

        boolean readyQueue = false;

        while (!readyQueue) {
            readyQueue = true;
            /*
            Runs each process within the readyQueue at a time
            Once each process's burst time is 0, the scheduler stops
            Uses Round Robin scheduling, Time Quantum of 1000 millisecs or 1 second
             */
            for (int i = 0; i < readyProcesses.size(); i++) {
                PCB processList = readyProcesses.get(i);
                int timeLeft = timeList.get(i);

                if (timeLeft <= 0) {
                    continue;
                }
                readyQueue = false;

                try {
                    int runTime = Math.min(timeQuantum,timeLeft);
                    processList.setState(PCB.State.Running);
                    System.out.println("Running: " + processList.getName());
                    System.out.println("Process " + processList.getPid() + " " + processList.getName() + " is running for " + runTime + " milliseconds");

                    Thread.sleep(runTime);

                    timeLeft -= timeQuantum;
                    if (timeLeft <= 0) {
                        System.out.println("Process " + processList.getPid() + " " + processList.getName() + " has completed");
                        timeList.set(i, 0);
                    }
                    else {
                        timeList.set(i, timeLeft);
                        System.out.println("Process " + processList.getPid() + " has " + timeLeft + " milliseconds left");
                    }
                    processList.setState(PCB.State.Ready);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
