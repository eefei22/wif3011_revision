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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class TicketBooking2 {
static class TicketCounter {                                                                            //A shared ticket inventory, allows customers to try booking
        private final AtomicInteger availableTickets = new AtomicInteger(100);             //Shared counter with atomic thread-safe updates
        private final Object lock = new Object();                                                       //Synchronization Monitor for wait/notify control

        public boolean tryBooking(String customerName, int ticketsRequest){                             //booking method
            synchronized(lock){                                                                         //wait() and notifyAll() must be used inside a synchronized block
                while (ticketsRequest > availableTickets.get()){                                        //check for ticket avaiability, before cutomer's purchase
                    System.out.println(customerName + " purchase " + ticketsRequest + " tickets");      //if not enough, thread waits on lock (why does it need to wait if there's not enough?)
                    System.out.println("Purchase failed: not enough tickets\n");                      //wait() causes the current thread to release the lock and suspend execution until someone else changes the condition (i.e., tickets become available) and calls notifyAll().
                    try {                                                                               //But in this case, since tickets are only ever decreasing, the waiting here is more for a symbolic purpose
                        lock.wait();
                    } catch (InterruptedException e) 
                    {Thread.currentThread().interrupt();
                    return false;
                    }
                }
                availableTickets.addAndGet(-ticketsRequest);                                            //if enough, ticket count is updated via atomic subtraction and notifies other waiting threads
                System.out.println(customerName + " purchase " + ticketsRequest + " tickets");
                System.out.println("Purchase success!\n");

                lock.notifyAll();
                return true;

            }
        }
        public int getRemainingTickets(){
            return availableTickets.get();
        }
    }
    static class Customer implements Runnable {                                 //Represents a customer behavior
        private final TicketCounter counter;                                    //Each customer can purchase from the shared counter
        private final String customerName;                                      //Each customer is identified by their name
        private final int ticketsRequest;                                       //Each customer has a number of tickets to purchase
        
        public Customer(TicketCounter counter, String customerName){            //Customer constructor
            this.counter = counter;
            this.customerName = customerName;
            this.ticketsRequest = 1 + new Random().nextInt(10);
        }
        @Override
        public void run(){                                                      //Customer's task to be executed by a thread
            try {                                                               //Brief delay with Thread.sleep to simulate processing time
                Thread.sleep((long)(Math.random()*500));                        //Customer then calls tryBooking() on the TicketCounter
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            counter.tryBooking(customerName, ticketsRequest);
        }
    }
    public static void main(String[] args) throws InterruptedException{         //Coordinate overall simulation
        TicketCounter counter = new TicketCounter();                            //Create shared counter object
        List<Thread> threads = new ArrayList<>();                               //List to hold references to all threads

        for (int i=0; i<20; i++){                                               //Create 20 cutomer threads, start them concurrently 
            String name = "Customer " + i;
            Thread t = new Thread(new Customer(counter, name));
            threads.add(t);
            t.start();
        }
        for(Thread t : threads){
            t.join();                                                           //Join after completion
        }                                                                       //And print final ticket summary
        System.out.println("\nBooking complete.");
        System.out.println("Remaining tickets: " + counter.getRemainingTickets());
    }
    
}
