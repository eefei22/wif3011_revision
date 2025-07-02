package com.example;

import java.util.concurrent.*;
import java.util.concurrent.locks.*;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class P8a_RW_ThreadPool {
    private static int sharedNum = 0;
    private static final ReadWriteLock rwLock = new ReentrantReadWriteLock(true);
    private static final ExecutorService pool = Executors.newFixedThreadPool(6); 
    private static List<Runnable> tasks = new ArrayList<>();

    public static void main (String[] args) throws InterruptedException{
        for (int i=0; i<3; i++){
            int r_id = i;
            tasks.add(() -> readTask(r_id));
        }
        for (int i=0; i<3; i++){
            int w_id = i;
            tasks.add(() -> writeTask(w_id));
        }
        Random rand = new Random();
        while (!tasks.isEmpty()){
            int index = rand.nextInt(tasks.size());
            Runnable task = tasks.remove(index);
            pool.submit(task);
        }

        pool.shutdown();
    }
    public static void readTask(int readerId){
        for (int i=0; i<3; i++){
            try {
                Thread.sleep(4000);
                rwLock.readLock().lock();
                System.out.println("Reader: " + readerId + "\t Read value: " + sharedNum);
                Thread.sleep(4000); 
            } 
            catch (InterruptedException e) {Thread.currentThread().interrupt();}
            finally {rwLock.readLock().unlock();}
        }
    }
    public static void writeTask(int writerId){
        Random rand = new Random();
        for (int i=0; i<3; i++){
            try {
                Thread.sleep(4000);
                rwLock.writeLock().lock();
                sharedNum = rand.nextInt(1, 100);
                System.out.println("Writer " + writerId + "\t Writing...");
                System.out.println(" \nNew Value: " + sharedNum);
                Thread.sleep(4000);
            }
            catch (InterruptedException e) {Thread.currentThread().interrupt();}
            finally {
                rwLock.writeLock().unlock(); 
            }
        }
    }
}
