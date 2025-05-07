import java.util.*;
import java.io.*;
public class Main {
    public static void main(String args[]){
        ProcessManager manager = new ProcessManager(5);
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
            for(int i = 0; i<commandLine.length; i++){
                param[i] = commandLine[i + 1]; //For loop allocates every param
            }
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
                break;
            case "alloc":
                break;
            case "mem":
                break;
            case "exit":
                break;
            default -> throw new IllegalStateException("Unexpected value: " + userInput);
        }
    }
}
