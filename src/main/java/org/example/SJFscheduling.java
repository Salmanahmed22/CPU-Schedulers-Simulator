package org.example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SJFscheduling {

    private List<Process> processList;
    private int avgWaitTime;
    private int avgTurnArroundTime;


    public SJFscheduling(Process [] process) {
        this.processList = new ArrayList<>();
        for (Process p : process) {
            processList.add(p);
        }
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
            process.turnArroundTime = process.waitingTime + process.burstTime;
        }
        calcAvgTurnArroundTime();
    }

    public void calcAvgTurnArroundTime() {
        int totalWaitTime = 0;
        for (Process process : processList) {
            totalWaitTime += process.turnArroundTime;
        }
        avgTurnArroundTime = totalWaitTime / processList.size();
    }

    public void displayProcesses() {

        System.out.println("Using SJF scheduling technique these are the results");
        System.out.println("Time    process     waiting time     turn around time");
        for (Process process : processList) {
            int startTime = process.waitingTime , endTime = startTime + process.burstTime;
            System.out.println(startTime + "-" + endTime +"\t\t\t" + process.processName + "\t\t\t" + process.waitingTime+ "\t\t\t" + process.turnArroundTime);
        }

        System.out.println("Average waiting time: " + avgWaitTime);
        System.out.println("Average turn around time: " + avgTurnArroundTime);
    }

}

