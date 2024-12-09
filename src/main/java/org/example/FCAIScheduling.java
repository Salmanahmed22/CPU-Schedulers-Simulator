package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Arrays;

public class FCAIScheduling {
    private  Process[] processes;
    private final int contextSwitching;
    private final double V1;
    private final double V2;

    public FCAIScheduling(Process[] processes, int contextSwitching) {
        this.processes = processes;
        this.contextSwitching = contextSwitching;

        // Calculate V1 and V2
        int lastArrivalTime = 0;
        int maxBurstTime = 0;
        for (Process process : processes) {
            lastArrivalTime = Math.max(lastArrivalTime, process.arrivalTime);
            maxBurstTime = Math.max(maxBurstTime, process.burstTime);
        }
        this.V1 = lastArrivalTime / 10.0;
        this.V2 = maxBurstTime / 10.0;

        // Initialize FCAI factors
        for (Process process : processes) {
            process.calculateFCAIFactor(V1, V2);
        }
    }

    public void schedule() {
        ArrayList<Process> readyQueue = new ArrayList<>();
        int currentTime = 0;
        // Custom comparator to sort based on FCAIFactor (ascending order)
        Comparator<Process> comparator = (p1, p2) -> Integer.compare(p1.FCAIFactor, p2.FCAIFactor);
        // Priority queue to hold Process objects
        PriorityQueue<Process> processPQ = new PriorityQueue<>(comparator);
        // Detailed Timeline Header
        System.out.println("Detailed Execution Timeline:");
        System.out.println("Time\tProcess\tRemaining Burst Time\tUpdated Quantum\tPriority\tFCAI Factor\tAction Details");

        while (true) {
            // Add processes to the ready queue based on arrival time
            Process[] newProcesses = new Process[processes.length];
            int index = 0; // Keeps track of the position in the new array

            for (Process process : processes) {
                if (process.arrivalTime <= currentTime && process.remainingTime > 0 && !readyQueue.contains(process)) {
                    readyQueue.add(process);
                    processPQ.add(process);
                    // Skip adding this process to the new array
                } else {
                    // Add the process to the new array if it's not being removed
                    newProcesses[index++] = process;
                }
            }

            // Trim the new array to the correct size
            processes = Arrays.copyOf(newProcesses, index);




            if (readyQueue.isEmpty()) {
                // If all processes are completed, break
                boolean allCompleted = true;
                for (Process process : processes) {
                    if (process.remainingTime > 0) {
                        allCompleted = false;
                        currentTime++;
                        break;
                    }
                }
                if (allCompleted) break;
                continue;
            }

            // Sort readyQueue by FCAI factor (descending)
            readyQueue.sort(Comparator.comparingDouble((Process p) -> p.FCAIFactor).reversed());



            Process currentProcess = readyQueue.get(0);
            int tempQuantum = currentProcess.quantum;
            readyQueue.remove(0);

            // Calculate the execution time and adjust quantum
            int executionTime = Math.min(currentProcess.quantum, currentProcess.remainingTime);
            if (executionTime > (currentProcess.quantum * 0.4)) {
                executionTime = (int) Math.ceil(currentProcess.quantum * 0.4); // Allow preemption after 40%
            }
            // Update the process state
            currentProcess.remainingTime -= executionTime;
            int remQuantum = currentProcess.quantum - executionTime;
            int startTime = currentTime;
            currentTime += executionTime;
//          if it still the lowest FCAI factor continue to execute it till the Quantum is over or till the next FCAI factor is smaller
            if(processPQ.peek().FCAIFactor == currentProcess.FCAIFactor){
                while(remQuantum > 0){
                    Process[] newProcesses2 = new Process[processes.length];
                    int index2 = 0; // Keeps track of the position in the new array

                    for (Process process : processes) {
                        if (process.arrivalTime <= currentTime && process.remainingTime > 0 && !readyQueue.contains(process)) {
                            readyQueue.add(process);
                            processPQ.add(process);
                            // Skip adding this process to the new array
                        } else {
                            // Add the process to the new array if it's not being removed
                            newProcesses2[index2++] = process;
                        }
                    }

                    // Trim the new array to the correct size
                    processes = Arrays.copyOf(newProcesses2, index2);

                    if(processPQ.peek().FCAIFactor == currentProcess.FCAIFactor) {
                        currentTime++;
                        currentProcess.remainingTime--;
                        remQuantum--;
                        executionTime++;
                    }
                }
            }
            // Before execution, update the quantum for this process
            if (remQuantum == 0) {
                currentProcess.quantum += 2; // Increase quantum for future executions

            } else {
                currentProcess.quantum += remQuantum ;
            }
            if(currentProcess.remainingTime > 0){
                currentProcess.calculateFCAIFactor(V1, V2);
                readyQueue.add(currentProcess);
            }

            // Print detailed information for the current time slice
            System.out.printf("%-15s%-25s%-30d%-20s%-15d%-15d%-25s\n",
                    startTime + "-" + (startTime + executionTime), // Current time range
                    currentProcess.processName,                  // Process name
                    currentProcess.remainingTime,               // Remaining Burst Time
                    tempQuantum + " → " + currentProcess.quantum,            // Simulate quantum change
                    currentProcess.priority,                    // Priority
                    currentProcess.FCAIFactor,                  // FCAI Factor
                    "Executing");                               // Action Details


            // Print the current action details after execution
//            if (currentProcess.remainingTime > 0) {
//                System.out.printf("Post Execution Update: %s has remaining burst time: %d\n",
//                        currentProcess.processName, currentProcess.remainingTime);
//            } else {
//                System.out.printf("Post Execution Update: %s completed at time %d\n",
//                        currentProcess.processName, currentTime);
//            }
        }

        // Print the final results after all processes are completed
        printResults();
    }

    private void printResults() {
        System.out.println("\nFCAI Scheduling Results:");
        System.out.println("Process\tArrival\tBurst\tPriority\tCompletion\tTurnAround\tWaiting");
        for (Process process : processes) {
            process.turnAroundTime = process.completionTime - process.arrivalTime;
            process.waitingTime = process.turnAroundTime - process.burstTime;

            System.out.println(process.processName + "\t" + process.arrivalTime + "\t" + process.burstTime + "\t" +
                    process.priority + "\t\t" + process.completionTime + "\t\t" +
                    process.turnAroundTime + "\t\t" + process.waitingTime);
        }
    }
}
