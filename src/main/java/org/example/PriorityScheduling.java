package org.example;
import java.util.PriorityQueue;
import java.util.Comparator;

public class PriorityScheduling {
    public void schedule(Process[] processes, int contextSwitching) {
        int n = processes.length;
        int currentTime = 0;

        // first one will always be first
        int temp = processes[0].priority;
        processes[0].priority = Integer.MIN_VALUE;

        // Priority queue by priority or arrival
        PriorityQueue<Process> sortedProcesses = new PriorityQueue<>(n, new Comparator<Process>() {
            @Override
            public int compare(Process p1, Process p2) {
                // Sort by priority first, then by arrival time
                if (p1.priority == p2.priority) {
                    return Integer.compare(p1.arrivalTime, p2.arrivalTime);
                }
                return Integer.compare(p1.priority, p2.priority); // Smaller priority comes first
            }
        });

        // Add all processes to the priority queue
        for (Process process : processes) {
            sortedProcesses.add(process);
        }

        // Process the queue in order
        while (!sortedProcesses.isEmpty()) {
            Process process = sortedProcesses.poll(); // Fetch the process with the highest priority

            // If the current time is less than the process's arrival time, wait
            if (currentTime < process.arrivalTime) {
                currentTime = process.arrivalTime;
            }

            // Calculate the process metrics
            process.waitingTime = currentTime - process.arrivalTime;
            process.completionTime = currentTime + process.burstTime;
            process.turnAroundTime = process.completionTime - process.arrivalTime;

            currentTime += process.burstTime + contextSwitching;
        }

        //restore value of first priority
        processes[0].priority = temp;

        display(processes,contextSwitching);

    }
    public void display(Process[] processes,int contextSwitching){
        int n = processes.length;
        // Print table header
        System.out.println("----------------------------------------------------------------------------------------------------------");
        System.out.printf("| %-12s | %-13s | %-10s | %-8s | %-15s | %-12s | %-12s | %-15s | %-10s |\n",
                "Process Name", "Arrival Time", "Burst Time", "Priority", "Completion Time", "Waiting Time", "Turnaround",
                "Context Switching", "Color");
        System.out.println("----------------------------------------------------------------------------------------------------------");

        // Print process details in table format
        for (Process process : processes) {
            System.out.printf("| %-12s | %-13s | %-10s | %-8s | %-15s | %-12s | %-12s | %-15s | %-10s |\n",
                    process.processName, process.arrivalTime, process.burstTime, process.priority,
                    process.completionTime, process.waitingTime, process.turnAroundTime, contextSwitching, process.color);
        }
        System.out.println("----------------------------------------------------------------------------------------------------------");


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



