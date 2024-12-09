package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PrioritySchedulerGUI extends JFrame {
    private Process[] processes;
    private double avgWaitTime;
    private double avgTurnaroundTime;
    private int contextSwitching;

    public PrioritySchedulerGUI(Process[] processes, double avgWaitTime, double avgTurnaroundTime,int contextSwitching) {
        this.processes = processes;
        this.avgWaitTime = avgWaitTime;
        this.avgTurnaroundTime = avgTurnaroundTime;
        this.contextSwitching = contextSwitching;
        setTitle("Priority Scheduling Visualization");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top Panel for Graph
        JPanel graphPanel = new GraphPanel();
        graphPanel.setBackground(Color.WHITE);
        add(graphPanel, BorderLayout.CENTER);

        // Right Panel for Process Info
        JPanel processInfoPanel = createProcessInfoPanel();
        add(processInfoPanel, BorderLayout.EAST);

        // Bottom Panel for Statistics
        JPanel statsPanel = createStatsPanel();
        add(statsPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createProcessInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(200, 0));

        String[] columns = {"Process", "Color", "Name", "Priority"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Process p : processes) {
            model.addRow(new Object[]{p.processId, p.color, p.processName, p.priority});
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.DARK_GRAY);
        panel.setLayout(new GridLayout(3, 1));

        JLabel scheduleNameLabel = new JLabel("Schedule Name: Priority Scheduling");
        scheduleNameLabel.setForeground(Color.WHITE);
        JLabel avgWaitTimeLabel = new JLabel("Avg waiting time: " + avgWaitTime);
        avgWaitTimeLabel.setForeground(Color.WHITE);
        JLabel avgTurnaroundTimeLabel = new JLabel("Avg TurnaroundTime: " + avgTurnaroundTime);
        avgTurnaroundTimeLabel.setForeground(Color.WHITE);

        panel.add(scheduleNameLabel);
        panel.add(avgWaitTimeLabel);
        panel.add(avgTurnaroundTimeLabel);

        return panel;
    }

    private class GraphPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Padding and scaling variables
            int padding = 50;
            int yPosition = padding;
            int barHeight = 40;
            int contextBarHeight = 20;
            int totalWidth = getWidth() - (2 * padding);
            int maxTime = getMaxCompletionTime();  // Adjusted to get the max completion time from processes
            int unitWidth = totalWidth / (maxTime + 1);

            // Draw timeline axis
            g.setColor(Color.BLACK);
            g.drawLine(padding, yPosition + barHeight + 20, padding + totalWidth, yPosition + barHeight + 20);

            // Draw time markers
            for (int i = 0; i <= maxTime; i++) {
                int x = padding + (i * unitWidth);
                g.drawLine(x, yPosition + barHeight + 15, x, yPosition + barHeight + 25);
                g.drawString(Integer.toString(i), x - 5, yPosition + barHeight + 40);
            }

            // Draw process execution bars and context switches if applicable
            int xStart = padding;
            for (Process process : processes) {
                int width = process.burstTime * unitWidth;

                // Draw the process bar
                g.setColor(Color.decode(Colors.getHex(process.color)));
                g.fillRect(xStart, yPosition, width, barHeight);
                g.setColor(Color.BLACK);
                g.drawRect(xStart, yPosition, width, barHeight);

                // Draw process name
                g.drawString(process.processName, xStart + (width / 2) - 10, yPosition + (barHeight / 2) + 5);

                // If context switching is needed, draw the context switch bar
                if (contextSwitching > 0) {
                    int contextWidth = contextSwitching * unitWidth;
                    xStart += width;  // Move xStart to the end of the process
                    g.setColor(Color.GRAY);  // Set color for context switch
                    g.fillRect(xStart, yPosition + barHeight, contextWidth, contextBarHeight);  // Draw context switch bar

                    // Draw the "ContextSwitch" string centered within the context switch bar
                    String contextSwitchText = "ContextSwitch";
                    FontMetrics fm = g.getFontMetrics();
                    int textWidth = fm.stringWidth(contextSwitchText);  // Get the width of the string
                    int textX = xStart + (contextWidth - textWidth) / 2;  // Center the text horizontally
                    int textY = yPosition + barHeight + (contextBarHeight / 2) + fm.getAscent() / 2;  // Center the text vertically
                    g.setColor(Color.BLACK);  // Set color for the text
                    g.drawString(contextSwitchText, textX, textY);  // Draw the text
                    xStart += contextWidth;  // Move xStart past the context switch bar
                } else {
                    // If no context switch, just move to the next process
                    xStart += width;
                }

            }
        }

        // Calculate max completion time (used for timeline width)
        private int getMaxCompletionTime() {
            int maxTime = 0;
            for (Process process : processes) {
                if (process.completionTime > maxTime) {
                    maxTime = process.completionTime;
                }
            }
            return maxTime;
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(800, 300);
        }
    }


}