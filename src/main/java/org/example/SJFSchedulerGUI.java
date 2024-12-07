package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SJFSchedulerGUI extends JFrame {

    private List<Process> processes;
    private int avgWaitTime;
    private int avgTurnAroundTime;

    public SJFSchedulerGUI(List<Process> processes, int avgWaitTime, int avgTurnAroundTime) {
        this.processes = processes;
        this.avgWaitTime = avgWaitTime;
        this.avgTurnAroundTime = avgTurnAroundTime;

        // Set up the frame
        setTitle("SJF Scheduler GUI");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Add the chart panel
        add(createChartPanel(), BorderLayout.CENTER);

        // Add the process information table
        add(createProcessInfoPanel(), BorderLayout.EAST);

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
                    String hexColor = Colors.getHex(process.color);

                    g.setColor(Color.decode(hexColor)); // Use the process's color
                    int width = process.burstTime * 10; // Scale burst time for visualization
                    g.fillRect(x, y, width, height);
                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, width, height);
                    g.drawString(process.processName, x + width / 2 - 10, y + 25);
                    x += width + 10; // Move to the next process position
                }
            }
        };
        chartPanel.setPreferredSize(new Dimension(600, 200));
        return chartPanel;
    }

    private JPanel createProcessInfoPanel() {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BorderLayout());
        String[] columnNames = {"Process", "Color", "PID", "Priority"};
        Object[][] data = new Object[processes.size()][4];

        for (int i = 0; i < processes.size(); i++) {
            Process process = processes.get(i);
            data[i][0] = process.processName;
            data[i][1] = process.color;
            data[i][2] = process.processId;
            data[i][3] = process.priority;
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        infoPanel.add(scrollPane, BorderLayout.CENTER);
        infoPanel.setPreferredSize(new Dimension(200, 200));
        return infoPanel;
    }

    private JPanel createStatisticsPanel() {
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new GridLayout(2, 1));
        statsPanel.add(new JLabel("Average Waiting Time: " + avgWaitTime));
        statsPanel.add(new JLabel("Average Turnaround Time: " + avgTurnAroundTime));
        return statsPanel;
    }

    public static void main(String[] args) {
        // Mock data for testing
        Process[] mockProcesses = {
                new Process("P1", "red", 1, 0, 3, 1),
                new Process("P2", "green", 2, 1, 5, 2),
                new Process("P3", "blue", 3, 2, 2, 3),
                new Process("P4", "magenta", 4, 3, 1, 4),
        };
        SJFscheduling sjfscheduling = new SJFscheduling(mockProcesses);
        sjfscheduling.sortProcesses();
        sjfscheduling.calcWaitingTime();
        sjfscheduling.calcTurnArroundTime();

        // Launch the GUI
        new SJFSchedulerGUI(
                sjfscheduling.getProcessList(),
                sjfscheduling.getAvgWaitTime(),
                sjfscheduling.getAvgTurnAroundTime()
        );
    }
}
