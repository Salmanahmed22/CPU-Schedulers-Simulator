package org.example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class SJFscheduling {

    private List<Process> processes;
    private int avgWaitTime;
    private int avgTurnArroundTime;

    public SJFscheduling() {
        processes = new ArrayList<>();
    }

    public void addProcess(Process process) {
        processes.add(process);
    }

    public void sortProcesses() {
        processes.sort(Comparator.comparingInt(process -> process.burstTime));
    }

    public void calcWaitingTime(){
        processes.get(0).waitingTime = 0;
        for (int i = 1; i < processes.size(); i++) {
            processes.get(i).waitingTime += processes.get(i-1).burstTime;
        }
        calcAvgWaitTime();
    }

    public void calcAvgWaitTime() {
        int totalWaitTime = 0;
        for (int i = 1; i < processes.size(); i++) {
            totalWaitTime += processes.get(i).waitingTime;
        }
        avgWaitTime = totalWaitTime / processes.size();
    }

    public void calcTurnArroundTime() {
        for (Process process : processes) {
            process.turnArroundTime = process.waitingTime + process.burstTime;
        }
        calcAvgTurnArroundTime();
    }

    public void calcAvgTurnArroundTime() {
        int totalWaitTime = 0;
        for (Process process : processes) {
            totalWaitTime += process.turnArroundTime;
        }
        avgTurnArroundTime = totalWaitTime / processes.size();
    }

    public void displayProcesses() {

        System.out.println("Using SJF scheduling technique these are the results");
        System.out.println("Time    process     waiting time     turn around time");
        for (Process process : processes) {
            int startTime = process.waitingTime , endTime = startTime + process.burstTime;
            System.out.println(startTime + "-" + endTime +"\t\t\t" + process.processName + "\t\t\t" + process.waitingTime+ "\t\t\t" + process.turnArroundTime);
        }

        System.out.println("Average waiting time: " + avgWaitTime);
        System.out.println("Average turn around time: " + avgTurnArroundTime);
    }

}

