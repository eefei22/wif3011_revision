package com.example;

import java.util.concurrent.*;

public class P5a_BlockingQueue_MultiPC {
    public static void main(String[] args) throws InterruptedException{
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);
        int Num_Pro = 3, Num_Con = 4;
        int items_per_pro = 20;
        int POISON = -1;

        ExecutorService proPool = Executors.newFixedThreadPool(Num_Pro);
        ExecutorService conPool = Executors.newFixedThreadPool(Num_Con);

        for (int p=0; p<Num_Pro; p++){
            final int pid = p;
            proPool.submit(() -> {
                try {
                    for (int i=1; i<=items_per_pro; i++){
                        queue.put(pid *100 + i);
                        System.out.println("Producer " + pid + " put " + (pid*100 + i));
                    }
                    queue.put(POISON);
                } catch (InterruptedException e){
                    Thread.currentThread().interrupt();
                }
            });
        }
        proPool.shutdown();

        for (int c=0; c<Num_Con; c++){
            conPool.submit(() -> {
                try {
                    int val;
                    while ((val = queue.take()) != POISON) {
                        System.out.println("Consumer " + Thread.currentThread().getName() + " took " + val);
                    }
                    queue.put(POISON);
                } catch (InterruptedException e ){
                    Thread.currentThread().interrupt();
                }
            });
        }

        proPool.awaitTermination(1, TimeUnit.MINUTES);
        conPool.shutdown();
        conPool.awaitTermination(1, TimeUnit.MINUTES);

        System.out.println("All Finished");
    }
    
}
