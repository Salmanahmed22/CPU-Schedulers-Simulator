package org.example;

import java.util.PriorityQueue;
import java.util.Arrays;

public class PriorityScheduling {
    private Process[] processes;
    private double avgWaitTime;
    private double avgTurnaroundTime;

    public void schedule(Process[] processes, int contextSwitching) {
        this.processes = processes;
        int n = processes.length;
        int currentTime = 0;

        // first one will always be first
        int temp = processes[0].priority;
        processes[0].priority = Integer.MIN_VALUE;

        // priority queue by priority or arrival
        PriorityQueue<Process> sortedProcesses = new PriorityQueue<>(n, (p1, p2) -> {
            if (p1.priority == p2.priority) {
                return Integer.compare(p1.arrivalTime, p2.arrivalTime);
            }
            return Integer.compare(p1.priority, p2.priority); // smaller priority comes first
        });

        // Add all processes to the priority queue
        sortedProcesses.addAll(Arrays.asList(processes));

        // process the queue in order
        while (!sortedProcesses.isEmpty()) {
            Process process = sortedProcesses.poll();

            //like p1 arrival is 0 ,3  and p2 arrival is 5 so it has to wait 2 mins before been executed
            // if the current time is less than the process's arrival time, wait
            if (currentTime < process.arrivalTime) {
                currentTime = process.arrivalTime;
            }

            //completion from 0 till process right edge , wait is how much process waited since arrival
            //turnarround is the whole process time
            process.waitingTime = currentTime - process.arrivalTime;
            process.completionTime = currentTime + process.burstTime;
            process.turnAroundTime = process.completionTime - process.arrivalTime;

            currentTime += process.burstTime + contextSwitching;
        }

        //restore value of first priority
        processes[0].priority = temp;

        // Calculate average waiting and turnaround times
        calculateAverages(processes);

        // Display process information
        display(processes, contextSwitching);
    }

    private void calculateAverages(Process[] processes) {
        int n = processes.length;
        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;

        for (Process process : processes) {
            totalWaitingTime += process.waitingTime;
            totalTurnaroundTime += process.turnAroundTime;
        }

        avgWaitTime = totalWaitingTime / n;
        avgTurnaroundTime = totalTurnaroundTime / n;
    }

    public Process[] getProcessList() {
        return processes;
    }

    public double getAvgWaitTime() {
        return avgWaitTime;
    }

    public double getAvgTurnaroundTime() {
        return avgTurnaroundTime;
    }

    public void display(Process[] processes, int contextSwitching) {
        int n = processes.length;
        // print table header
        System.out.println("----------------------------------------------------------------------------------------------------------");
        System.out.printf("| %-12s | %-13s | %-10s | %-8s | %-15s | %-12s | %-12s | %-15s | %-10s |\n",
                "Process Name", "Arrival Time", "Burst Time", "Priority", "Completion Time", "Waiting Time", "Turnaround",
                "Context Switching", "Color");
        System.out.println("----------------------------------------------------------------------------------------------------------");

        // print process details in table format
        for (Process process : processes) {
            System.out.printf("| %-12s | %-13s | %-10s | %-8s | %-15s | %-12s | %-12s | %-15s | %-10s |\n",
                    process.processName, process.arrivalTime, process.burstTime, process.priority,
                    process.completionTime, process.waitingTime, process.turnAroundTime, contextSwitching, process.color);
        }
        System.out.println("----------------------------------------------------------------------------------------------------------");

        // print average waiting time and turnaround time
        System.out.printf("\nAverage Waiting Time: %.2f\n", avgWaitTime);
        System.out.printf("Average Turnaround Time: %.2f\n", avgTurnaroundTime);
    }
}
