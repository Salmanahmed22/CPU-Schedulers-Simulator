package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SRTFSchedulerGUI extends JFrame {
    private List<SRTFscheduling.TimelineEntry> timeline;
    private Process[] processes;
    private int avgWaitTime;
    private int avgTurnaroundTime;

    public SRTFSchedulerGUI(List<SRTFscheduling.TimelineEntry> timeline, Process[] processes, int avgWaitTime, int avgTurnaroundTime) {
        this.timeline = timeline;
        this.processes = processes;
        this.avgWaitTime = avgWaitTime;
        this.avgTurnaroundTime = avgTurnaroundTime;

        setTitle("CPU Scheduling Graph");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Graph
        JPanel graphPanel = new GraphPanel();
        graphPanel.setBackground(Color.WHITE);
        add(graphPanel, BorderLayout.CENTER);

        // Process Info
        JPanel processInfoPanel = createProcessInfoPanel();
        add(processInfoPanel, BorderLayout.EAST);

        // Statistics
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

        JLabel scheduleNameLabel = new JLabel("Schedule Name: SRTF");
        scheduleNameLabel.setForeground(Color.WHITE);
        JLabel avgWaitTimeLabel = new JLabel("Avgwaitingtime: " + avgWaitTime);
        avgWaitTimeLabel.setForeground(Color.WHITE);
        JLabel avgTurnaroundTimeLabel = new JLabel("Avgturnaround: " + avgTurnaroundTime);
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

            // Padding
            int padding = 50;
            int yPosition = padding;
            int barHeight = 40;
            int scalingFactor = 20;

           //total width and scale the timeline
            int totalWidth = getWidth() - (2 * padding);
            int maxTime = timeline.stream().mapToInt(entry -> entry.endTime).max().orElse(0);
            int unitWidth = totalWidth / (maxTime + 1);

            // timeline axis
            g.setColor(Color.BLACK);
            g.drawLine(padding, yPosition + barHeight + 20, padding + totalWidth, yPosition + barHeight + 20);

            //time markers
            for (int i = 0; i <= maxTime; i++) {
                int x = padding + (i * unitWidth);
                g.drawLine(x, yPosition + barHeight + 15, x, yPosition + barHeight + 25);
                g.drawString(Integer.toString(i), x - 5, yPosition + barHeight + 40);
            }

            // Draw process execution bars and context switches if found
            for (SRTFscheduling.TimelineEntry entry : timeline) {
                int xStart = padding + (entry.startTime * unitWidth);
                int width = (entry.endTime - entry.startTime) * unitWidth;
                String name = "Context Switching";
                if (entry.description.equals("Context Switch")) {
                    g.setColor(Color.GRAY);
                } else {
                    // set color based on process ID
                    for (Process process : processes) {
                        if (process.processId == entry.processId) {
                            name = process.processName;
                            String hexColor = Colors.getHex(process.color);
                            g.setColor(Color.decode(hexColor));
                            break;
                        }
                    }
                }

                // draw bar
                g.fillRect(xStart, yPosition, width, barHeight);
                g.setColor(Color.BLACK);
                g.drawRect(xStart, yPosition, width, barHeight);

                // draw process ID or description in the middle of the bar
                g.drawString(name, xStart + (width / 2) - 10, yPosition + (barHeight / 2) + 5);
            }

        }


        @Override
        public Dimension getPreferredSize() {
            return new Dimension(800, 300); // Set a preferred size for the graph panel
        }
    }



}
