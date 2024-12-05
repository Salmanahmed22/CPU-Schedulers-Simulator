package org.example;
import java.util.Scanner;


public class Main {
    public static void main(String []args) {
        Scanner scanner = new Scanner(System.in);

        // input number of processes
        System.out.print("Enter the number of processes: ");

        int numProcesses = scanner.nextInt();

        int roundRobinTimeQuantum = scanner.nextInt();

        int contextSwitching = scanner.nextInt();

        Process [] processes = new Process[numProcesses];

        // Input the process details
        for (int i = 0; i < numProcesses; i++) {
            System.out.println("Enter details for Process " + (i + 1));

            System.out.print("Enter Process Name: ");
            String processName = scanner.next();

            System.out.print("Enter Process Color (for graphical representation): ");
            String color = scanner.next();

            System.out.print("Enter Process Arrival Time: ");
            int arrivalTime = scanner.nextInt();

            System.out.print("Enter Process Burst Time: ");
            int burstTime = scanner.nextInt();

            System.out.print("Enter Process Priority Number: ");
            int priority = scanner.nextInt();

            // Create a new Process object and add it to the array
            processes[i] = new Process(processName, color, i, arrivalTime, burstTime, priority);
        }

        PriorityScheduling priorityScheduling = new PriorityScheduling();
        priorityScheduling.schedule(processes,contextSwitching);






    }

}
