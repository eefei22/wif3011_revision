package com.example;

public class P1_BasicThreading {
    public static void main (String[] args) throws InterruptedException{
        Thread t1 = new Thread(new MyRunnable(), "t1");
        Thread t2 = new Thread(new MyRunnable(), "t2");
        t1.start();
        t2.start();
        
        t1.join();
        t2.join();

        System.out.println("Done");
    }
}

// Creating a thread using Runnable interface

class MyRunnable implements Runnable {
    public void run(){
        System.out.println("Hello from runnable " + Thread.currentThread().getName());
    }
}