package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FCAIScheduling {
    private final Process[] processes;
    private final int contextSwitching;
    private final double V1;
    private final double V2;

    public FCAIScheduling(Process[] processes, int contextSwitching) {
        this.processes = processes;
        this.contextSwitching = contextSwitching;

        // Calculate V1 and V2
        int lastArrivalTime = 0;
        int maxBurstTime = 0;
        for (Process process : processes) {
            lastArrivalTime = Math.max(lastArrivalTime, process.arrivalTime);
            maxBurstTime = Math.max(maxBurstTime, process.burstTime);
        }
        this.V1 = lastArrivalTime / 10.0;
        this.V2 = maxBurstTime / 10.0;

        // Initialize FCAI factors
        for (Process process : processes) {
            process.calculateFCAIFactor(V1, V2);
        }
    }

    public void schedule() {
        ArrayList<Process> readyQueue = new ArrayList<>();
        int currentTime = 0;

        // Detailed Timeline Header
        System.out.println("Detailed Execution Timeline:");
        System.out.println("Time\tProcess Executed\tTime Remaining Burst Time\tUpdated Quantum\tPriority\tFCAI Factor\tAction Details");

        while (true) {
            // Add processes to the ready queue based on arrival time
            for (Process process : processes) {
                if (process.arrivalTime <= currentTime && process.remainingTime > 0 && !readyQueue.contains(process)) {
                    readyQueue.add(process);
                }
            }

            if (readyQueue.isEmpty()) {
                // If all processes are completed, break
                boolean allCompleted = true;
                for (Process process : processes) {
                    if (process.remainingTime > 0) {
                        allCompleted = false;
                        currentTime++;
                        break;
                    }
                }
                if (allCompleted) break;
                continue;
            }

            // Sort readyQueue by FCAI factor (descending)
            readyQueue.sort(Comparator.comparingDouble((Process p) -> p.FCAIFactor).reversed());

            // Pick the process with the highest FCAI factor
            Process currentProcess = readyQueue.get(0);
            readyQueue.remove(0);

            // Calculate the execution time and adjust quantum
            int executionTime = Math.min(currentProcess.quantum, currentProcess.remainingTime);
            if (executionTime > (currentProcess.quantum * 0.4)) {
                executionTime = (int) Math.ceil(currentProcess.quantum * 0.4); // Allow preemption after 40%
            }

            // Print detailed information for the current time slice
            System.out.printf("%d-%d\t%s\t%d\t\t%d\t\t\t%d → %d\t\t%.1f\t\t%s\n",
                    currentTime, currentTime + executionTime,
                    currentProcess.processName, currentProcess.remainingTime,
                    currentProcess.burstTime, currentProcess.quantum,
                    currentProcess.quantum + 2, currentProcess.FCAIFactor,
                    "Executing");

            // Update the process state
            currentProcess.remainingTime -= executionTime;
            currentTime += executionTime + contextSwitching;

            // After execution, update the quantum for this process
            if (currentProcess.remainingTime > 0) {
                currentProcess.quantum += 2; // Increase quantum for future executions
                readyQueue.add(currentProcess);
            } else {
                currentProcess.completionTime = currentTime; // Process is completed
            }

            // Recalculate FCAI factor for all processes
            for (Process process : processes) {
                if (process.remainingTime > 0) {
                    process.calculateFCAIFactor(V1, V2);
                }
            }

            // Print the current action details after execution
            if (currentProcess.remainingTime > 0) {
                System.out.printf("Post Execution Update: %s has remaining burst time: %d\n",
                        currentProcess.processName, currentProcess.remainingTime);
            } else {
                System.out.printf("Post Execution Update: %s completed at time %d\n",
                        currentProcess.processName, currentTime);
            }
        }

        // Print the final results after all processes are completed
        printResults();
    }

    private void printResults() {
        System.out.println("\nFCAI Scheduling Results:");
        System.out.println("Process\tArrival\tBurst\tPriority\tCompletion\tTurnAround\tWaiting");
        for (Process process : processes) {
            process.turnAroundTime = process.completionTime - process.arrivalTime;
            process.waitingTime = process.turnAroundTime - process.burstTime;

            System.out.println(process.processName + "\t" + process.arrivalTime + "\t" + process.burstTime + "\t" +
                    process.priority + "\t\t" + process.completionTime + "\t\t" +
                    process.turnAroundTime + "\t\t" + process.waitingTime);
        }
    }
}
