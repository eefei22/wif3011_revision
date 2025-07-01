package com.example;

/* Task:    Build two threads competing for the same lock. Each thread should:
            Attempt to acquire the lock with a timeout (e.g., 500 ms).
If acquired:
    Enter the critical section, sleep briefly to simulate work, then release the lock.
    If the lock isn’t acquired within timeout:
    Log a timeout message and retry after a short pause.
*/

import java.util.concurrent.locks.*;
import java.util.concurrent.*;

class Worker implements Runnable{
    private ReentrantLock lock;
    private String name;

    public Worker(ReentrantLock lock, String name){
        this.lock = lock;
        this.name = name;
    }

    public void run(){
        while (!Thread.currentThread().isInterrupted()){
            try {
                System.out.println(name + "trying to acquire lock");
                if (lock.tryLock(7000, TimeUnit.MICROSECONDS)){
                    try{
                    System.out.println(name + "acquired the lock");
                    Thread.sleep(5000);
                    } finally {
                        System.out.println(name + "releasing the lock");
                        lock.unlock();
                    }
                    break;
                } else {
                    System.out.println(name + "couldn't get lock");
                }
            } catch (InterruptedException e){
                System.out.println(name + "was interrupted");
                Thread.currentThread().interrupt();
            }
        }
    }
}
public class P3_ReentrantLock {
    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        
        Thread t1 = new Thread(new Worker(lock, "Thread 1 "));
        Thread t2 = new Thread(new Worker(lock, "Thread 2 "));

        t1.start();
        t2.start();

        t1.join();
        t2.join();
        System.out.println("Finish");
    }    
}

