package org.example;

import java.util.*;

public class FCAIScheduling {
    private final Process[] processes;
    private final Queue<Process> readyQueue = new LinkedList<>();
    private final List<ExecutionStep> timeline = new ArrayList<>();
    private int currentTime = 0;

    public FCAIScheduling(Process[] processes) {
        this.processes = processes;
    }

    public void schedule() {
        int lastArrivalTime = Arrays.stream(processes).mapToInt(p -> p.arrivalTime).max().orElse(0);
        int maxBurstTime = Arrays.stream(processes).mapToInt(p -> p.burstTime).max().orElse(0);

        double V1 = lastArrivalTime / 10.0;
        double V2 = maxBurstTime / 10.0;

        // Main scheduling loop
        while (Arrays.stream(processes).anyMatch(p -> p.remainingTime > 0) || !readyQueue.isEmpty()) {
            // Add arrived processes to the ready queue
            for (Process process : processes) {
                if (process.arrivalTime <= currentTime && process.remainingTime > 0 && !readyQueue.contains(process)) {
                    process.calculateFCAIFactor(V1, V2);
                    readyQueue.add(process);
                }
            }

            // Sort the ready queue by FCAI factor
            List<Process> sortedQueue = new ArrayList<>(readyQueue);
            sortedQueue.sort(Comparator.comparingDouble(p -> p.FCAIFactor));

            // Pick the process with the lowest FCAI factor
            Process currentProcess = sortedQueue.get(0);
            readyQueue.remove(currentProcess);

            // Execute for 40% of the quantum
            int quantumTime = Math.min((int) Math.ceil(currentProcess.quantum * 0.4), currentProcess.remainingTime);
            int startTime = currentTime;
            executeProcess(currentProcess, quantumTime);
            int endTime = currentTime;

            // Log the execution step
            timeline.add(new ExecutionStep(
                    startTime + "–" + endTime,
                    currentProcess.processName,
                    quantumTime,
                    currentProcess.remainingTime,
                    currentProcess.quantum + " → " + (currentProcess.quantum + 2),
                    currentProcess.priority,
                    String.format("%.2f", currentProcess.FCAIFactor)
            ));

            // Check if preemption is required
            if (currentProcess.remainingTime > 0) {
                // Update quantum dynamically
                if (quantumTime < currentProcess.quantum) {
                    currentProcess.quantum += currentProcess.quantum - quantumTime;
                } else {
                    currentProcess.quantum += 2;
                }

                // Recalculate FCAI factor and re-add to the queue
                currentProcess.calculateFCAIFactor(V1, V2);
                readyQueue.add(currentProcess);
            } else {
                // Process completed
                currentProcess.completionTime = currentTime;
                currentProcess.turnAroundTime = currentProcess.completionTime - currentProcess.arrivalTime;
                currentProcess.waitingTime = currentProcess.turnAroundTime - currentProcess.burstTime;
            }
        }

        // Print results
        displayProcesses();
        displayTimeline();
    }

    private void executeProcess(Process process, int time) {
        currentTime += time;
        process.remainingTime -= time;
    }

    private void displayProcesses() {
        System.out.println("\nProcess Execution Results:");
        System.out.printf("%-10s %-10s %-10s %-10s %-10s %-10s %-10s\n",
                "Process", "Burst", "Arrival", "Priority", "Completion", "Waiting", "Turnaround");

        for (Process process : processes) {
            System.out.printf("%-10s %-10d %-10d %-10d %-10d %-10d %-10d\n",
                    process.processName,
                    process.burstTime,
                    process.arrivalTime,
                    process.priority,
                    process.completionTime,
                    process.waitingTime,
                    process.turnAroundTime
            );
        }

        // Calculate and display average waiting time and turnaround time
        double totalWaitingTime = Arrays.stream(processes).mapToDouble(p -> p.waitingTime).sum();
        double totalTurnAroundTime = Arrays.stream(processes).mapToDouble(p -> p.turnAroundTime).sum();

        double avgWaitingTime = totalWaitingTime / processes.length;
        double avgTurnAroundTime = totalTurnAroundTime / processes.length;

        System.out.println("\nAverage Waiting Time: " + String.format("%.2f", avgWaitingTime));
        System.out.println("Average Turnaround Time: " + String.format("%.2f", avgTurnAroundTime));
    }

    private void displayTimeline() {
        System.out.println("\nExecution Timeline:");
        System.out.printf("%-10s %-10s %-15s %-20s %-15s %-15s %-15s\n",
                "Time", "Process", "Executed Time", "Remaining Burst Time",
                "Updated Quantum", "Priority", "FCAI Factor");

        for (ExecutionStep step : timeline) {
            System.out.printf("%-10s %-10s %-15d %-20d %-15s %-15d %-15s\n",
                    step.timeRange,
                    step.processName,
                    step.executedTime,
                    step.remainingBurstTime,
                    step.updatedQuantum,
                    step.priority,
                    step.fcaiFactorAndDetails
            );
        }
    }

    // ExecutionStep Class to Log Timeline Details
    private static class ExecutionStep {
        String timeRange;
        String processName;
        int executedTime;
        int remainingBurstTime;
        String updatedQuantum;
        int priority;
        String fcaiFactorAndDetails;

        public ExecutionStep(String timeRange, String processName, int executedTime, int remainingBurstTime, String updatedQuantum, int priority, String fcaiFactorAndDetails) {
            this.timeRange = timeRange;
            this.processName = processName;
            this.executedTime = executedTime;
            this.remainingBurstTime = remainingBurstTime;
            this.updatedQuantum = updatedQuantum;
            this.priority = priority;
            this.fcaiFactorAndDetails = fcaiFactorAndDetails;
        }
    }
}
