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
import java.util.concurrent.*;
import java.util.ArrayList;

public class Practice1 {

    static class TicketCounter{
        
    }

}
