package com.example;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class P8b_RW_ThreadPool {
    private static int sharedNum = 0;
    private static ReadWriteLock lock = new ReentrantReadWriteLock();
    private static ExecutorService pool = Executors.newFixedThreadPool(6);
    private static List<Runnable> tasks = new ArrayList<>();

    public static void readTask(int readerID){
        try {
            Thread.sleep(2000);
            lock.readLock().lock();
            System.out.println("Reader: " + readerID + "\t Reads: " + sharedNum);
        } 
        catch (InterruptedException e) {Thread.currentThread().interrupt();}
        finally {lock.readLock().unlock();}
    }
    public static void writeTask(int writerID){
        Random rand = new Random();
        try{
            Thread.sleep(3000);
            lock.writeLock().lock();
            sharedNum = rand.nextInt(1, 100);
            System.out.println("Writer: " + writerID + "\t Writing...");
            Thread.sleep(2000);
            System.out.println(" \nNew value: " + sharedNum);
        }
        catch (InterruptedException e) {Thread.currentThread().interrupt();}
        finally {lock.writeLock().unlock();}
    }
    public static void main (String[] args) throws InterruptedException{
        Random rand = new Random();
       for (int i=0; i<3; i++){
            for (int j=0; j<3; j++){
                int r_id = i;
                tasks.add(() -> readTask(r_id));
            }
        }
        for (int i=0; i<3; i++){
            for (int j=0; j<3; j++){
                int w_id = i;
                tasks.add(() -> writeTask(w_id));
            }
        }
        while (!tasks.isEmpty()){
            int index = rand.nextInt(tasks.size());
            Runnable task = tasks.remove(index);
            pool.submit(task);
        }
        pool.shutdown();
    }
}
