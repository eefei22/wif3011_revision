/*
 * Use explicit locks to implement the program below.   
    Create a generic Node class. The class has an instance 
    variable value, an instance of Lock, and an instance of 
    Condition: valueChanged. Node has the following methods: 
    1.  setValue – assigns the received parameter to value and 
        notifies all waiting threads.   
    2.  executeOnValue – receives 2 parameters desiredValue and 
        task. If desiredValue equals value, task is executed; 
        or otherwise, it waits until the desiredValue is found.
*/

package com.example;
import java.util.Random;
import java.util.concurrent.locks.*;

class Node<T> {
    private T value;
    private final Lock lock = new ReentrantLock();
    private final Condition valueChanged = lock.newCondition();

    public void setValue(T newValue){
        lock.lock();
        try{
            value = newValue;
            valueChanged.signalAll();
        } finally {
            lock.unlock();
        }
    }
    public void executeOnValue(T desiredValue, Runnable task) {
        lock.lock();
        try{
            while (!desiredValue.equals(value)){
                valueChanged.await();
            }
            task.run();
        } catch (InterruptedException e){Thread.currentThread().interrupt();
        } finally {lock.unlock();}
    }
}

public class Lab4_Q1 {
    
}
