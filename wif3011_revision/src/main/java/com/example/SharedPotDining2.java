package com.example;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.*;

class SharedPot {
    int mealCount = 0;
    int potCapacity;
    Lock lock;
    Condition emptyPot;
    Condition fullPot;
    private volatile boolean doneServing = false;
    private boolean refilling = false;

    public SharedPot(int potCapcity){
        this.potCapacity = potCapcity;
        this.lock = new ReentrantLock(true);
        this.emptyPot = lock.newCondition();
        this.fullPot = lock.newCondition();
    }

    public void getFood(String savager) throws InterruptedException{
        lock.lock();
        try {
            while (mealCount == 0 &&!doneServing){
                if (!refilling){
                    System.out.println(savager + ": pot empty, alert cook. ");
                    refilling = true;
                    emptyPot.signal();
                }
                fullPot.await();
            }
            if (doneServing && mealCount ==0) return;
            mealCount--;
            System.out.println(savager + " ate a meal. Remaining: " + mealCount);
        } finally { lock.unlock();}
    }

    public void refillPot() throws InterruptedException {
        lock.lock();
        try {
            while (mealCount > 0){
                emptyPot.await();
            }
            System.out.println("Cook is refilling the pot...");
            Thread.sleep(3000);
            mealCount = potCapacity;
            refilling = false;
            System.out.println("Pot refilled with " + mealCount + " portions.");
            fullPot.signalAll();
        
        } finally {lock.unlock();} 
    }

    public boolean isDone(){
        return doneServing;
    }

    public void setDone(){
        doneServing = true;
    }
}

class Savage implements Runnable {
    SharedPot pot;
    String savager;
    Semaphore eatingPermits;
    int mealsEaten = 0;

    public Savage(SharedPot pot, String savager, Semaphore eatingPermits){
        this.pot = pot;
        this.savager = savager;
        this.eatingPermits = eatingPermits;
    }

    public void run(){
        try {
            while(!pot.isDone()){
                eatingPermits.acquire();
                try {
                    pot.getFood(savager);
                    mealsEaten++;
                    Thread.sleep(5000);
                } finally {eatingPermits.release();}
            }
            System.out.println(savager + " ate " + mealsEaten + " meals ");
        } catch (InterruptedException e) {
            System.out.println(savager + " Interrupted. ");
        }
    }
}


class Cook implements Runnable {
    SharedPot pot;
    int maxMeals = 10;
    int meals = 0;

    public Cook(SharedPot pot){
        this.pot = pot;
    }
    public void run(){
        try {
            while (meals < maxMeals){
                pot.refillPot();
                meals++;
            }
            System.out.println("Cook done serving");    
        } catch (InterruptedException e){
            System.out.println("Cook Interrupted");
        }
    }
}


public class SharedPotDining2 {
    public static void main (String[] args) throws InterruptedException{

        int savages = 5;
        int potCapacity = 5;
        int permits = 4;

        SharedPot pot = new SharedPot(potCapacity);
        Semaphore eatingPermits = new Semaphore(permits);

        Thread[] savageThreads = new Thread[savages];
        for (int i=0; i<savages; i++){
            savageThreads[i] = new Thread(new Savage(pot, "Savage-"+(i+1), eatingPermits));
            savageThreads[i].start();
        }

        Thread cookThread = new Thread(new Cook(pot), "Cook");
        cookThread.start();
        cookThread.join();
        pot.setDone();
        pot.lock.lock();

        try {
            pot.fullPot.signalAll();
        } finally {pot.lock.unlock();}
        
        for (Thread t : savageThreads){
            t.join();
        }
        
        System.out.println("All Done.");
    }
}
