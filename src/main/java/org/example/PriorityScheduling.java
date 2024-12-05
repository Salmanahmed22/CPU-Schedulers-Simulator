package org.example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class PriorityScheduling {
    static class ScheduleEntry {
        String processName;
        int arrivalTime;
        int burstTime;
        int priority;
        int startTime;
        int endTime;
        boolean contextSwitch;

        ScheduleEntry(String processName, int arrivalTime, int burstTime, int priority, int startTime, int endTime, boolean contextSwitch) {
            this.processName = processName;
            this.arrivalTime = arrivalTime;
            this.burstTime = burstTime;
            this.priority = priority;
            this.startTime = startTime;
            this.endTime = endTime;
            this.contextSwitch = contextSwitch;
        }
    }

    public void schedule(Process[] processes, int contextSwitching) {

        PriorityQueue<Process> processPriorityQueue = new PriorityQueue<>(Comparator.comparingInt(p -> p.priority));

        ArrayList<ScheduleEntry> schedule = new ArrayList<>();


        for (Process process : processes) {
            processPriorityQueue.add(process);
        }

        System.out.println("\nScheduling Processes based on Priority:");
        int currentTime = 0;

        while (!processPriorityQueue.isEmpty()) {
            Process currentProcess = processPriorityQueue.poll();

            // Handle context switching if needed
            if (!schedule.isEmpty()) {
                currentTime += contextSwitching;
                schedule.add(new ScheduleEntry(
                        "Context Switch", 0, 0, 0, currentTime - contextSwitching, currentTime, true));
            }

            // Execute the process
            int startTime = Math.max(currentTime, currentProcess.arrivalTime);
            int endTime = startTime + currentProcess.burstTime;
            currentTime = endTime;

            schedule.add(new ScheduleEntry(
                    currentProcess.processName, currentProcess.arrivalTime, currentProcess.burstTime,
                    currentProcess.priority, startTime, endTime, false));
        }

        // Print the schedule table
        printScheduleTable(schedule);
    }

    private void printScheduleTable(ArrayList<ScheduleEntry> schedule) {
        System.out.println("+---------+--------------+------------+----------+------------+----------+-------------------+");
        System.out.println("| Process | Arrival Time | Burst Time | Priority | Start Time | End Time | Context Switching |");
        System.out.println("+---------+--------------+------------+----------+------------+----------+-------------------+");

        for (ScheduleEntry entry : schedule) {
            System.out.printf("| %-7s | %-12s | %-10s | %-8s | %-10s | %-8s | %-17s |\n",
                    entry.contextSwitch ? "N/A" : entry.processName,
                    entry.contextSwitch ? "N/A" : entry.arrivalTime,
                    entry.contextSwitch ? "N/A" : entry.burstTime,
                    entry.contextSwitch ? "N/A" : entry.priority,
                    entry.startTime, entry.endTime,
                    entry.contextSwitch ? "Yes" : "No");
        }

        System.out.println("+---------+--------------+------------+----------+------------+----------+-------------------+");
    }
}

