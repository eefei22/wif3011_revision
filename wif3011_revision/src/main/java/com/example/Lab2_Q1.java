/*  Write a Java program that sequentially finds the 
    largest number in an array of integers with 
    1,000,000 elements filled with randomly generated 
    numbers in the range of 1 to 50,000.  */

package com.example;
import java.util.Random;

public class Lab2_Q1 {
    public static void main(String args[]){
        int size = 1000000;
        Random num = new Random();
        int[] array = new int[size];

        for (int i=0; i<size; i++){
            array[i] = num.nextInt(50000);
        }

        int largest = 0;
        for (int j=0; j<size; j++){
            if (array[j]>largest){
                largest = array[j];
                System.out.println("New Largest: " + largest);
            }
        }
    }
}

