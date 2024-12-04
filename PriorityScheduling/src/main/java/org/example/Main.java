package org.example;
import java.util.Comparator;
import java.util.Scanner;
import java.util.PriorityQueue;
class Process{
    int ProcessId;
    int BurstTime;
    int Priority;

    Process(int ProcessId , int BurstTime , int Priority){
        this.ProcessId = ProcessId;
        this.BurstTime = BurstTime;
        this.Priority = Priority;
    }
}

class PriorityScheduling{
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);

        // input number of processes
        System.out.print("Enter the number of processes: ");
        int numProcesses = scanner.nextInt();

        PriorityQueue<Process> processPriorityQueue = new PriorityQueue<>(Comparator.comparingInt(p -> p.Priority));

        // input process details
        for (int i = 1; i <= numProcesses; i++) {
            System.out.print("Enter Process Name ");
            String ProcessName = scanner.next();
                            
            System.out.println("Enter details for Process " + i + ":");
            System.out.print("Burst Time: ");
            int burstTime = scanner.nextInt();
            System.out.print("Priority (lower value = higher priority): ");
            int priority = scanner.nextInt();
            processPriorityQueue.add(new Process(i, burstTime, priority));
        }
    }

}


public class Main {

}