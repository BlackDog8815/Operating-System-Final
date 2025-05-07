import java.util.*;
public class ProcessManager {
    public int arrivalTime, burstTime, serviceTime, completionTime, responseTime;
    Process cpu = null;
    int timer = 0;
    ArrayList<Process> listOfProcesses;
    ArrayList<PCB> listOfPCB;
    ArrayList<Process> readyQueue;
    ArrayList<Process> endProcesses;
    int timeQuantum;
    int counter;
    int contextSwitch;
    public ProcessManager(int timeQuantum) {
        this.timeQuantum = timeQuantum;
        listOfProcesses = new ArrayList<>();
        listOfPCB = new ArrayList<>();

    }
    public void createProcess(String name){
        Process newProcess = new Process(arrivalTime, burstTime);
        PCB newPCB = new PCB(listOfProcesses.size() - 1, name, PCB.State.Blocked, false);
        listOfProcesses.add(newProcess);
        listOfPCB.add(newPCB);
    }
    public void listProcesses(){
        for(int i = 0; i<listOfPCB.size(); i++){
            System.out.println("PID " + listOfPCB.get(i).pid + listOfPCB.get(i).name + listOfPCB.get(i).state);
        }
    }
    public void schedule(){
        //Uses Round Robin scheduler
        while (!readyQueue.isEmpty() || !listOfProcesses.isEmpty() || cpu != null) {
            // add to readyQueue
            for (int i = 0; i < listOfProcesses.size(); i++) {
                if (listOfProcesses.get(i).arrivalTime == timer) {
                    readyQueue.add(listOfProcesses.remove(i));
                    listOfPCB.get(i).state = PCB.State.Ready;
                }
            }

            // add to cpu
            if (cpu == null) {
                cpu = readyQueue.remove(0);
                listOfPCB
                //Running
            }

            while (cpu != null) {
                
                if (cpu.serviceTime == 0) {
                    cpu.responseTime = timer - cpu.arrivalTime;
                }
                counter++;
                cpu.serviceTime++;
                if (cpu.serviceTime == cpu.burstTime) {
                    cpu.completionTime = timer;
                    endProcesses.add(cpu);
                    cpu = null;
                    contextSwitch++;
                    counter = 0;
                } else if (counter == timeQuantum) {
                    readyQueue.add(cpu);
                    cpu = null;
                    contextSwitch++;
                    counter = 0;
                }
            }
        }
    }
}
