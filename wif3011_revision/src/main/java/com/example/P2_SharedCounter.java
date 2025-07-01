package com.example;

// Threads are incrementing a shared counter. 
// Use synchronized to ensure only one thread can execute increment() method at a time
// Prevents race condition.
// Create a class with multiple threads incrementing a shared int counter 10,000 times each.

public class P2_SharedCounter {
    public static void main(String[] args) throws InterruptedException{
        Counter c1 = new Counter();
        Runnable task = () -> {
            for (int i = 1; i <= 10000; i ++){
                c1.increment();
            }
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("Final count: " + c1.getCount());

    }
}


class Counter {
    private int count = 0;

    public synchronized void increment(){
        count++;
    }

    public synchronized int getCount(){
        return count;
    }
}