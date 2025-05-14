import java.util.*;
public class ProcessManager {
    ArrayList<PCB> listOfPCB;
    MemoryManager memoryManager;
    private final Random random;
    private final int minTime;
    private final int maxTime;
    private int nextPID;
    private final int timeQuantum;
    public ProcessManager() {
        listOfPCB = new ArrayList<>();
        this.nextPID = 0;
        this.minTime = 1000;
        this.timeQuantum = 1000;
        this.random = new Random();
        this.maxTime = 5000;
    }
    public void createProcess(String name){
        int pid = nextPID++;
        PCB process = new PCB(pid, name);
        listOfPCB.add(process);
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
            if (process.isActive() && process.getState() == PCB.State.Ready) {
                readyProcesses.add(process);
            }
        }

        if (readyProcesses.isEmpty()) {
            System.out.println("No ready processes.");
            return;
        }

        List<Integer> remainingTimes = new ArrayList<>();

        for (int i = 0; i < readyProcesses.size(); i++) {
            int totalRunTime = minTime + random.nextInt(maxTime - minTime);
            remainingTimes.add(totalRunTime);
            //PCB process = readyProcesses.get(i);
            //System.out.println(totalRunTime);
            //System.out.println(process.getPid() + " " + process.getName() + " assigned run time of " + totalRunTime + " milliseconds");
        }

        boolean allDone = false;

        while (!allDone) {
            allDone = true;

            for (int i = 0; i < readyProcesses.size(); i++) {
                PCB process = readyProcesses.get(i);
                int timeLeft = remainingTimes.get(i);

                if (timeLeft <= 0) {continue;}

                allDone = false;

                try {
                    process.setState(PCB.State.Running);
                    System.out.println("Running: " + process.getName());
                    //System.out.println("Process " + process.getPid() + " " + process.getName() + " is running for " + Math.min(timeQuantum, timeLeft) + " milliseconds");


                    Thread.sleep(Math.min(timeQuantum, timeLeft));

                    timeLeft -= timeQuantum;
                    if (timeLeft <= 0) {
                        System.out.println("Process " + process.getPid() + " " + process.getName() + " has completed");
                        remainingTimes.set(i, 0);
                        memoryManager.free(process.getPid());
                    } else {
                        remainingTimes.set(i, timeLeft);
                        //System.out.println("Process " + process.getPid() + " has " + timeLeft + " milliseconds left");
                    }

                    process.setState(PCB.State.Ready);
                    //System.out.println("Process " + process.getPid() + " yielding CPU");
                    //System.out.println("---");

                } catch (InterruptedException e) {
                    System.err.println("Process execution was interrupted: " + e.getMessage());
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
