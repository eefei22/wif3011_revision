package com.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.*;
import java.util.*;

public class P9b_FuturesBulk2 {
    private static final int pool_size = 4;
    private static final ExecutorService pool = Executors.newFixedThreadPool(pool_size);

    public static class Calculator implements Callable<Integer>{
        private final int x;
        
        public Calculator(int x){ this.x = x;}
        public Integer call() throws Exception{
            Thread.sleep(500);
            return x*x; 
        }
    }

    public List<Future<Integer>> submitTasks(List<Integer> input){
        List<Future<Integer>> tasks = new ArrayList<>();
        for (int n : input){
            tasks.add(pool.submit(new Calculator(n)));
        }
        return tasks;
    }
    public void shutdown(){
        pool.shutdown();
    }

    public static void main (String[] args) throws InterruptedException {
        P9b_FuturesBulk2 calc = new P9b_FuturesBulk2();
        List<Integer> numbers = Arrays.asList(2, 4, 6, 8, 10, 12, 14, 16, 18, 20);
        List<Future<Integer>> submission = calc.submitTasks(numbers);
        for (int i=0; i<submission.size(); i++){
            Future<Integer> result = submission.get(i);
            try {
                System.out.println(numbers.get(i) + " squared = " + result.get());
            }
            catch (ExecutionException e){
                System.err.println(e.getMessage());
            }
        }
        calc.shutdown();

    }
}
