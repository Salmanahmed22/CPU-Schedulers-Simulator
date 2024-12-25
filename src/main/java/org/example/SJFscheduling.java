package org.example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SJFscheduling {

    private List<Process> processList;
    private int avgWaitTime;
    private int avgTurnAroundTime;
    private final int AGING_THRESHOLD = 5;

    public SJFscheduling(Process[] process) {
        this.processList = new ArrayList<>();
        for (Process p : process) {
            processList.add(p);
        }
    }

    public List<Process> getProcessList() {
        return processList;
    }

    public int getAvgWaitTime() {
        return avgWaitTime;
    }

    public int getAvgTurnAroundTime() {
        return avgTurnAroundTime;
    }

    public void applyAging() {
        for (Process process : processList) {
            if (process.waitingTime > AGING_THRESHOLD) {
                process.priority -= 1; // Decrease priority value (less the number higher the priority)
                if (process.priority < 1) {
                    process.priority = 1;
                }
            }
        }
    }
    //edge solution for starvation
    //compare process priority first then burst time
    public void sortProcessesWithAging() {
        processList.sort(Comparator
                .comparingInt((Process p) -> p.priority)
                .thenComparingInt(p -> p.burstTime));
    }

    public void calcWaitingTime() {
        int currentTime = 0;

        // Sort processes by arrival time first
        processList.sort(Comparator.comparingInt(process -> process.arrivalTime));

        // calc waiting time
        for (int i = 0; i < processList.size(); i++) {

            // If the process arrives after the current time, the CPU idles until its arrival
            if (processList.get(i).arrivalTime > currentTime) {
                currentTime = processList.get(i).arrivalTime;
                processList.get(i).waitingTime = 0;
            }
            else
                processList.get(i).waitingTime = currentTime - processList.get(i).arrivalTime;

            currentTime += processList.get(i).burstTime;
        }

        // Calculate average waiting time after all processes
        calcAvgWaitTime();
    }

    public void calcAvgWaitTime() {
        int totalWaitTime = 0;
        for (Process process : processList) {
            totalWaitTime += process.waitingTime;
        }
        avgWaitTime = totalWaitTime / processList.size();
    }

    public void calcTurnArroundTime() {
        for (Process process : processList) {
            process.turnAroundTime = process.waitingTime + process.burstTime;
        }
        calcAvgTurnArroundTime();
    }

    public void calcAvgTurnArroundTime() {
        int totalTurnAroundTime = 0;
        for (Process process : processList) {
            totalTurnAroundTime += process.turnAroundTime;
        }
        avgTurnAroundTime = totalTurnAroundTime / processList.size();
    }

    public void sortProcesses() {
        processList.sort(Comparator.comparingInt(process -> process.burstTime));
    }

    public void executeWithAging() {
        int time = 0;
        while (!processList.isEmpty()) {
            // Apply aging before sorting
            applyAging();
            sortProcessesWithAging();

            // Execute the next process
            Process current = processList.remove(0);
            if (time > 0)
                current.waitingTime = time - current.arrivalTime;

            current.turnAroundTime = current.waitingTime + current.burstTime;
            time += current.burstTime;

            for(Process process : processList){
                process.waitingTime = time - process.arrivalTime;
            }

            // Print execution details
            System.out.println("Executed process: " + current.processName + " | Waiting Time: " + current.waitingTime +
                    " | Turnaround Time: " + current.turnAroundTime);
        }
    }

    public void displayProcesses() {
        System.out.println("\n=== SJF Scheduling with Aging Results ===");
        System.out.println("-------------------------------------------------------------");
        System.out.printf("%-15s %-15s %-20s %-20s\n", "Process", "Priority", "Waiting Time", "Turnaround Time");
        System.out.println("-------------------------------------------------------------");

        for (Process process : processList) {
            System.out.printf("%-15s %-15d %-20d %-20d\n",
                    process.processName,
                    process.priority,
                    process.waitingTime,
                    process.turnAroundTime);
        }

        System.out.println("-------------------------------------------------------------");
        System.out.printf("%-30s: %d\n", "Average Waiting Time", avgWaitTime);
        System.out.printf("%-30s: %d\n", "Average Turnaround Time", avgTurnAroundTime);
        System.out.println("=============================================================\n");
    }
}
