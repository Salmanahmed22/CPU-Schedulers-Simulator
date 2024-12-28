package org.example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SJFscheduling {

    private List<Process> processList, tempProcessList;
    private double avgWaitingTime;
    private double avgTurnaroundTime;
    private static final int AGING_FACTOR = 1; // Increment to simulate aging

    public SJFscheduling(List<Process> processList) {
        this.processList = processList;
        this.tempProcessList = new ArrayList<>();
    }

    public void schedule() {
        int currentTime = 0;
        int totalWaitingTime = 0;
        int totalTurnAroundTime = 0;

        while (!processList.isEmpty()) {
            // Sort processes by arrival time first, then by adjusted burst time
            processList.sort(Comparator.comparingInt((Process p) -> p.arrivalTime)
                    .thenComparingInt(p -> p.burstTime - p.waitingTime * AGING_FACTOR));

            Process process = processList.get(0);

            if (currentTime < process.arrivalTime) {
                currentTime = process.arrivalTime;
            }

            // Set start time
            process.startTime = currentTime;

            process.waitingTime = currentTime - process.arrivalTime;
            process.completionTime = currentTime + process.burstTime;
            process.turnAroundTime = process.completionTime - process.arrivalTime;

            totalWaitingTime += process.waitingTime;
            totalTurnAroundTime += process.turnAroundTime;

            currentTime += process.burstTime;

            System.out.println("Process: " + process.processName +
                    ", Start Time: " + process.startTime +
                    ", Completion Time: " + process.completionTime +
                    ", Waiting Time: " + process.waitingTime +
                    ", Turnaround Time: " + process.turnAroundTime);

            // Remove the process from the list as it is completed
            tempProcessList.add(process);
            processList.remove(0);

            // Increment waiting time for all remaining processes to simulate aging
            for (Process p : processList) {
                if (p.arrivalTime <= currentTime) {
                    p.waitingTime += AGING_FACTOR;
                }
            }
        }

        avgWaitingTime = (double) totalWaitingTime / tempProcessList.size();
        avgTurnaroundTime = (double) totalTurnAroundTime / tempProcessList.size();
        System.out.println("Average Waiting Time: " + avgWaitingTime);
        System.out.println("Average Turnaround Time: " + avgTurnaroundTime);
    }

    public List<Process> getProcessList() {
        return tempProcessList;
    }

    public double getAvgWaitingTime() {
        return avgWaitingTime;
    }

    public double getAvgTurnaroundTime() {
        return avgTurnaroundTime;
    }
}