/* ATOMIC VARIABLES + WAIT/NOTIFY IMPLEMENTATION
 * Concurrent Programming Question: Multi-Threaded Ticket Booking System
    Scenario:
    You’re building a backend system for an online concert ticketing platform. 
    Multiple customers are trying to purchase tickets at the same time. 
    The system must ensure that:
        - No ticket is oversold.
        - Each purchase is valid and thread-safe.
        - The number of available tickets decreases accurately no matter 
        how many people book concurrently.

    Requirements:
    - There are 100 tickets available.
    - Simulate 20 customer threads — each trying to buy 1 to 5 tickets (random).
    - Each customer thread should:
        - Randomly decide how many tickets to buy (1 to 5).
        - Attempt to make the purchase.
        - Print whether the purchase was successful or failed.
    - After all threads complete, print:
        - Total tickets sold.
        - Remaining tickets.
 */

package com.example;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Practice2{

    static class TicketCounter{
        private AtomicInteger availableTickets = new AtomicInteger(100);
        private Object lock = new Object();

        public boolean buyTickets(String customerName, int ticketsRquest){
            synchronized(lock){
                while (ticketsRquest > availableTickets.get()){
                    System.out.println("Cutomer " + customerName + " purchsing " + availableTickets.get() + " tickets");
                    System.out.println("Purchase Failed (not enough tickets available)");
                    try {
                        lock.wait();
                    } catch (InterruptedException e){
                        Thread.currentThread().interrupt();
                        return false;
                    }
                }
                availableTickets.addAndGet(-ticketsRquest);
                System.out.println("Cutomer " + customerName + " purchsing " + availableTickets.get() + " tickets");
                System.out.println("Purchase Success!\n");

                lock.notifyAll();
                return true;
            }
        }
        public int getRemainingTickets(){
            return availableTickets.get();
        }
    }   
    static class Customer implements Runnable{
        private String customerName;
        private int ticketsRequest;
        private TicketCounter counter;

        public Customer(String customerName, TicketCounter counter){
            this.customerName = customerName;
            this.counter = counter;
            this.ticketsRequest = new Random().nextInt(5) + 1;
        }

        public void run(){
            try {
                Thread.sleep((long)(Math.random()*500)); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            counter.buyTickets(customerName, ticketsRequest);
        }
    }
    public static void main (String[] args) throws InterruptedException{
        TicketCounter counter = new TicketCounter();
        List<Thread> threadList = new ArrayList<>();

        for (int i=0; i<20; i++){
            Runnable r = new Customer("Customer " + i, counter);
            Thread t = new Thread(r);
            threadList.add(t);
            t.start();
        }
        for (Thread t : threadList){
            t.join();
        } 
        System.out.println("\nBooking complete.");
        System.out.println("Remaining Tickets Available: " + counter.getRemainingTickets());
    }
}
