import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class Main {
    private static boolean running = false;
    private static String title = "MiniOS";
    private static String version = "v0.1";
    private static String author = "";
    private static String longTitle = "MiniOS " + version + " by " + author;
    public static void main(String args[]) {
        ProcessManager procManager = new ProcessManager();
        MemoryManager memManager = new MemoryManager();
        new GUI(title, longTitle, procManager, memManager);
        running = true;
        while (running) {
            // Dont Exit
        }
        System.exit(0);
    }

    public static void run(String command, ProcessManager manager, MemoryManager memManager) {
        
        //String[] param = new String[0]; // param initialized outside so it can be used within switches
        running = true; 

                if (command.equals("")  || command == null|| command.equals(" ")) { // checks if the command is empty or null
            command = "help"; // if no command is given, show help
        }
        String[] param = new String[0];
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
            case "free":
                String usageFree = "Usage: free <pid>";
                if (param.length == 1) {
                    // if -u show usage
                    if (param[0].equals("-u")) {
                        System.out.println(usageFree);
                        break;
                    }
                }
                else{
                    System.out.println("Too many parameters");
                    break;
                }
            case "alloc":
                String usageAlloc = "Usage: alloc <pid> <size>";
                if (param.length == 1) {
                    // if -u show usage
                    if (param[0].equals("-u")) {
                        System.out.println(usageAlloc);
                        break;
                    }
                }
                else if (param.length == 2 && Integer.parseInt(param[1]) > 0) {
                    for (int i = 0; i < manager.listOfPCB.size(); i++) {
                        if (Integer.parseInt(param[0]) == manager.listOfPCB.get(i).getPid()) { //checks input pid to see if it exists as a process
                            int memPID = manager.listOfPCB.get(i).getPid();
                            int size = Integer.parseInt(param[1]);
                            if(memManager.pidList.isEmpty()){
                                memManager.pidList.add(memPID);
                                memManager.allocate(memPID + 1, size);
                                break;
                            }
                            for(int j = 0; j<memManager.pidList.size(); j++){
                                if(memPID == memManager.pidList.get(j)){
                                    memManager.free(memPID + 1);
                                    memManager.allocate(memPID + 1, size);
                                    break;
                                }
                                else{
                                    memManager.pidList.add(memPID);
                                    memManager.allocate(memPID + 1, size);
                                }
                            }
                        }
                        else{
                            System.out.println("Invalid PID");
                        }
                    }
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
                System.out.println("\nUsage: <command> [<args>]");
                break;
            case "help":
                System.out.println("\nAvailable commands:");
                System.out.println("create <process_name> - Create a new process");
                System.out.println("ps - List all processes");
                System.out.println("schedule - Schedule the processes");
                System.out.println("alloc <pid> <size> - Allocate memory for a process");
                System.out.println("mem - Show memory allocation");
                System.out.println("exit - Exit the program");
                System.out.println("-u - Show usage information");
                System.out.println("help - Show this help message");
                break;
                
            case "clear":
                // clear the GUI console
                GUI.clearConsole(title);
                break;
            default:
                System.out.println("Command \"" + command.toLowerCase() + "\" does not exist");
        }
        System.out.print(title+"> ");
    }
    static class TriangleButton extends JButton {
        public TriangleButton() {
            setContentAreaFilled(false);
            setFocusPainted(false);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Paint background
            if (getModel().isPressed()) {
                g2.setColor(new Color(35, 35, 35));
            } else if (getModel().isRollover()) {
                g2.setColor(new Color(55, 55, 55));
            } else {
                g2.setColor(getBackground());
            }
            g2.fillRect(0, 0, getWidth(), getHeight());
            
            // Paint triangle (pointing right)
            g2.setColor(getForeground());
            int[] xPoints = {getWidth()/2 - 5, getWidth()/2 + 5, getWidth()/2 - 5};
            int[] yPoints = {getHeight()/2 - 5, getHeight()/2, getHeight()/2 + 5};
            g2.fillPolygon(xPoints, yPoints, 3);
            
            g2.dispose();
        }
    }
    
    // Custom slim rounded scrollbar UI
    static class SlimRoundedScrollBarUI extends BasicScrollBarUI {
        private final int THUMB_SIZE = 8;
        private final int THUMB_RADIUS = 4;
        
        @Override
        protected void configureScrollBarColors() {
            this.thumbColor = new Color(80, 80, 80);
            this.trackColor = new Color(35, 35, 35);
        }
        
        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }
        
        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }
        
        private JButton createZeroButton() {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
            button.setMinimumSize(new Dimension(0, 0));
            button.setMaximumSize(new Dimension(0, 0));
            return button;
        }
        
        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
                return;
            }
            
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Set the thumb color
            Color thumbColor = isDragging ? new Color(100, 100, 100) : this.thumbColor;
            g2.setColor(thumbColor);
            
            // Create a rounded rectangle for the thumb
            if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
                // For vertical scrollbar
                int width = THUMB_SIZE;
                int x = thumbBounds.x + (thumbBounds.width - width) / 2;
                RoundRectangle2D roundRect = new RoundRectangle2D.Float(
                    x, thumbBounds.y, width, thumbBounds.height, THUMB_RADIUS, THUMB_RADIUS
                );
                g2.fill(roundRect);
            } else {
                // For horizontal scrollbar
                int height = THUMB_SIZE;
                int y = thumbBounds.y + (thumbBounds.height - height) / 2;
                RoundRectangle2D roundRect = new RoundRectangle2D.Float(
                    thumbBounds.x, y, thumbBounds.width, height, THUMB_RADIUS, THUMB_RADIUS
                );
                g2.fill(roundRect);
            }
            
            g2.dispose();
        }
        
        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(trackColor);
            g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
            g2.dispose();
        }
    }

}