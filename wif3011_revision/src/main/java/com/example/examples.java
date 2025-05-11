package com.example;
import java.util.concurrent.Semaphore;

public class examples {
    private static final int MAX_GUESTS = 6;
    private static final Semaphore guestSemaphore = new Semaphore(MAX_GUESTS);
    private static final Object cleanerLock = new Object();
    private static int cleanerCount = 0;
    private static int guestCount = 0;

    static class Cleaner implements Runnable {
        private final String name;

        public Cleaner(String name) {
            this.name = name;
        }
        @Override
        public void run() {
            try {
                synchronized (cleanerLock) {
                    while (guestCount > 0) {
                        System.out.println(name + " is waiting because there are guests in the room");
                        cleanerLock.wait();
                    }
                    cleanerCount++;
                    System.out.println(name + " entered the room for cleaning");
                }
                // Simulate cleaning time
                Thread.sleep(2000);

                synchronized (cleanerLock) {
                    cleanerCount--;
                    System.out.println(name + " finished cleaning and left the room");
                    cleanerLock.notifyAll();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    static class Guest implements Runnable {
        private final String name;

        public Guest(String name) {
            this.name = name;
        }
        @Override
        public void run() {
            try {
                // Try to acquire guest permit
                guestSemaphore.acquire();

                synchronized (cleanerLock) {
                    while (cleanerCount > 0) {
                        System.out.println(name + " is waiting because cleaners are in the room");
                        cleanerLock.wait();
                    }
                    guestCount++;
                    System.out.println(name + " entered the room. Current guests: " + guestCount);
                }

                // Simulate guest staying in the room
                Thread.sleep(1000);

                synchronized (cleanerLock) {
                    guestCount--;
                    System.out.println(name + " left the room. Current guests: " + guestCount);
                    if (guestCount == 0) {
                        cleanerLock.notifyAll();
                    }
                }

                guestSemaphore.release();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        // Create and start cleaners
        for (int i = 1; i <= 3; i++) {
            new Thread(new Cleaner("Cleaner " + i)).start();
        }

        // Create and start guests
        for (int i = 1; i <= 10; i++) {
            new Thread(new Guest("Guest " + i)).start();
        }
    }
}
