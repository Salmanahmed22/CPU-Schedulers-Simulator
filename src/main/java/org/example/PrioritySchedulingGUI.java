package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.List;

public class PrioritySchedulingGUI extends JFrame {

    private List<Process> processes;
    private double avgWaitTime;
    private double avgTurnAroundTime;
    private int contextSwitching; // Added context-switching value

    public PrioritySchedulingGUI(List<Process> processes, double avgWaitTime, double avgTurnAroundTime, int contextSwitching) {
        this.processes = processes;
        this.avgWaitTime = avgWaitTime;
        this.avgTurnAroundTime = avgTurnAroundTime;
        this.contextSwitching = contextSwitching; // Initialize context-switching value

        // Set up the frame
        setTitle("Priority Scheduling GUI");
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Split panels
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createChartPanel(), createProcessInfoPanel());
        splitPane.setDividerLocation(600);
        add(splitPane, BorderLayout.CENTER);

        // Add the statistics panel
        add(createStatisticsPanel(), BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createChartPanel() {
        JPanel chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Draw the CPU scheduling chart
                int x = 50; // Starting position
                int y = 50;
                int height = 40;

                for (Process process : processes) {
                    g.setColor(Color.decode(process.color)); // Use the process's color
                    int width = process.burstTime * 20; // Scale burst time for visualization
                    g.fillRect(x, y, width, height);

                    // Draw process name and burst time above the block
                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, width, height);
                    g.drawString(process.processName + " (" + process.burstTime + ")", x + width / 2 - 15, y - 5);

                    x += width + 10; // Move to the next process position
                }
            }
        };
        chartPanel.setPreferredSize(new Dimension(600, 400));
        chartPanel.setBorder(BorderFactory.createTitledBorder("Gantt Chart"));
        return chartPanel;
    }

    private JScrollPane createProcessInfoPanel() {
        String[] columnNames = {"Process", "Color", "Arrival", "Burst", "Priority", "Wait", "Turnaround", "Completion"};
        Object[][] data = new Object[processes.size()][8];

        for (int i = 0; i < processes.size(); i++) {
            Process process = processes.get(i);
            data[i][0] = process.processName;
            data[i][1] = process.color;
            data[i][2] = process.arrivalTime;
            data[i][3] = process.burstTime;
            data[i][4] = process.priority;
            data[i][5] = process.waitingTime;
            data[i][6] = process.turnAroundTime;
            data[i][7] = process.completionTime;
        }

        JTable table = new JTable(data, columnNames);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Set column widths
        TableColumn column;
        for (int i = 0; i < columnNames.length; i++) {
            column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(100);
        }

        // Center align table cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Process Information"));
        scrollPane.setPreferredSize(new Dimension(400, 400));
        return scrollPane;
    }

    private JPanel createStatisticsPanel() {
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new GridLayout(3, 1)); // Adjusted layout for 3 rows
        statsPanel.setBorder(BorderFactory.createTitledBorder("Statistics"));
        statsPanel.add(new JLabel("Average Waiting Time: " + avgWaitTime));
        statsPanel.add(new JLabel("Average Turnaround Time: " + avgTurnAroundTime));
        statsPanel.add(new JLabel("Context Switching: " + contextSwitching)); // Display context-switching value
        return statsPanel;
    }

    public static void main(String[] args) {
        // data for testing
        Process[] guiProcesses = {
                new Process("P1", "#FF0000", 0, 0, 4, 3),
                new Process("P2", "#00FF00", 1, 1, 2, 2),
                new Process("P3", "#0000FF", 2, 2, 3, 4),
                new Process("P4", "#FFFF00", 3, 4, 2, 1),
        };

        // Context-switching value
        int contextSwitchValue = 0;

        PriorityScheduling priorityScheduling = new PriorityScheduling();
        priorityScheduling.schedule(guiProcesses, contextSwitchValue);



        double totalWaitingTime = 0, totalTurnaroundTime = 0;
        for (Process process : guiProcesses) {
            totalWaitingTime += process.waitingTime;
            totalTurnaroundTime += process.turnAroundTime;
        }
        double avgWait = totalWaitingTime / guiProcesses.length;
        double avgTurnAround = totalTurnaroundTime / guiProcesses.length;



        // Launch the GUI
        new PrioritySchedulingGUI(
                List.of(guiProcesses),
                avgWait,
                avgTurnAround,
                contextSwitchValue // Pass context-switching value
        );
    }
}
