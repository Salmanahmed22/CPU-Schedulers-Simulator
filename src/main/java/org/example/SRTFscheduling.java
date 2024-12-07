package org.example;

import java.util.*;


public class SRTFscheduling {

    public static void simulateSRTF(Process[] processes, int contextSwitchTime) {
        int currentTime = 0;
        int completed = 0;
        int n = processes.length;
        boolean contextSwitching = false;

        // Priority queue to hold processes based on remaining time and arrival time
        PriorityQueue<Process> readyQueue = new PriorityQueue<>((p1, p2) -> {
            if (p1.remainingTime == p2.remainingTime) {
                return Integer.compare(p1.arrivalTime, p2.arrivalTime);
            }
            return Integer.compare(p1.remainingTime, p2.remainingTime);
        });

        while (completed < n) {
            // Add all processes that have arrived by current time to the ready queue
            for (Process p : processes) {
                if (p.arrivalTime <= currentTime && p.remainingTime > 0 && !readyQueue.contains(p)) {
                    readyQueue.add(p);
                }
            }

            if (!readyQueue.isEmpty()) {
                // Get the process with the shortest remaining time
                Process currentProcess = readyQueue.poll();

                // Simulate context switching
                if (contextSwitching) {
                    currentTime += contextSwitchTime;
                    contextSwitching = false;
                }

                // Execute the process for 1 unit of time
                currentProcess.remainingTime--;
                currentTime++;

                // If process finishes execution
                if (currentProcess.remainingTime == 0) {
                    completed++;
                    currentProcess.completionTime = currentTime;
                    currentProcess.turnAroundTime = currentProcess.completionTime - currentProcess.arrivalTime;
                    currentProcess.waitingTime = currentProcess.turnAroundTime - currentProcess.burstTime;
                } else {
                    readyQueue.add(currentProcess); // Re-add to the queue
                }
            } else {
                // If no process is ready, increment time
                currentTime++;
                contextSwitching = true;
            }
        }

        printProcessDetails(processes);
    }

    public static void printProcessDetails(Process[] processes) {
        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;

        System.out.println("\nProcess\tArrival\tBurst\tCompletion\tWaiting\tTurnaround");
        for (Process p : processes) {
            System.out.println(p.processId + "\t" + p.arrivalTime + "\t" + p.burstTime + "\t" + p.completionTime + "\t" + p.waitingTime + "\t" + p.turnAroundTime);
            totalWaitingTime += p.waitingTime;
            totalTurnaroundTime += p.turnAroundTime;
        }

        System.out.println("\nAverage Waiting Time: " + (totalWaitingTime / processes.length));
        System.out.println("Average Turnaround Time: " + (totalTurnaroundTime / processes.length));
    }
}
