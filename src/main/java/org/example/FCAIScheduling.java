package org.example;

import java.util.*;

public class FCAIScheduling {
    private  Process[] processes;
    private final int contextSwitching;
    private final double V1;
    private final double V2;

    Vector<Process> processesData = new Vector<>();

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

        for (Process process : processes) {
            processesData.add(process);
        }
    }

    public void schedule() {
        ArrayList<Process> readyQueue = new ArrayList<>();
        int currentTime = 0;
        // Custom comparator to sort based on FCAIFactor (ascending order)
        Comparator<Process> comparator = (p1, p2) -> Integer.compare(p1.FCAIFactor, p2.FCAIFactor);
        // Priority queue to hold Process objects
        PriorityQueue<Process> processPQ = new PriorityQueue<>(comparator);

        // Track waiting time and turnaround time
        int[] waitingTime = new int[processes.length];
        int[] turnaroundTime = new int[processes.length];


        // Detailed Timeline Header
        System.out.println("Detailed Execution Timeline:");
        System.out.println("Time\t\tProcess\t\t\t\tRemaining Burst Time\t\t\t\tUpdated Quantum\t\tPriority\t\tFCAI Factor");

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
//            readyQueue.sort(Comparator.comparingDouble((Process p) -> p.FCAIFactor).reversed());


            Process currentProcess = readyQueue.get(0);
            int tempQuantum = currentProcess.quantum;
            readyQueue.remove(0);

            // Calculate the execution time and adjust quantum
            int executionTime = Math.min(currentProcess.quantum, currentProcess.remainingTime);
            if (executionTime > (currentProcess.quantum * 0.4)) {
                executionTime = (int) Math.ceil(currentProcess.quantum * 0.4); // Allow preemption after 40%
            }

            // Update waiting time for all other processes in the queue
            for (Process process : readyQueue) {
                waitingTime[process.processId] += executionTime;
            }

            // Update the process state
            currentProcess.remainingTime -= executionTime;

            int startTime = currentTime;
            currentTime += executionTime;
            //turnaround
            if (currentProcess.remainingTime == 0) {
                turnaroundTime[currentProcess.processId] = currentTime - currentProcess.arrivalTime;
            }

            int remQuantum = currentProcess.quantum - executionTime;




            Process[] newProcesses3 = new Process[processes.length];
            int index3 = 0; // Keeps track of the position in the new array

            for (Process process : processes) {
                if (process.arrivalTime <= currentTime && process.remainingTime > 0 && !readyQueue.contains(process)) {
                    readyQueue.add(process);
                    processPQ.add(process);
                    // Skip adding this process to the new array
                } else {
                    // Add the process to the new array if it's not being removed
                    newProcesses3[index3++] = process;
                }
            }

            // Trim the new array to the correct size
            processes = Arrays.copyOf(newProcesses3, index3);

//          if it still the lowest FCAI factor continue to execute it till the Quantum is over or till the next FCAI factor is smaller
            if (processPQ.peek().FCAIFactor == currentProcess.FCAIFactor) {
                while (remQuantum > 0 && currentProcess.remainingTime > 0) {
                    Process[] newProcesses2 = new Process[processes.length];
                    int index2 = 0; // Keeps track of the position in the new array

                    //track waitingtime
                    for (Process process : readyQueue) {
                        waitingTime[process.processId]++;
                    }

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

                    assert processPQ.peek() != null;

                    if (processPQ.peek().FCAIFactor == currentProcess.FCAIFactor) {
                        currentTime++;
                        currentProcess.remainingTime--;
                        remQuantum--;
                        executionTime++;
                    } else {
                        break;
                    }
                }
            } else {
                ArrayList<Process> tempQueue = new ArrayList<>();
                Process newProcess = processPQ.peek();
                tempQueue.add(newProcess);
                for (int i = 0; i < readyQueue.size(); i++) {
                    if (readyQueue.get(i) == newProcess) {
                        continue;
                    }
                    tempQueue.add(readyQueue.get(i));
                }
                readyQueue = tempQueue;
            }
            // Before execution, update the quantum for this process
            if (remQuantum == 0) {
                currentProcess.quantum += 2; // Increase quantum for future executions

            } else {
                currentProcess.quantum += remQuantum;
            }
            int tempFcaiFactor = currentProcess.FCAIFactor;
            if (currentProcess.remainingTime > 0) {
                currentProcess.calculateFCAIFactor(V1, V2);
                readyQueue.add(currentProcess);
            } else {
                processPQ.remove(currentProcess);
                currentProcess.remainingTime = 0;
            }

            // Print detailed information for the current time slice
            System.out.printf("%-15s%-25s%-30d%-20s%-15d%-15s\n",
                    startTime + "-" + (startTime + executionTime), // Current time range
                    currentProcess.processName,                  // Process name
                    currentProcess.remainingTime,               // Remaining Burst Time
                    tempQuantum + " → " + currentProcess.quantum,            // Simulate quantum change
                    currentProcess.priority,
                    tempFcaiFactor + " → " +// Priority
                            currentProcess.FCAIFactor);                  // FCAI Factor
            // Check if all processes are completed
        }
        // Print waiting time and turnaround time
        printStatistics(waitingTime, turnaroundTime);
    }
    private void printStatistics(int[] waitingTime, int[] turnaroundTime) {
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;

        System.out.printf("\n---------------------------------------------\n");

        System.out.println("\nProcess\t\tWaiting Time\tTurnaround Time");
        for (Process process : processesData) {
            totalWaitingTime += waitingTime[process.processId];
            totalTurnaroundTime += turnaroundTime[process.processId];
            System.out.printf("%-10s\t%-15d\t%-15d\n",
                    process.processName, waitingTime[process.processId], turnaroundTime[process.processId]);
        }

        System.out.printf("\n---------------------------------------------\n");

        double avgWaitingTime = (double) totalWaitingTime / processesData.size();
        double avgTurnaroundTime = (double) totalTurnaroundTime / processesData.size();

        System.out.printf("\nAverage Waiting Time: %.2f\n", avgWaitingTime);
        System.out.printf("Average Turnaround Time: %.2f\n", avgTurnaroundTime);
    }
}



