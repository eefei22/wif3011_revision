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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TicketBooking1 {
    static class TicketCounter {                                            //Shared object to keep track of ticket count
        private int availableTickets = 100;

        public synchronized boolean bookTickets(int quantity) {             //Synchronized method to safely (purchase) update available ticket count
            if (quantity <= availableTickets){                              //Customers can call bookTickets to request certain number of tickets
                availableTickets -= quantity;                               //Request can either succeed or fail dependent on quantity of tickets
                return true;                                                //Synchronized -- 1 customer per ticket booth at a time
            } else {
                return false;
            }
        }
        public int getRemainingTickets(){                                   //Returns remaining tickets left
            return availableTickets;
        }
    }

    static class Customer implements Runnable {                             //Represents one customer object
        private final TicketCounter counter;                                //Each customer can purchase from the shared counter
        private final int requestedTickets;                                 //Each customer has a number of tickets to purchase
        private final String customerName;                                  //Each customer is identified by their name

        public Customer(TicketCounter counter, String customerName){        //Customer constructor
            this.counter = counter;
            this.customerName = customerName;
            this.requestedTickets = new Random().nextInt(20) + 1;
        }
        @Override
        public void run(){                                                  //Customer's task to be executed by a thread
            boolean success = counter.bookTickets(requestedTickets);        //Cutomer's attmpt to purchase x number of tickets by calling bookTickets 
            synchronized(System.out){                                       //Print result of customer's attempt 
                System.out.println(customerName + " buys " + requestedTickets + " tickets");
                System.out.println("Ticket Purchase: " + (success ? "Success" : "Failed"));
            }
        }
    }
    public static void main(String[] args) throws InterruptedException{     //Coordinate overall simulation
        TicketCounter counter = new TicketCounter();                        //Create a *shared* counter object
        List<Thread> threadList = new ArrayList<>();

        for (int i=0; i<20; i++){
            Runnable r = new Customer(counter, "Customer " + i);            //20 customers, all sharing 1 counter to execute respective purchsing task 
            Thread t = new Thread(r);                                       //Threads to run customer's task -- Threads run concurrently but ticket availability updates are sequential
            threadList.add(t);                                              //Store all threads in an array list to keep track of processes
            t.start();                                                      //Start the execution
        }
        for (Thread th : threadList){
            th.join();                                                      //Complete the execution
        }
        System.out.println("Remaining tickets: " + counter.getRemainingTickets());
    }
}
