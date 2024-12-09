package org.example;

public class Process {
    String processName;
    String color;
    int processId;
    int arrivalTime;
    int burstTime;
    int priority;
    int waitingTime = 0;
    int turnAroundTime = 0;
    int completionTime = 0;
    int remainingTime;
    int quantum;
    int FCAIFactor;

    // Constructor for FCAI Scheduling
    Process(String processName, String color, int processId, int arrivalTime, int burstTime, int priority) {
        this.processName = processName;
        this.color = color;
        this.processId = processId;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.remainingTime = burstTime;
        this.quantum = 4; // Default quantum, can be modified dynamically
    }

    Process(String processName, String color, int processId, int arrivalTime, int burstTime, int priority, int quantum) {
        this.processName = processName;
        this.color = color;
        this.processId = processId;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.remainingTime = burstTime;
        this.quantum = quantum;
    }

    public Process(int i, int arrivalTime, int burstTime) {
    }

    // Method to calculate the FCAI factor and ceil it
    public void calculateFCAIFactor(double V1, double V2) {
        double rawFactor = (10 - priority) + Math.ceil(arrivalTime / V1) + Math.ceil(remainingTime / V2);
        FCAIFactor = (int) Math.ceil(rawFactor);
    }

}