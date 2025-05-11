/*  Write a Java program to simulate a room cleaning scenario using 
    thread synchronization. The rules are as follows:
    -   Cleaners can enter the room only when no guests are present, 
        and only one cleaner is allowed in the room at a time. 
        If the room is occupied (by any guests), a cleaner must wait until 
        it becomes available.
    -   Guests can enter the room only when no cleaner is inside, and up to 6 
        guests can be in the room simultaneously. If a cleaner is inside or if 
        the room already has 6 guests, a guest must wait before entering.
    Implement this simulation using Java’s built-in synchronization 
    constructs (synchronized, wait(), and notifyAll() or notify()). 
*/

package com.example;
import java.util.concurrent.Semaphore;

public class Lab3_Q1 {
    private static final int max_guest = 6;
    private static final Semaphore guestSemaphore = new Semaphore (max_guest);
    private static final Object cleanerLock = new Object();
    private static int cleanerCount = 0;
    private static int guestCount = 0;

    static class Cleaner implements Runnable {
        private final String name;
        
        public Cleaner(String name){
            this.name = name;
        }

        @Override
        public void run() {
            try {
                synchronized (cleanerLock){
                    while (guestCount > 0){
                        System.out.println("guests are in the room, " + name + " will wait");
                        cleanerLock.wait();
                    }
                    cleanerCount++;
                    System.out.println(name + " entered the room for cleaning");
                }
                Thread.sleep(2000);

                synchronized (cleanerLock){
                    cleanerCount --;
                    System.out.println(name + " finished cleaning and left the room");
                    cleanerLock.notifyAll();
                }
            } catch (InterruptedException e){Thread.currentThread().interrupt();}
        }
    }

    static class Guest implements Runnable{
        private final String name;

        public Guest(String name) {
            this.name = name;
        }
        @Override
        public void run(){
            try {
                guestSemaphore.acquire();
                synchronized (cleanerLock){
                    while (cleanerCount>0){
                        System.out.println("cleaners are in the room " + name + " will wait");
                        cleanerLock.wait();
                    }
                    guestCount++;
                    System.out.println(name + " entered the room. Current guests: " + guestCount);
                }
                Thread.sleep(1000);

                synchronized (cleanerLock){
                    guestCount--;
                    System.out.println(name + " left the room. Current guests: " + guestCount);
                    if (guestCount == 0){
                        cleanerLock.notifyAll();
                    }
                }
                guestSemaphore.release();
            } catch (InterruptedException e){Thread.currentThread();}
        }
    }
    public static void main(String[] args){
        for (int i=1; i<=3; i++){
            new Thread(new Cleaner("Cleaner " + i)).start();
        }
        for (int i=1; i<11; i++){
            new Thread(new Guest("Guest " + i)).start();
        }
    }
}