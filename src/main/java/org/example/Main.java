package org.example;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main{
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Hello into our CPU scheduler program: ");
        System.out.println("Press any number to choose one of the following: ");
        System.out.println("(1) Priority Scheduling: ");
        System.out.println("(2) SJF Scheduling: ");
        System.out.println("(3) SRTF Scheduling: ");
        System.out.println("(4) FCAI Scheduling: ");

        int choice = scanner.nextInt();
        System.out.println("Enter number of processes: ");
        int numberOfProcesses = scanner.nextInt();
        System.out.println("Enter context switching: ");
        int contextSwitching = scanner.nextInt();

        Process [] processes = new Process[numberOfProcesses];
        // Input the process details
        for (int i = 0; i < numberOfProcesses; i++) {
            System.out.println("Enter details for Process " + (i + 1));
            System.out.print("Enter Process Name: ");
            String processName = scanner.next();
            System.out.print("Choose Process Color (for graphical representation): ");
            System.out.print("-red\n-blue\n-green\n-yellow\n-orange\n-purple\n-black\n-magenta\n-cyan\n");
            String color = scanner.next();
            color.toLowerCase();
            System.out.print("Enter Process Arrival Time: ");
            int arrivalTime = scanner.nextInt();
            System.out.print("Enter Round Robin Time Quantum: ");
            int RRQuantum = scanner.nextInt();

            System.out.print("Enter Process Burst Time: ");
            int burstTime = scanner.nextInt();
            System.out.print("Enter Process Priority Number: ");
            int priority = scanner.nextInt();
            System.out.print("\n\n");
            // Create a new Process object and add it to the array
            processes[i] = new Process(processName, color, i, arrivalTime, burstTime, priority,RRQuantum);
        }

        if (choice == 1) {
            PriorityScheduling priorityScheduling = new PriorityScheduling();
            priorityScheduling.schedule(processes, contextSwitching);

            new PrioritySchedulerGUI(
                    priorityScheduling.getProcessList(),
                    priorityScheduling.getAvgWaitTime(),
                    priorityScheduling.getAvgTurnaroundTime()
                    ,contextSwitching
            );
        } else if (choice == 2) {
            List<Process> processList = new ArrayList<>();
            for (Process process : processes) {
                processList.add(process);
            }

            SJFscheduling sjfscheduling = new SJFscheduling(processList);
            sjfscheduling.schedule();

            new SJFSchedulerGUI(
                    sjfscheduling.getProcessList(),
                    sjfscheduling.getAvgWaitingTime(),
                    sjfscheduling.getAvgTurnaroundTime()
            );
        }else if (choice == 3) {
            SRTFscheduling scheduler = new SRTFscheduling();
            scheduler.simulateSRTF(processes, contextSwitching);
            Process[] updatedProcesses = scheduler.getProcesses();
            SRTFscheduling.printProcessDetails(updatedProcesses);
            List<SRTFscheduling.TimelineEntry> timeline = scheduler.getTimeline();
            SRTFscheduling.printTimeline(timeline);
            new SRTFSchedulerGUI(
                    timeline,
                    processes,
                    scheduler.getAvgWaitTime(processes),
                    scheduler.getAvgTurnaroundTime(processes)
            );
        }else if (choice == 4) {

            FCAIScheduling fcaiScheduling = new FCAIScheduling(processes, contextSwitching);
            fcaiScheduling.schedule();

        }
    } 

}
