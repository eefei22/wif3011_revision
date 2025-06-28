package com.example;

import java.util.concurrent.locks.*;

class SharedPot {
    private int foodCount = 0;
    private final int potCapacity;
    final Lock lock;
    final Condition emptyPot;
    final Condition fullPot;
    private volatile boolean done = false;

    public SharedPot(int potCapacity){
        this.potCapacity = potCapacity;
        this.lock = new ReentrantLock(true);
        this.emptyPot = lock.newCondition();
        this.fullPot = lock.newCondition();
    }

    public void getFood(String savageName) throws InterruptedException{
        lock.lock();
        try {
            while (foodCount == 0 && !done){
                System.out.println("pot is empty, waking cook...");
                emptyPot.signal();
                fullPot.await();
            }
            if(done && foodCount ==0) return;

            foodCount--;
            System.out.println(savageName + " taken food. Remaining: " + foodCount);
        } finally {lock.unlock();}
    }

    public void refillPot() throws InterruptedException{
        lock.lock();
        try {
            while (foodCount > 0){
                emptyPot.await();
            }
            System.out.println("Cook is refilling the pot...");
            Thread.sleep(3000);
            foodCount = potCapacity;
            System.out.println("Pot refilled, " + foodCount + " portions. ");
            fullPot.signalAll();
        } finally {lock.unlock();}
    }

    public boolean isDone(){
        return done;
    }

    public void setDone(){
        done = true;
    }
}

class Cook implements Runnable {
    private final SharedPot pot;
    private final int maxMeals = 10;
    private int meals = 0;

    public Cook(SharedPot pot){
        this.pot = pot;
    }
    public void run(){
        try {
            while(meals<maxMeals){
                pot.refillPot();
                meals++;
            }
            System.out.println("Cook done serving");
        } catch (InterruptedException e){
            System.out.println("Cook Interrupted");
        }
    }
}

class Savage implements Runnable {
    private final SharedPot pot;
    private final String name;

    public Savage(SharedPot pot, String name){
        this.pot = pot;
        this.name = name;
    }
    public void run(){
        try {
            while(!pot.isDone()){
                pot.getFood(name);
                Thread.sleep((int)(Math.random()*500));
            }
            System.out.println("No more meals");
        } catch (InterruptedException e){
            System.out.println(" interrupted ");
        }
    }
}

public class SharedPotDining {
    public static void main(String[] args) throws InterruptedException {
        int numSavages = 5;
        int potCapacity = 3;

        SharedPot pot = new SharedPot(potCapacity);

        Thread[] savageThreads = new Thread[numSavages];
        for (int i = 0; i < numSavages; i++) {
            savageThreads[i] = new Thread(new Savage(pot, "Savage-" + (i + 1)));
            savageThreads[i].start();
        }

        Thread cookThread = new Thread(new Cook(pot), "Cook");
        cookThread.start();
        cookThread.join();
        pot.setDone(); 
        pot.lock.lock();

        try {
            pot.fullPot.signalAll();
        } finally {
            pot.lock.unlock();
        }

        for (Thread t : savageThreads) {
            t.join();
        }

        System.out.println("All done.");
    }
}
