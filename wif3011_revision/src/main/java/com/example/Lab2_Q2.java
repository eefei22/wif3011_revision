/*  Write a Java program that sequentially finds the 
    largest number in an array of integers with 
    1,000,000 elements filled with randomly generated 
    numbers in the range of 1 to 50,000.  
    Use two threads to concurrently find the largest number*/

package com.example;
import java.util.Random;

public class Lab2_Q2 {
    private static int arraySize = 1000000;
    private static int max_num = 5000;
    private static int threadCount = 2;
       public static void main (String args[]) throws InterruptedException{
       Random num = new Random();
       int[] array = new int[arraySize];
       for (int i=0; i<arraySize; i++){
            array[i] = num.nextInt(max_num);
       }
       int chunkSize = arraySize / threadCount;
       int[] results = new int [threadCount];

        Thread t1 = new Thread(() ->{
            int maxNum1 = 0;
            for (int i=0; i<chunkSize; i++){
                if (array[i] > maxNum1){
                    maxNum1 = array[i];
                    System.out.println("Thread 1 - max: " + maxNum1);
                }
            }
            results[0] = maxNum1;
       });

        Thread t2 = new Thread(() -> {
            int maxNum2 = 0;
            for (int j=chunkSize; j < arraySize; j++){
                if (array[j] > maxNum2){
                    maxNum2 = array[j];
                    System.out.println("Thread 2 - max: " + maxNum2);
                }
            }
            results[1] = maxNum2;
       });

       t1.start();
       t2.start();
       t1.join();
       t2.join();

       int finalLargestNum = Math.max(results[0], results[1]);
       System.out.println("The largest number is: " + finalLargestNum);
    }
}