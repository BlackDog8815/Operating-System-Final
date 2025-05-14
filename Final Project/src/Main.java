import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.event.*;

public class Main {
    private static boolean running = false;
    public static void main(String args[]) {
        ProcessManager manager = new ProcessManager();
        MemoryManager memManager = new MemoryManager();

        // 
        JFrame window = new JFrame("MiniOS");
        JButton sendCommand = new JButton("<");
        sendCommand.setBounds(520, 320, 50, 30);
        JTextField commandField = new JTextField(20);
        commandField.setBounds(10, 320, 500, 30);
        JTextArea commandOutput = new JTextArea(20, 50);
        commandOutput.setEditable(false);
        JScrollPane scroll = new JScrollPane(commandOutput);
        scroll.setBounds(10, 10, 560, 300);
        window.add(sendCommand);
        window.add(commandField);
        window.add(scroll);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(600, 400);
        window.setLayout(null);
        //window.setForeground(new java.awt.Color(60, 60, 60));
        //window.setBackground(new java.awt.Color(50, 50, 50));
        //window.setTitle(title);
        //window.setIconImage(new ImageIcon("src/icon.jpg").getImage());
        //window.setResizable(false);
        //window.setFont(new java.awt.Font("Monospaced", 0, 12));
        window.setVisible(true);

        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                commandOutput.append(String.valueOf((char) b));
                originalOut.write(b);
            }
        }));

        commandField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String command = commandField.getText();
                commandOutput.append(command + "\n");
                commandField.setText("");
                run(command, manager, memManager); // Call the run method with the command
            }
        });

        sendCommand.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String command = commandField.getText();
                commandOutput.append(command + "\n");
                commandField.setText("");
                run(command, manager, memManager); // Call the run method with the command
            }
        });

        commandField.requestFocusInWindow();

        //End GUI
        running = true;
        while (running) {
            // Dont Exit
        }
        System.exit(0);
    }

    public static void run(String command, ProcessManager manager, MemoryManager memManager) {
        
        String[] param = new String[0]; // param initialized outside so it can be used within switches
        System.out.print("Welcome to MiniOS!\n");
        System.out.print("MiniOS> ");
        running = true; 

        param = new String[0];
        if (command.contains(" ")) { // checks if a space exists for commands with parameters
            String[] commandLine = command.split(" "); // splits the commandline into an array
            param = new String[commandLine.length - 1]; // param is resized to fit the amount of params
            command = commandLine[0]; // takes the initial command
            try {
                System.arraycopy(commandLine, 1, param, 0, commandLine.length - 1);
            } catch (Exception e) {
                System.out.println("An error has occured"); // testing only, handle better
            }
            // System.out.println(Arrays.toString(param)); testing
        }

        if (command == "") {
            command = "help"; // if no command is given, show help
        }

        switch (command.toLowerCase()) {
            case "create":
                String usage = "Usage: create <process_name>";

                if (param.length == 1) {
                    // if -u show usage
                    if (param[0].equals("-u")) {
                        System.out.println(usage);
                        break;
                    }
                    // Create a new process
                    manager.createProcess(param[0]);

                } else if (param.length > 1) {
                    // if more than one parameter is given, show usage
                    System.out.println("Too Many Parameters. " + usage);
                    break;
                } else {
                    // if no parameter is given, show usage
                    System.out.println("Process name missing. " + usage);
                    break;
                }
                break;
            case "ps":
                String usagePs = "Usage: ps";
                if (param.length >= 1) {
                    // if -u show usage
                    if (param[0].equals("-u")) {
                        System.out.println(usagePs);
                        break;
                    }
                    System.out.println("No parameters expected. " + usagePs);
                    break;
                }
                manager.listProcesses();
                break;
            case "schedule":
                String usageSchedule = "Usage: schedule";
                if (param.length == 1) {
                    // if -u show usage
                    if (param[0].equals("-u")) {
                        System.out.println(usageSchedule);
                        break;
                    }
                    System.out.println("No parameters expected. " + usageSchedule);
                    break;
                }
                manager.schedule();
                break;
            case "alloc":
                String usageAlloc = "Usage: alloc <pid> <size>";
                if (param.length == 1) {
                    // if -u show usage
                    if (param[0].equals("-u")) {
                        System.out.println(usageAlloc);
                        break;
                    }
                } else if (param.length == 2 && Integer.parseInt(param[1]) > 0) {
                    memManager.allocate(Integer.parseInt(param[0]) + 1, Integer.parseInt(param[1]));
                } else {
                    System.out.println("Invalid parameters. " + usageAlloc);
                    break;
                }
                break;
            case "mem":
                String usageMem = "Usage: mem";
                if (param.length == 1) {
                    // if -u show usage
                    if (param[0].equals("-u")) {
                        System.out.println(usageMem);
                        break;
                    }
                    System.out.println("No parameters expected. " + usageMem);
                    break;
                }
                memManager.printMemory();
                break;
            case "exit":
                running = false;
                break;
            case "-u":
                System.out.println("Usage: <command> [<args>]");
                break;
            case "help":
                System.out.println("Available commands:");
                System.out.println("create <process_name> - Create a new process");
                System.out.println("ps - List all processes");
                System.out.println("schedule - Schedule the processes");
                System.out.println("alloc <pid> <size> - Allocate memory for a process");
                System.out.println("mem - Show memory allocation");
                System.out.println("exit - Exit the program");
                System.out.println("-u - Show usage information");
                System.out.println("help - Show this help message");

                break;
            default:
                System.out.println("Command \"" + command.toLowerCase() + "\" does not exist");
        }
        System.out.print("MiniOS> "); // Prints out the prompt
    }
}