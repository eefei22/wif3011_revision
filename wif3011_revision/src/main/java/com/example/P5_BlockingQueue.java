package com.example;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class P5_BlockingQueue {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(4);
        int POISON = -1;

        Runnable producer = () -> {
            try {
                for (int i=1; i<=4; i++){
                    queue.put(i);
                    System.out.println("Produced " + i);
                }
                queue.put(POISON);
            }catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        Runnable consumer = () -> {
            try {
                int val;
                while ((val = queue.take()) != POISON){
                    System.out.println("Consumed " + val);
                }
                System.out.println("Consumer received poison -- exiting. ");
            } catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
        };

        Thread p = new Thread(producer, "Producer");
        Thread c = new Thread(consumer, "Consumer");

        p.start();
        c.start();
        p.join();
        c.join();

        System.out.println("Done");
    }
}
