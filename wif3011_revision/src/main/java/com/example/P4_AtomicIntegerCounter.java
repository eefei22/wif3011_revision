package com.example;

/*Rewrite the shared-counter problem using AtomicInteger instead 
of synchronized or locks. Verify that after multiple threads 
increment the counter, you consistently get the correct result.
 */

import java.util.concurrent.atomic.*;
import java.util.ArrayList;

class AtomicCounter{

    AtomicInteger count = new AtomicInteger(0);
    
    public void increment() {
        count.incrementAndGet();
    }

    public int getCount(){
        return count.get();
    }
}

public class P4_AtomicIntegerCounter {
    public static void main (String[] args) throws InterruptedException {
        AtomicCounter counter = new AtomicCounter();
        Runnable task = () -> {
            for (int i=0; i<100000; i++){
                counter.increment();
            }
        };

        ArrayList<Thread> threads = new ArrayList<>();

        for (int i=0; i<5; i++){
            int threadID = i;
            
            Thread thread = new Thread(task);
            System.out.println("Threaed " + threadID + " is running.");
            
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads){
            thread.join();
        }
        
        System.out.println("Final Count: " + counter.getCount());
    }
    
}
