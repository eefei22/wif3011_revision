# Java Concurrent Programming Cheatsheet (Complete)

## Thread Basics

### Creating a Thread
```java
public class Lab2_Q2 {
  public static void main (String args[]) throws InterruptedException {
    Thread t2 = new Thread(() -> {
      // code to execute
    });
    t2.start();
    t2.join();
  }
}
```

---

## 🔒 Synchronized Blocks

```java
import java.util.concurrent.Semaphore;

public class Lab3_Q1 {
    private static final Semaphore guestSemaphore = new Semaphore(#permits);
    private static final Object lock = new Object();

    static class Cleaner implements Runnable {
        private final String name;

        public Cleaner(String name){
            this.name = name;
        }

        @Override
        public void run() {
            try {
                synchronized (lock){
                    while (condition to wait){
                        //code to execute
                        cleanerLock.wait();
                    }
                    //more code to execute
                }
                Thread.sleep(2000);
                synchronized (lock){
                    //code to execute
                    cleanerLock.notifyAll();
                }
            } catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }
    }
}
```

---

## 🚦 Semaphores

```java
static class Guest implements Runnable {
  private final String name;

  public Guest(String name) {
    this.name = name;
  }

  @Override
  public void run(){
    try {
      guestSemaphore.acquire();
      synchronized (lock){
        while (condition to wait){
          cleanerLock.wait();
        }
      }
      Thread.sleep(1000);
      synchronized (lock){
        if (guestCount == 0){
          cleanerLock.notifyAll();
        }
      }
      guestSemaphore.release();
    } catch (InterruptedException e){
      Thread.currentThread();
    }
  }
}
```

---

## 🧮 Synchronized Methods

```java
public class Lab3_Q2 {
  static class BankAccount {
    private double balance;
    public BankAccount(double initialBal){
      this.balance = initialBal;
    }

    public synchronized void deposit(double amount){
      // deposit logic
    }

    public synchronized void withdrawal(double amount){
      // withdrawal logic
    }
  }

  static class AccountUser implements Runnable {
    private final BankAccount account;
    private final boolean isDepositor;
    private final double amount;

    public AccountUser(BankAccount account, boolean isDepositor, double amount){
      this.account = account;
      this.isDepositor = isDepositor;
      this.amount = amount;
    }

    @Override
    public void run(){
      if (isDepositor){
        account.deposit(amount);
      } else {
        account.withdrawal(amount);
      }
    }
  }
}
```

---

## 🧵 Creating Multiple Threads

```java
public static void main(String[] args){
  for (int i=1; i<=3; i++){
    new Thread(new Cleaner("name" + i)).start();
  }

  for (int i=1; i<11; i++){
    new Thread(new Guest("name" + i)).start();
  }
}
```

or

```java
public static void main (String[] args){
  Thread[] threads = new Thread[10];
  for (int i=0; i<5; i++){
    threads[i] = new Thread(new AccountUser(account, true , 50), "Thread Name " + (i+1));
  }
  for (int i=5; i<10; i++){
    threads[i] = new Thread(new AccountUser(account, false, 20), "Thread Name " + (i-4));
  }
  for (Thread thread : threads){
    thread.start();
  }
  for (Thread thread: threads){
    try {
      thread.join();
    } catch (InterruptedException e){
      Thread.currentThread().interrupt();
    }
  }
}
```

---

## 🔓 Explicit Locks & Conditions

### Lock Interface

- `lock()`, `unlock()`, `tryLock()`, `tryLock(time, unit)`, `lockInterruptibly()`

```java
Lock lock = new ReentrantLock();
lock.lock();
try {
  // critical section
} finally {
  lock.unlock();
}
```

### Condition Interface

```java
Condition condition = lock.newCondition();
lock.lock();
try {
  while (!someCondition) {
    condition.await();
  }
  // work
  condition.signal();
} finally {
  lock.unlock();
}
```

---

## 🔁 ReentrantLock Example

```java
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockExample {
  private static final ReentrantLock lock = new ReentrantLock();
  private static int counter = 0;

  public static void increment() {
    lock.lock();
    try {
      counter++;
      System.out.println(Thread.currentThread().getName() + " incremented counter to " + counter);
    } finally {
      lock.unlock();
    }
  }

  public static void main(String[] args) {
    Runnable task = () -> {
      for (int i = 0; i < 3; i++) {
        increment();
      }
    };

    Thread t1 = new Thread(task, "Thread-A");
    Thread t2 = new Thread(task, "Thread-B");

    t1.start();
    t2.start();
  }
}
```

---

## 🔏 ReentrantReadWriteLock Example

```java
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrantReadWriteLockExample {
  private static final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
  private static String sharedData = "Initial";

  public static void readData(String readerName) {
    rwLock.readLock().lock();
    try {
      System.out.println(readerName + " is reading: " + sharedData);
      Thread.sleep(500);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    } finally {
      rwLock.readLock().unlock();
    }
  }

  public static void writeData(String newValue, String writerName) {
    rwLock.writeLock().lock();
    try {
      System.out.println(writerName + " is writing: " + newValue);
      Thread.sleep(1000);
      sharedData = newValue;
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    } finally {
      rwLock.writeLock().unlock();
    }
  }

  public static void main(String[] args) {
    Runnable reader1 = () -> readData("Reader-1");
    Runnable reader2 = () -> readData("Reader-2");
    Runnable writer = () -> writeData("Updated", "Writer");

    new Thread(reader1).start();
    new Thread(reader2).start();
    new Thread(writer).start();
  }
}
```