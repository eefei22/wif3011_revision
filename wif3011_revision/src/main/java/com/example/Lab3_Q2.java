/*
 * A bank account allows different users to perform deposit 
 * or withdrawal at the same time. This requires the update 
 * of account balance to be properly controlled, or otherwise, 
 * error may occur. For example, if two deposits are performed 
 * concurrently, the account’s balance must be the sum of existing
 * balance and the two deposits. Write a Java program that allows 
 * multiple deposits/withdrawals to be performed as threads. 
 * Provide a control mechanism to ensure the account balance is 
 * always correct. 
 */

package com.example;

public class Lab3_Q2 {
    
    static class BankAccount {
        private double balance;

        public BankAccount(double initialBal){
            this.balance = initialBal;
        }
        public synchronized void deposit(double amount){
            double newBal = balance + amount;
            balance = newBal;
            System.out.println(Thread.currentThread().getName() + " deposited " + amount + ". Balance:" + balance );
        }
        public synchronized void withdrawal(double amount){
           if(balance >= amount){
                double newBal = balance - amount;
                balance = newBal;
                System.out.println(Thread.currentThread().getName() + " withdrew " + amount + ". Balance: " + balance);
            } else {
                System.out.println("Unable to withdraw, insufficient balance. Current balance: " + balance);
            }
        }
        public synchronized double getBalance(){
            return balance;
        }
    }
    static class AccountUser implements Runnable{
        private final BankAccount account;
        private final boolean isDepositor;
        private final double amount;

        public AccountUser (BankAccount account, boolean isDepositor, double amount){
            this.account = account;
            this.isDepositor = isDepositor;
            this.amount = amount;
        }
        @Override
        public void run(){
            if (isDepositor){
                account.deposit(amount);
            }else {
                account.withdrawal(amount);
            }
        }
    }
    public static void main (String[] args){
        BankAccount account = new BankAccount (1000.0);
        System.out.println("Initial Balance: " + account.getBalance());
        Thread[] threads = new Thread[10];

        for (int i=0; i<5; i++){
           threads[i] = new Thread(new AccountUser(account, true , 50), "Depositor " + (i+1)); 
        }
        for (int i=5; i<10; i++){
            threads[i] = new Thread(new AccountUser(account, false, 20), "Withdrawer " + (i-4));
        }
        for (Thread thread : threads){
            thread.start();
        }
        for (Thread thread: threads){
            try {
                thread.join();
            } catch (InterruptedException e){Thread.currentThread().interrupt();}
        }
        System.out.println("Final Balance: " + account.getBalance());
    }

}

