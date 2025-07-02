package com.example;
import java.util.Stack;

class StackAccess {
    private final Stack<Integer> stack = new Stack<>();
    private final int MAX_SIZE = 3;

    public synchronized void push(int value) throws InterruptedException {
        while (stack.size() == MAX_SIZE) {
            wait(1000);
        }
        stack.push(value);
        System.out.println("Pushed: " + value);
        notifyAll();
    }

    public synchronized int pop() throws InterruptedException {
        int value;
        while (stack.isEmpty()){
            wait(1000);
        }
        value = stack.pop();
        notifyAll();
        return value;
    }

    public synchronized int peek() throws InterruptedException {
        int value;
        while (stack.isEmpty()){
            wait();
        }
        value = stack.peek();
        return value;
    }
}



public class Lab6_Q1 {
    
}
