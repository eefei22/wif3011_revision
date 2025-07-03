package com.example;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import java.util.Random;

public class P8b_RW_ThreadPool {
    private static int sharedNum = 0;
    private static final ReadWriteLock lock = new ReentrantReadWriteLock(true);
    private static ExecutorService pool = Executors.newFixedThreadPool(6);
    private static List<Runnable> tasks = new ArrayList<>();

    public static void readersTask(int r_id){
        try {
            Thread.sleep(2000);
            lock.readLock().lock();
            System.out.println("Reader: " + r_id + "\tReads: " + sharedNum);
        }
        catch (InterruptedException e) {Thread.currentThread().interrupt();}
        finally {lock.readLock().unlock();}
    }
    public static void writersTask(int w_id){
        Random rand = new Random();
        try {
            Thread.sleep(4000);
            lock.writeLock().lock();
            sharedNum = rand.nextInt(1, 100);
            System.out.println("Writer: " + w_id + "\tUpdating number...");
            Thread.sleep(2000);
            System.out.println("\nWriting done, new value: " + sharedNum);
        }
        catch (InterruptedException e){Thread.currentThread().interrupt();}
        finally {lock.writeLock().unlock();}
    }

    public static void main (String[] args) throws InterruptedException{
        Random rand = new Random();
        for (int i=0; i<3; i++){
            for (int j=0; j<3; j++){
                int r_id = i;
                tasks.add(() -> readersTask(r_id));
            }
        }
        for (int i=0; i<3; i++){
            for (int j=0; j<3; j++){
                int w_id = i;
                tasks.add(() -> writersTask(w_id));
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
