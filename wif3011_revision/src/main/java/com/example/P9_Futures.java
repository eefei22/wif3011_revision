package com.example;

import java.util.concurrent.*;

//square calculator program

public class P9_Futures { 
    private static ExecutorService pool = Executors.newFixedThreadPool(2);

    public Future<Integer> square(int value){
        return pool.submit(() -> {
            Thread.sleep(2000);
            return value * value;
        });
    }
    public void shutdown(){
        pool.shutdown();
    }

    public static void main (String[] args) throws InterruptedException {
        P9_Futures cal = new P9_Futures();
        Future<Integer> f1 = cal.square(20);
        Future<Integer> f2 = cal.square(40);

        try {
            System.out.println("Getting the damn results...");
            int r1 = f1.get(3, TimeUnit.SECONDS);
            int r2 = f2.get(3, TimeUnit.SECONDS);
            System.out.println("Results: " + r1 + " and " + r2);
        }
        catch (TimeoutException e) {System.err.println("Timeout");}
        catch (ExecutionException e) {e.printStackTrace();}

        finally {cal.shutdown();}
    }
}
