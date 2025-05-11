import java.util.*;
import java.io.*;

public class Main {
    public static void main(String args[]) {
        ProcessManager manager = new ProcessManager(5);
        MemoryManager memManager = new MemoryManager();
        boolean running = false; // boolean to check if the program has started and is ready to accept commands
        Scanner userInput = new Scanner(System.in); // User input
        String command = null; // initializes command as empty
        // String[] commandLine; //commandLine is initialized outside conditional
        String[] param = new String[0]; // param initialized outside so it can be used within switches

        System.out.print("Welcome to MiniOS!\n");
        System.out.print("MiniOS> "); // Prints out the prompt
        running = true; // sets started to true so the program can accept commands
        /*
         * This code could be done a little better, should work as is but I'm wasting
         * memory
         * Since I never remove index 0 out of the commandLine array, it still exists
         * within the commandLine array
         * 
         */
        while (running) {
            command = userInput.nextLine(); // reads the command line
            if (command.contains(" ")) { // checks if a space exists for commands with parameters
                String[] commandLine = command.split(" "); // splits the commandline into an array
                param = new String[commandLine.length - 1]; // param is resized to fit the amount of params
                command = commandLine[0]; // takes the initial command
                try {
                    System.arraycopy(commandLine, 1, param, 0, commandLine.length - 1);
                } catch (Exception e) {
                    System.out.println("An error has occured");
                }
                //System.out.println(Arrays.toString(param)); testing
            }
            switch (command.toLowerCase()) {
                case "create":
                    String usage = "Usage: create <process_name>";
                    
                    if (param.length == 1) {
                        //if -u show usage
                        if(param[0].equals("-u")){
                            System.out.println(usage);
                            break;
                        }
                        // Create a new process
                        manager.createProcess(param[0]);
                    } else {
                        System.out.println("Process name missing. "+usage);
                        break;
                    }
                    break;
                case "ps":
                    manager.listProcesses();
                    break;
                case "schedule":
                    manager.schedule();
                    break;
                case "alloc":
                    if (param.length == 2 && Integer.parseInt(param[1]) > 0) { // Input washing needed
                        // if pid input doesn't exist as a PCB, print error code
                        // Create a new process
                        memManager.allocate(Integer.parseInt(param[0]) + 1, Integer.parseInt(param[1]));
                    } else {
                        throw new RuntimeException("Allocation missing pid or no declared size");
                    }
                    break;
                case "mem":
                    memManager.printMemory();
                    break;
                case "exit":
                    running = false;
                    break;
                default:
                    System.out.println("Command \"" + command.toLowerCase() + "\" does not exist");
            }
            System.out.print("MiniOS> "); // Prints out the prompt
        }

        //TODO Not running / Exit code here
        System.exit(0);
    }
}
