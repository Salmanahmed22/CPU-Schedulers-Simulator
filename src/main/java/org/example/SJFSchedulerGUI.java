package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SJFSchedulerGUI extends JFrame {

    private List<Process> processes;
    private double avgWaitTime;
    private double avgTurnAroundTime;

    public SJFSchedulerGUI(List<Process> processes, double avgWaitTime, double avgTurnAroundTime) {
        this.processes = processes;
        this.avgWaitTime = avgWaitTime;
        this.avgTurnAroundTime = avgTurnAroundTime;

        // Set up the frame
        setTitle("SJF Scheduler GUI");
        setSize(800, 400); // Increased width for better visualization
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
                int x = 50;
                int y = 100;
                int height = 40;
                int timeScale = 10; // Scale burst time for visualization

                // Font settings for time labels
                FontMetrics fm = g.getFontMetrics();
                g.setFont(new Font("Arial", Font.PLAIN, 12));

                // Track the cumulative time to ensure processes stick together
                int cumulativeTime = 0;

                for (Process process : processes) {
                    String hexColor = Colors.getHex(process.color);
                    g.setColor(Color.decode(hexColor));

                    int width = process.burstTime * timeScale; // Scale burst time

                    // Use cumulative time for x-coordinate
                    g.fillRect(x + cumulativeTime, y, width, height);
                    g.setColor(Color.BLACK);
                    g.drawRect(x + cumulativeTime, y, width, height);

                    // Draw process name inside the rectangle
                    String processName = process.processName;
                    int nameWidth = fm.stringWidth(processName);
                    g.drawString(processName,
                            x + cumulativeTime + (width - nameWidth) / 2,
                            y + height / 2 + fm.getAscent() / 2 - 2);

                    // Update cumulative time
                    cumulativeTime += width;
                }

                // Draw the timeline
                g.setColor(Color.BLACK);
                int timelineY = y + height + 20;
                g.drawLine(40, timelineY, x + cumulativeTime, timelineY); // Horizontal line

                // Draw time markers
                cumulativeTime = 0;
                for (Process process : processes) {
                    int markerX = x + cumulativeTime;

                    // Draw time marker
                    g.drawLine(markerX, timelineY - 5, markerX, timelineY + 5);

                    // Draw time label
                    String timeStr = String.valueOf(cumulativeTime);
                    int timeStrWidth = fm.stringWidth(timeStr);
                    g.drawString(timeStr, markerX - timeStrWidth / 2, timelineY + 20);

                    // Update cumulative time
                    cumulativeTime += process.burstTime * timeScale;
                }

                // Draw final time marker
                g.drawLine(x + cumulativeTime, timelineY - 5, x + cumulativeTime, timelineY + 5);
                String finalTimeStr = String.valueOf(cumulativeTime / timeScale);
                int finalTimeStrWidth = fm.stringWidth(finalTimeStr);
                g.drawString(finalTimeStr, x + cumulativeTime - finalTimeStrWidth / 2, timelineY + 20);
            }
        };
        chartPanel.setPreferredSize(new Dimension(700, 250)); // Increased height to accommodate time labels
        chartPanel.setBackground(Color.WHITE);
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
        infoPanel.setPreferredSize(new Dimension(300, 200));
        return infoPanel;
    }

    private JPanel createStatisticsPanel() {
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new GridLayout(2, 1));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        statsPanel.add(new JLabel("Average Waiting Time: " + String.format("%.2f", avgWaitTime)));
        statsPanel.add(new JLabel("Average Turnaround Time: " + String.format("%.2f", avgTurnAroundTime)));
        return statsPanel;
    }

}