package com.example;

import java.util.concurrent.locks.ReentrantLock;

/* Task:    Build two threads competing for the same lock. Each thread should:
            Attempt to acquire the lock with a timeout (e.g., 500 ms).
*/

public class P3a_ReentrantLock2 {

    static ReentrantLock lock = new ReentrantLock();

    static Runnable task = () -> {
        while (true) {
            if (lock.tryLock()){
                try{
                    System.out.println(Thread.currentThread().getName() + " acquired lock, doing work");
                    try {Thread.sleep(5000);} catch (InterruptedException e)
                        {Thread.currentThread().interrupt();
                        break;
                        }
                } finally {
                    lock.unlock();
                }
            } else {
                System.out.println(Thread.currentThread().getName() + " did not acquire lock, doing other work");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e){
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    };

    public static void main (String[] args) throws InterruptedException{
        Thread t1 = new Thread(task, "worker-1");
        Thread t2 = new Thread(task, "worker-2:");

        t1. start();
        t2. start();

        t1.join();
        t2.join();

    }
    
}
