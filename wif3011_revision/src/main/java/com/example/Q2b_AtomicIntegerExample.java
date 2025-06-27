package com.example;
import java.util.concurrent.atomic.AtomicInteger;

// thread safe

public class Q2b_AtomicIntegerExample {
    private static AtomicInteger counter = new AtomicInteger(0);

    public static void main (String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i=0; i<10000; i++){
                counter.incrementAndGet();
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i=0; i<10000; i++){
                counter.incrementAndGet();
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("Final count: " + counter.get());
    }
}
