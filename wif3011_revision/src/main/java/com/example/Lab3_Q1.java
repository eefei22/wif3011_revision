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
            
        }
    }
}