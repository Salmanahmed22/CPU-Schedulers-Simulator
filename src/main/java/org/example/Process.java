package org.example;

public class Process{
    String processName;
    String color;
    int processId;
    int arrivalTime;
    int burstTime;
    int priority;
    int waitingTime = 0;
    int turnArroundTime = 0;

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

}


