
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Node<T> {
    private T value;
    private final Lock lock = new ReentrantLock();
    private final Condition valueChanged = lock.newCondition();

    public void setValue(T newValue) {
        lock.lock();
        try {
            value = newValue;
            valueChanged.signalAll();
        } finally {
            lock.unlock();
        }
    }
    public void executeOnValue(T desiredValue, Runnable task) {
        lock.lock();
        try {
            while (!desiredValue.equals(value)) {
                valueChanged.await();
            }
            task.run();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }
}

//ReentrantReadWriteLock
lock.readLock().lock();     // multiple threads can hold
lock.writeLock().lock();    // exclusive access

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrantReadWriteLockExample {
    private static final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private static String sharedData = "Initial";

    public static void readData(String readerName) {
        rwLock.readLock().lock();
        try {
            System.out.println(readerName + " is reading: " + sharedData);
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            rwLock.readLock().unlock();
        }
    }

    public static void writeData(String newValue, String writerName) {
        rwLock.writeLock().lock();
        try {
            System.out.println(writerName + " is writing: " + newValue);
            Thread.sleep(1000);
            sharedData = newValue;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    public static void main(String[] args) {
        Runnable reader1 = () -> readData("Reader-1");
        Runnable reader2 = () -> readData("Reader-2");
        Runnable writer = () -> writeData("Updated", "Writer");

        new Thread(reader1).start();
        new Thread(reader2).start();
        new Thread(writer).start();
    }
}




