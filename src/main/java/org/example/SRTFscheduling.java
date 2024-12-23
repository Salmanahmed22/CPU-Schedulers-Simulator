package org.example;

import java.util.*;

public class SRTFscheduling {
    private List<TimelineEntry> timeline = new ArrayList<>(); // Field to store the timeline
    private Process[] processes; // Field to store processes for access after simulation

    public void simulateSRTF(Process[] processes, int contextSwitchTime) {
        // Store the processes array for retrieval
        this.processes = processes;

        // Sort processes by arrival time initially
        Arrays.sort(processes, Comparator.comparingInt(p -> p.arrivalTime));

        int currentTime = 0;
        int completed = 0;
        int n = processes.length;
        Process currentProcess = null;

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
                Process shortestProcess = readyQueue.peek();

                // Check if we need to context switch
                if (currentProcess != null && currentProcess != shortestProcess) {
                    currentTime += contextSwitchTime;
                    // Add context switch to timeline
                    timeline.add(new TimelineEntry(currentTime - contextSwitchTime, currentTime, "Context Switch", 0));
                }

                // Execute the selected process
                currentProcess = readyQueue.poll();
                int executionStartTime = currentTime;
                currentProcess.remainingTime--;
                currentTime++;

                // Add execution to timeline
                timeline.add(new TimelineEntry(executionStartTime, currentTime, "Process " + currentProcess.processId, currentProcess.processId));

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
            }
        }
    }

    // Getter for timeline
    public List<TimelineEntry> getTimeline() {
        return timeline;
    }

    // Getter for processes
    public Process[] getProcesses() {
        return processes;
    }

    // Timeline entry class to track execution details
    static class TimelineEntry {
        int startTime;
        int endTime;
        String description;
        int processId;

        public TimelineEntry(int startTime, int endTime, String description, int processId) {
            this.startTime = startTime;
            this.endTime = endTime;
            this.description = description;
            this.processId = processId;
        }
    }

    // Helper methods for printing
    public static void printTimeline(List<TimelineEntry> timeline) {
        System.out.println("\nExecution Timeline:");
        System.out.println("Start\tEnd \tDescription");
        for (TimelineEntry entry : timeline) {
            System.out.println(entry.startTime + "\t\t" + entry.endTime + "\t\t" + entry.description);
        }
    }

    public static int getAvgWaitTime(Process[] processes) {
        int totalWaitingTime = 0;
        for (Process p : processes) {
            totalWaitingTime += p.waitingTime;
        }
        return totalWaitingTime / processes.length;
    }

    public static int getAvgTurnaroundTime(Process[] processes) {
        int totalTurnaroundTime = 0;
        for (Process p : processes) {
            totalTurnaroundTime += p.turnAroundTime;
        }
        return totalTurnaroundTime / processes.length;
    }

    public static void printProcessDetails(Process[] processes) {
        int avgWaitingTime = getAvgWaitTime(processes);
        int avgTurnaroundTime = getAvgTurnaroundTime(processes);

        System.out.println("\nProcess\tArrival\tBurst\tCompletion\tWaiting\tTurnaround");
        for (Process p : processes) {
            System.out.println(p.processId + "\t" + p.arrivalTime + "\t" + p.burstTime + "\t" + p.completionTime + "\t\t" + p.waitingTime + "\t" + p.turnAroundTime);
        }

        System.out.println("\nAverage Waiting Time: " + (avgWaitingTime));
        System.out.println("Average Turnaround Time: " + (avgTurnaroundTime));
    }
}
