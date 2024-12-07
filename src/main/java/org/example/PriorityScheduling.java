package org.example;
import java.util.PriorityQueue;


public class PriorityScheduling {
    public void schedule(Process[] processes, int contextSwitching) {
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
        for (Process process : processes) {
            sortedProcesses.add(process);
        }

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

        display(processes,contextSwitching);

    }
    public void display(Process[] processes,int contextSwitching){
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



