import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class GUI {
    private static javax.swing.JTextArea consoleOutput;

    public static void setConsoleOutput(javax.swing.JTextArea area) {
        consoleOutput = area;
    }

    public static void clearConsole(String title) {
        if (consoleOutput != null) {
            consoleOutput.setText("");
            consoleOutput.append("Welcome to " + title + "! Please type a command:\n");
        }
    }

    public GUI(String title, String longTitle, ProcessManager manager, MemoryManager memManager) {
        JFrame window = new JFrame(longTitle);
        window.setUndecorated(true);
        JPanel titleBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        titleBar.setBackground(new Color(45, 45, 45));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(new Color(187, 187, 187));

        JButton minimizeButton = new JButton("-");
        minimizeButton.setForeground(new Color(187, 187, 187));
        minimizeButton.setBackground(new Color(45, 45, 45));
        minimizeButton.setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 10));
        minimizeButton.setFocusPainted(false);
        minimizeButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                minimizeButton.setBackground(new Color(0, 120, 215));
            }
            public void mouseExited(MouseEvent e) {
                minimizeButton.setBackground(new Color(45, 45, 45));
            }
        });
        minimizeButton.addActionListener(e -> window.setState(Frame.ICONIFIED));

        JButton closeButton = new JButton("×");
        closeButton.setForeground(new Color(187, 187, 187));
        closeButton.setBackground(new Color(45, 45, 45));
        closeButton.setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 10));
        closeButton.setFocusPainted(false);
        closeButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                closeButton.setBackground(new Color(232, 17, 35));
            }
            public void mouseExited(MouseEvent e) {
                closeButton.setBackground(new Color(45, 45, 45));
            }
        });
        closeButton.addActionListener(e -> System.exit(0));

        // Add maximize button
        JButton maximizeButton = new JButton("⬜");
        maximizeButton.setForeground(new Color(187, 187, 187));
        maximizeButton.setBackground(new Color(45, 45, 45));
        maximizeButton.setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 10));
        maximizeButton.setFocusPainted(false);
        maximizeButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                maximizeButton.setBackground(new Color(0, 200, 0)); // Green on hover
            }
            public void mouseExited(MouseEvent e) {
                maximizeButton.setBackground(new Color(45, 45, 45));
            }
        });
        maximizeButton.addActionListener(e -> {
            if (window.getExtendedState() == JFrame.MAXIMIZED_BOTH) {
                window.setExtendedState(JFrame.NORMAL);
            } else {
                window.setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
        });

        // Center panel for the title with upper padding
        JPanel centerPanelTitle = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        centerPanelTitle.setOpaque(false);
        centerPanelTitle.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0)); // 8px top padding
        centerPanelTitle.add(titleLabel);

        // Right panel for window buttons (add more padding to each button)
        Insets buttonPadding = new Insets(4, 18, 4, 18);
        minimizeButton.setBorder(BorderFactory.createEmptyBorder(
            buttonPadding.top, buttonPadding.left, buttonPadding.bottom, buttonPadding.right));
        maximizeButton.setBorder(BorderFactory.createEmptyBorder(
            buttonPadding.top, buttonPadding.left, buttonPadding.bottom, buttonPadding.right));
        closeButton.setBorder(BorderFactory.createEmptyBorder(
            buttonPadding.top, buttonPadding.left, buttonPadding.bottom, buttonPadding.right));

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightPanel.setOpaque(false);
        rightPanel.add(minimizeButton);
        rightPanel.add(maximizeButton);
        rightPanel.add(closeButton);

        // New title bar layout
        titleBar.setLayout(new BorderLayout());
        titleBar.add(centerPanelTitle, BorderLayout.CENTER);
        titleBar.add(rightPanel, BorderLayout.EAST);

        final Point[] dragPoint = new Point[1];
        titleBar.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                dragPoint[0] = e.getPoint();
                SwingUtilities.convertPointToScreen(dragPoint[0], titleBar);
                dragPoint[0].x -= window.getX();
                dragPoint[0].y -= window.getY();
            }
        });
        titleBar.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point currentPoint = e.getPoint();
                SwingUtilities.convertPointToScreen(currentPoint, titleBar);
                window.setLocation(currentPoint.x - dragPoint[0].x, currentPoint.y - dragPoint[0].y);
            }
        });

        JButton sendCommand = new JButton("> ");
        sendCommand.setBackground(new Color(45, 45, 45));
        sendCommand.setForeground(new Color(187, 187, 187));
        sendCommand.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60)));

        JTextField commandField = new JTextField(20);
        commandField.setBackground(new Color(30, 30, 30));
        commandField.setForeground(Color.WHITE);
        commandField.setCaretColor(new Color(200, 200, 200));
        commandField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 60, 60)),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        JTextArea commandOutput = new JTextArea(20, 50);
        commandOutput.setEditable(false);
        commandOutput.setFocusable(false);
        commandOutput.setRequestFocusEnabled(false);
        commandOutput.setHighlighter(new javax.swing.text.DefaultHighlighter()); // allow highlights
        commandOutput.setBackground(new Color(18, 18, 18));
        commandOutput.setForeground(new Color(0, 255, 128));
        commandOutput.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60)));

        JScrollPane scroll = new JScrollPane(commandOutput);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60)));
        scroll.getVerticalScrollBar().setBackground(new Color(35, 35, 35));
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(8, Integer.MAX_VALUE));
        scroll.getVerticalScrollBar().setUI(new SlimRoundedScrollBarUI());
        scroll.getHorizontalScrollBar().setPreferredSize(new Dimension(Integer.MAX_VALUE, 8));
        scroll.getHorizontalScrollBar().setUI(new SlimRoundedScrollBarUI());

        // Register the commandOutput JTextArea for clearing from Main
        GUI.setConsoleOutput(commandOutput);

        // Use BorderLayout for auto layout
        window.setLayout(new BorderLayout());
        window.add(titleBar, BorderLayout.NORTH);

        // Panel for command area
        JPanel commandPanel = new JPanel(new BorderLayout(10, 10));
        commandPanel.setOpaque(false);
        commandPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        commandPanel.add(commandField, BorderLayout.CENTER);
        sendCommand.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 60, 60)),
            BorderFactory.createEmptyBorder(8, 18, 8, 18)
        ));
        commandPanel.add(sendCommand, BorderLayout.EAST);

        // Add scroll area and command panel
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setOpaque(false);
        centerPanel.add(scroll, BorderLayout.CENTER);
        centerPanel.add(commandPanel, BorderLayout.SOUTH);
        window.add(centerPanel, BorderLayout.CENTER);

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(600, 400);
        window.getContentPane().setForeground(new Color(187, 187, 187));
        window.getContentPane().setBackground(new Color(24, 24, 24));
        window.setIconImage(new ImageIcon("src/icon.jpg").getImage());
        window.setFont(new Font("Monospaced", 0, 12));
        window.setVisible(true);
        window.setResizable(true);

        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                commandOutput.append(String.valueOf((char) b));
                originalOut.write(b);
            }
        }));

        // Output initial welcome and prompt
        commandOutput.append("Welcome to " + title + "! Please type a command:\n");
        commandOutput.append( title+ "> ");

        commandField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String command = commandField.getText();
                commandOutput.append(command + "\n");
                commandField.setText("");
                Main.run(command, manager, memManager);
            }
        });

        sendCommand.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String command = commandField.getText();
                commandOutput.append(command + "\n");
                commandField.setText("");
                Main.run(command, manager, memManager);
            }
        });

        // Input size adjustment logic
        commandField.requestFocusInWindow();
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
            Color thumbColor = isDragging ? new Color(100, 100, 100) : this.thumbColor;
            g2.setColor(thumbColor);
            if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
                int width = THUMB_SIZE;
                int x = thumbBounds.x + (thumbBounds.width - width) / 2;
                RoundRectangle2D roundRect = new RoundRectangle2D.Float(
                    x, thumbBounds.y, width, thumbBounds.height, THUMB_RADIUS, THUMB_RADIUS
                );
                g2.fill(roundRect);
            } else {
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
