package com.example;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

class PrintJob{
    private final int jobID;
    private final String owner;

    public PrintJob(int jobID, String owner){
        this.jobID = jobID;
        this.owner = owner;
    }

    public String toString(){
        return "PrintJob{id=" + jobID + ",\towner: " + owner + " }";
    }
}

class PrintQueueManager{
    private final BlockingQueue<PrintJob> queue = new ArrayBlockingQueue<>(5);
    
    public void submitJob(PrintJob job) throws InterruptedException{
        System.out.println(job + "\t Submitting...");
        queue.put(job);
        System.out.println(job + "\t Submitted. ");
    }
    public PrintJob getNextJob() throws InterruptedException {
        PrintJob job = queue.take();
        System.out.println("\n Printer processing: " + job + "\n");
        return job;
    }
}

class Employee implements Runnable {
    private final PrintQueueManager manager;
    private final String employeeName;
    private static final AtomicInteger jobCounter = new AtomicInteger(1);

    public Employee (PrintQueueManager manager, String name){
        this.manager = manager;
        this.employeeName = name;
    }

    public void run(){
        try {
            for (int i=0; i<3; i++){
                PrintJob job = new PrintJob(jobCounter.getAndIncrement(), employeeName);
                manager.submitJob(job);
                Thread.sleep(5000);
            }
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }
}

class Printer implements Runnable {
    private final PrintQueueManager manager;
    
    public Printer(PrintQueueManager manager){
        this.manager = manager;
    }

    public void run(){
        try {
            while(true){
                PrintJob job = manager.getNextJob();
                Thread.sleep(1500);
            }
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }
}

public class P6_BlockingQueue {
    public static void main (String[] args){
        PrintQueueManager manager = new PrintQueueManager();
        Thread printerThreaed = new Thread(new Printer(manager), "Printer");
        printerThreaed.start();

        for (int i=1; i<=4; i++){
            Thread employee = new Thread(new Employee(manager, "Employee-" + i ));
            employee.start();
        }
    }
}
