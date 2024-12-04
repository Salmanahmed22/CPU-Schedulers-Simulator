package org.example;

public class Process{
    String processName;
    String color;
    int processId;
    int arrivalTime;
    int burstTime;
    int priority;

    Process(String processName,String color ,int processId , int arrivalTime ,int burstTime,int priority){
        this.processName = processName;
        this.color = color;
        this.processId = processId;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
    }

}


