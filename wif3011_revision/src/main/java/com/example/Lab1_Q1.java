package com.example;
/*
 Using thread in Java.   
   1. Create a class called PrintChar that prints a given character for a given time.   
      （e.g. printing ‘A’ for 10 times.）   
   2. Create a class called PrintNum that prints numbers from 1 to the given number.  
       (e.g. printing from 1 to 45.)   
   3. Create a driver class that runs 2 instances of PrintChar and 1 instance of PrintNum as threads. 

 Run the program several times to examine how the execution of threads interleave with each other. 
 */

class printChar implements Runnable {
  private char character;
  
  public printChar (char c){
    character = c;
  }

  @Override
  public void run() {
    for (int i=0; i<10; i++){
      System.out.println(character);
    }
  } 
}

class printNum implements Runnable{
  private int num = 45; 
  @Override
  public void run(){
    for (int i=0; i<num; i++){
      System.out.println(i);
    }
  }
}

public class Lab1_Q1{
  public static void main (String args[]){
    Runnable R1 = new printChar('A');
    Runnable R2 = new printChar('V');
    Runnable R3 = new printNum();

    Thread t1 = new Thread(R1);
    Thread t2 = new Thread(R2);
    Thread t3 = new Thread(R3);

    t1.start();
    t2.start();
    t3.start();

  }
}