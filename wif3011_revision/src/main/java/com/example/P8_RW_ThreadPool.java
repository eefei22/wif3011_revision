package com.example;

import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class P8_RW_ThreadPool {
    private static int sharedConfig = 0;
    private static final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock(true); 
    private static final ExecutorService executor = Executors.newFixedThreadPool(6);

    public static void main(String[] args) {
        for (int i = 1; i <= 3; i++) {
            int readerId = i;
            executor.submit(() -> readerTask(readerId));
        }
        for (int i = 1; i <= 3; i++) {
            int writerId = i;
            executor.submit(() -> writerTask(writerId));
        }
        executor.shutdown();
    }

    private static void readerTask(int id) {
        for (int i = 0; i < 3; i++) { 
            try {
                Thread.sleep(3000); 
                rwLock.readLock().lock();
                System.out.printf("Reader %d is READING value: %d%n", id, sharedConfig);
                Thread.sleep(300); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                rwLock.readLock().unlock();
                System.out.printf("Reader %d finished reading.%n", id);
            }
        }
    }

    private static void writerTask(int id) {
        for (int i = 0; i < 3; i++) { 
            try {
                Thread.sleep(3000); 
                rwLock.writeLock().lock();
                sharedConfig++;
                System.out.printf("Writer %d is WRITING new value: %d%n", id, sharedConfig);
                Thread.sleep(500); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                rwLock.writeLock().unlock();
                System.out.printf("Writer %d finished writing.%n", id);
            }
        }
    }
}