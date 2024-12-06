package org.example;
import java.util.Arrays;

public class PriorityScheduling {
    public void schedule(Process[] processes, int contextSwitching) {
        int n = processes.length;
        int currentTime = 0;

        // Sort processes by priority, then by arrival time
        Arrays.sort(processes, (p1, p2) -> {
            if (p1.priority == p2.priority) {
                return Integer.compare(p1.arrivalTime, p2.arrivalTime);
            }
            return Integer.compare(p1.priority, p2.priority);
        });

        // Process the queue in order
        for (int i = 0; i < n; i++) {
            Process process = processes[i];

            // If current time is less than the process's arrival time, wait
            if (currentTime < process.arrivalTime) {
                currentTime = process.arrivalTime;
            }

            // Calculate the process metrics
            process.waitingTime = currentTime - process.arrivalTime;
            process.completionTime = currentTime + process.burstTime;
            process.turnAroundTime = process.completionTime - process.arrivalTime;
            currentTime += process.burstTime + contextSwitching;
        }

        // Print table header
        System.out.println("------------------------------------------------------------------------------------------");
        System.out.printf("| %-12s | %-13s | %-10s | %-8s | %-12s | %-12s | %-10s |\n",
                "Process Name", "Arrival Time", "Burst Time", "Priority", "Waiting Time", "Turnaround", "Color");
        System.out.println("------------------------------------------------------------------------------------------");

        // Print process details in table format
        for (Process process : processes) {
            System.out.printf("| %-12s | %-13d | %-10d | %-8d | %-12d | %-12d | %-10s |\n",
                    process.processName, process.arrivalTime, process.burstTime, process.priority,
                    process.waitingTime, process.turnAroundTime, process.color);
        }
        System.out.println("------------------------------------------------------------------------------------------");

        // Calculate and print average waiting time and turnaround time
        double totalWaitingTime = 0, totalTurnaroundTime = 0;
        for (Process process : processes) {
            totalWaitingTime += process.waitingTime;
            totalTurnaroundTime += process.turnAroundTime;
        }
        double averageWaitingTime = totalWaitingTime / n;
        double averageTurnaroundTime = totalTurnaroundTime / n;

        System.out.printf("\nAverage Waiting Time: %.2f\n", averageWaitingTime);
        System.out.printf("Average Turnaround Time: %.2f\n", averageTurnaroundTime);
    }
}
