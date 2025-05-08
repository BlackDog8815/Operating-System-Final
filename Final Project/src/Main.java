import java.util.*;
import java.io.*;
public class Main {
    public static void main(String args[]){
        ProcessManager manager = new ProcessManager(5);
        MemoryManager memManager = new MemoryManager();
        System.out.print("Welcome to MiniOS!\n");
        Scanner userInput = new Scanner("MiniOS> "); //User input
        String command = userInput.toString(); //Transfers user input into a string
        //String[] commandLine; //commandLine is initialized outside conditional
        String[] param = new String[0]; //param initialized outside so it can be used within switches
        /*
        This code could be done a little better, should work as is but I'm wasting memory
        Since I never remove index 0 out of the commandLine array, it still exists within the commandLine array

         */
        if(command.contains(" ")){ //checks if a space exists for commands with parameters
            String[] commandLine = command.split(" "); //splits the commandline into an array
            param = new String[commandLine.length - 1]; //param is resized to fit the amount of params
            command = commandLine[0]; //takes the initial command
            System.arraycopy(commandLine, 1, param, 0, commandLine.length);
        }
        switch (command.toLowerCase()){
            case "create":
                if (param.length == 1){
                    //Create a new process
                    manager.createProcess(param[0]);
                }
                else{
                    throw new RuntimeException("Process has no name or an illegal name");
                }
                break;
            case "ps":
                manager.listProcesses();
                break;
            case "schedule":
                manager.schedule();
                break;
            case "alloc":
                if (param.length == 2 && Integer.parseInt(param[1]) > 0){ //Input washing needed
                    //if pid input doesn't exist as a PCB, print error code
                    //Create a new process
                    memManager.allocate(Integer.parseInt(param[0]) + 1, Integer.parseInt(param[1]));
                }
                else{
                    throw new RuntimeException("Allocation missing pid or no declared size");
                }
                break;
            case "mem":
                memManager.printMemory();
                break;
            case "exit":
                System.exit(0);
                break;
            default:
                System.out.println("Command does not exist");
        }
    }
}
