package com.example;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

class PrintJob {
    private final int jobID;
    private final String owner;

    public PrintJob(int jobID, String owner){
        this.jobID = jobID;
        this.owner = owner;
    }
    public String jobLog(){
        return "Print Job: " + jobID + "\t Owner: " + owner + " - ";
    }
}

class PrintQueue {
    private BlockingQueue<PrintJob> queue  = new ArrayBlockingQueue<>(5);

    public void addToQueue(PrintJob job) throws InterruptedException{
        queue.put(job);
        System.out.println(job.jobLog() + "Submitted");
    }
    public PrintJob getFromQueue() throws InterruptedException{
        PrintJob job = queue.take();
        System.out.println(job.jobLog() + "Printed");
        return job;
    }
}

class PrinterUser implements Runnable {
    private final PrintQueue queue;
    private final String userName;
    private final AtomicInteger printCount = new AtomicInteger(1);

    public PrinterUser(PrintQueue queue, String userName){
        this.queue = queue;
        this.userName = userName;
    }
    public void run(){
        try {
            for (int i=0; i<3; i++){
                PrintJob job = new PrintJob(printCount.incrementAndGet(), userName);
                queue.addToQueue(job);
                Thread.sleep(5000);
            }
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }
}

class Printer implements Runnable{
    private final PrintQueue queue;

    public Printer(PrintQueue queue){
        this.queue = queue;
    }
    public void run(){
        try {
            while (true){
                PrintJob job = queue.getFromQueue();
                Thread.sleep(3000);                
            }
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        } 
    }
}
public class P6a_BlockingQueue {
    public static void main(String[] args) throws InterruptedException{
        PrintQueue queue = new PrintQueue();
        
        Thread printerThread = new Thread(new Printer(queue), "Printer");
        printerThread.start();

        for (int i=1; i<=5; i++){
            Thread userThread = new Thread(new PrinterUser(queue, "User-" + i));
            userThread.start();
            userThread.join();
        }

        printerThread.join();
    }  
}
