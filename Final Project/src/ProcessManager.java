import java.util.*;
public class ProcessManager {
    public int arrivalTime, burstTime, serviceTime, completionTime, responseTime;
    PCB cpu = null;
    int timer = 0;
    //ArrayList<Process> listOfProcesses;
    ArrayList<PCB> listOfPCB;
    ArrayList<PCB> readyQueue;
    ArrayList<PCB> endProcesses;
    int timeQuantum;
    int counter;
    int contextSwitch;
    public ProcessManager(int timeQuantum) {
        this.timeQuantum = timeQuantum;
        //listOfProcesses = new ArrayList<>();
        listOfPCB = new ArrayList<>();

    }
    public void createProcess(String name){
        //Process newProcess = new Process(arrivalTime, burstTime);
        PCB newPCB = new PCB(listOfPCB.size(), name, PCB.State.Ready, false);
        //listOfProcesses.add(newProcess);
        listOfPCB.add(newPCB);
    }
    public void listProcesses(){
        for(int i = 0; i<listOfPCB.size(); i++){
            System.out.println("PID " + listOfPCB.get(i).pid + listOfPCB.get(i).name + listOfPCB.get(i).state);
        }
    }
    public void schedule(){
        //Uses Round Robin scheduler
        while (!readyQueue.isEmpty() || !listOfPCB.isEmpty() || cpu != null) {
            // add to readyQueue
            for (int i = 0; i < listOfPCB.size(); i++) {
                listOfPCB.get(i).state = PCB.State.Ready;
                readyQueue.add(listOfPCB.get(i));
                //if (listOfPCB.get(i).arrivalTime == timer) {
                //    readyQueue.add(listOfProcesses.remove(i));
                //    listOfPCB.get(i).state = PCB.State.Ready;
                }
            }

            // add to cpu
            if (cpu == null) {
                readyQueue.get(0).state = PCB.State.Running;
                cpu = readyQueue.remove(0);
                //Running
            }

            while (cpu != null) {
                //use semaphore acquire here?
                try{
                    Thread.sleep(5000);
                } catch (Exception e){
                    System.out.println("An error has occured");
                }
                //use semaphore release here?


                /*
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
                 */
        }
    }
}
