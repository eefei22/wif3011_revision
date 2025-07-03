package com.example;

import java.util.*;
import java.util.concurrent.*;

public class P9a_FuturesBulk {
    private static final int pool_size = 4;
    private static final ExecutorService pool = Executors.newFixedThreadPool(pool_size);

    static class Calculator implements Callable<Integer>{
        private int x;
        Calculator (int x) { this.x = x;}

        public Integer call() throws Exception{
            Thread.sleep(500);
            return x*x;
        }
    }
        
    public List<Future<Integer>> submitTasks(List<Integer> inputs){
        List<Future<Integer>> futures = new ArrayList<>();
        for ( int num : inputs){
            futures.add(pool.submit(new Calculator(num)));
        }
        return futures;
    }

    public void shutdown(){
        pool.shutdown();
    }
    
    public static void main (String[] args) throws InterruptedException{
        P9a_FuturesBulk cal = new P9a_FuturesBulk();
        List<Integer> numbers = Arrays.asList(2, 4, 6, 8, 10, 12, 14, 16, 18, 20);

        List<Future<Integer>> futures = cal.submitTasks(numbers);
        for (int i=0; i<numbers.size(); i++){
            Future<Integer> result = futures.get(i);
            try {
                System.out.println(numbers.get(i) + " squared = " + result.get());
            }
            catch (ExecutionException e){
                System.err.println("task for " + numbers.get(i) + " failed." + e.getMessage());
            }
        }
        cal.shutdown();
    }
}


