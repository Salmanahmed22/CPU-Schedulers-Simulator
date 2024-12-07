package org.example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SJFscheduling {

    private List<Process> processList;
    private int avgWaitTime;
    private int avgTurnAroundTime;


    public SJFscheduling(Process [] process) {
        this.processList = new ArrayList<>();
        for (Process p : process) {
            processList.add(p);
        }
    }

    public List<Process> getProcessList() {
        return processList;
    }

    public int getAvgWaitTime() {
        return avgWaitTime;
    }

    public int getAvgTurnAroundTime() {
        return avgTurnAroundTime;
    }


    public void addProcess(Process process) {
        processList.add(process);
    }

    public void sortProcesses() {
        processList.sort(Comparator.comparingInt(process -> process.burstTime));
    }

    public void calcWaitingTime(){
        processList.get(0).waitingTime = 0;
        for (int i = 1; i < processList.size(); i++) {
            processList.get(i).waitingTime += processList.get(i-1).burstTime;
        }
        calcAvgWaitTime();
    }

    public void calcAvgWaitTime() {
        int totalWaitTime = 0;
        for (int i = 1; i < processList.size(); i++) {
            totalWaitTime += processList.get(i).waitingTime;
        }
        avgWaitTime = totalWaitTime / processList.size();
    }

    public void calcTurnArroundTime() {
        for (Process process : processList) {
            process.turnAroundTime = process.waitingTime + process.burstTime;
        }
        calcAvgTurnArroundTime();
    }

    public void calcAvgTurnArroundTime() {
        int totalWaitTime = 0;
        for (Process process : processList) {
            totalWaitTime += process.turnAroundTime;
        }
        avgTurnAroundTime = totalWaitTime / processList.size();
    }

    public void displayProcesses() {
        System.out.println("\n=== SJF Scheduling Results ===");
        System.out.println("-------------------------------------------------------------");
        System.out.printf("%-15s %-15s %-20s %-20s\n", "Time", "Process", "Waiting Time", "Turnaround Time");
        System.out.println("-------------------------------------------------------------");

        for (Process process : processList) {
            int startTime = process.waitingTime, endTime = startTime + process.burstTime;
            System.out.printf("%-15s %-15s %-20d %-20d\n",
                    startTime + "-" + endTime,
                    process.processName,
                    process.waitingTime,
                    process.turnAroundTime);
        }

        System.out.println("-------------------------------------------------------------");
        System.out.printf("%-30s: %d\n", "Average Waiting Time", (int) avgWaitTime);
        System.out.printf("%-30s: %d\n", "Average Turnaround Time", (int) avgTurnAroundTime);
        System.out.println("=============================================================\n");
    }



}

