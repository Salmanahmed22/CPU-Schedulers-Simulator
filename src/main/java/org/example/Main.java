package org.example;
import java.util.Scanner;


public class Main{
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SJFscheduling sjfscheduling = new SJFscheduling();
        System.out.println("Hello into our CPU scheduler program: ");
        System.out.println("Press any number to choose one of the following: ");
        System.out.println("(1) Priority Scheduling: ");
        System.out.println("(2) SJF Scheduling: ");
        System.out.println("(3) SRTF Scheduling: ");
        System.out.println("(4) FCAI Scheduling: ");
        int choice = scanner.nextInt();
        System.out.println("Enter number of processes: ");
        int numberOfProcesses = scanner.nextInt();
        for (int i = 0; i < numberOfProcesses; i++) {
            String processName , processColor;
            int burstTime;
            System.out.println("Enter process " + (i + 1) + " data: ");
            System.out.print("Name: ");
            processName = scanner.next();
            System.out.print("Color: ");
            processColor = scanner.next();
            System.out.print("Burst time: ");
            burstTime = scanner.nextInt();
            sjfscheduling.addProcess(new Process(processName,processColor,burstTime));
        }

        if (choice == 1) {

        } else if (choice == 2) {

            sjfscheduling.sortProcesses();
            sjfscheduling.calcWaitingTime();
            sjfscheduling.calcTurnArroundTime();
            sjfscheduling.displayProcesses();
        }else if (choice == 3) {

        }else if (choice == 4) {

        }
    }
}
