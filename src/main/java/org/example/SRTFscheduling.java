package org.example;

import java.util.*;


public class SRTFScheduler {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of processes: ");
        int n = scanner.nextInt();

        List<Process> processes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            System.out.print("Enter arrival time and burst time for process " + (i + 1) + ": ");
            int arrivalTime = scanner.nextInt();
            int burstTime = scanner.nextInt();
            processes.add(new Process(i + 1, arrivalTime, burstTime));
        }

        System.out.print("Enter context switch time: ");
        int contextSwitchTime = scanner.nextInt();

        simulateSRTF(processes, contextSwitchTime);
    }

    public static void simulateSRTF(List<Process> processes, int contextSwitchTime) {
        int currentTime = 0;
        int completed = 0;
        int n = processes.size();
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
                    currentProcess.turnArroundTime = currentProcess.completionTime - currentProcess.arrivalTime;
                    currentProcess.waitingTime = currentProcess.turnArroundTime - currentProcess.burstTime;
                } else {
                    readyQueue.add(currentProcess); // Re-add to the queue
                }

                // Aging: Reduce remaining time slightly for processes waiting long
                for (Process p : readyQueue) {
                    if (p != currentProcess) {
                        p.remainingTime = Math.max(1, p.remainingTime - 1); // Minimum remaining time is 1
                    }
                }
            } else {
                // If no process is ready, increment time
                currentTime++;
                contextSwitching = true;
            }
        }

        printProcessDetails(processes);
    }

    public static void printProcessDetails(List<Process> processes) {
        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;

        System.out.println("\nProcess\tArrival\tBurst\tCompletion\tWaiting\tTurnaround");
        for (Process p : processes) {
            System.out.println(p.processId + "\t" + p.arrivalTime + "\t" + p.burstTime + "\t" + p.completionTime + "\t" + p.waitingTime + "\t" + p.turnArroundTime);
            totalWaitingTime += p.waitingTime;
            totalTurnaroundTime += p.turnArroundTime;
        }

        System.out.println("\nAverage Waiting Time: " + (totalWaitingTime / processes.size()));
        System.out.println("Average Turnaround Time: " + (totalTurnaroundTime / processes.size()));
    }
}