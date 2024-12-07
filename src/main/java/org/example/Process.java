package org.example;

public class Process{
    String processName;
    String color;
    int processId;
    int arrivalTime;
    int burstTime;
    int priority;
    int waitingTime = 0;
    int turnAroundTime = 0;
    int completionTime = 0;
    int remainingTime = 0;

    //constructor for Priority
    Process(String processName,String color ,int processId , int arrivalTime ,int burstTime,int priority){
        this.processName = processName;
        this.color = color;
        this.processId = processId;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
    }

    //constructor for SJF
    Process(String processName,String color,int burstTime){
        this.processName = processName;
        this.color = color;
        this.burstTime = burstTime;
    }

    //constructor for SRTF
    Process(int pid, int arrivalTime, int burstTime) {
        this.processId = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.waitingTime = 0;
        this.turnAroundTime = 0;
        this.completionTime = 0;
    }

}


