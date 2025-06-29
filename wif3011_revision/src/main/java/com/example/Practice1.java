/* SYNCHRONIZED METHODS IMPLEMENTATION
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
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class Practice1 {

    static class TicketCounter{
        private int availableTickets = 100;

        public synchronized boolean buyTickets(int ticketsRequest){
            if(availableTickets > ticketsRequest){
                availableTickets -= ticketsRequest;
                return true;
            } else {
                return false;
            }
        }
        public int getRemainingTickets(){
            return availableTickets;
        }
    }

    static class Customer implements Runnable{
        private TicketCounter counter;
        private String customerName;
        private int ticketsRequest;

        public Customer(TicketCounter counter, String customerName){
            this.counter = counter;
            this.customerName = customerName;
            this.ticketsRequest = new Random().nextInt(5) + 1;
        }
        @Override
        public void run(){
            boolean success = counter.buyTickets(ticketsRequest);
            synchronized(System.out){
                System.out.println("Customer " + customerName + " -- Purchased " + ticketsRequest + " ticket");
                System.out.println("Ticket Purchase: " + (success ? "Sucess" : "Failed (not enough tickets available)"));
            }
        }
    }

    public static void main (String[] args) throws InterruptedException{
        TicketCounter counter = new TicketCounter();
        List<Thread> threadList = new ArrayList<>();

        for (int i=0; i<20; i++){
            Runnable r = new Customer(counter, "Customer " +i);
            Thread t = new Thread(r);
            threadList.add(t);
            t.start();
        }
        for (Thread t : threadList){
            t.join();
        }

        System.out.println("Remaining Tickets: " + counter.getRemainingTickets());
    }
}
