
# 🧵 Java OOP and Concurrency Cheatsheet

This README provides a consolidated reference for **Java Object-Oriented Programming** and **Concurrent Programming** concepts with code examples.

---

## 📦 Object-Oriented Programming

### 🧱 Class and Objects
```java
class Person {
    String name;
    int age;

    void greet() {
        System.out.println("Hello, my name is " + name);
    }
}
```
Use classes to model real-world entities as objects.

### 🔧 Constructors
```java
class Student {
    String name;
    int id;

    Student(String name, int id) {
        this.name = name;
        this.id = id;
    }
}
```
Constructors initialize objects with data during creation.

### 🔐 Encapsulation
```java
class BankAccount {
    private double balance;

    public void deposit(double amount) {
        if (amount > 0) balance += amount;
    }

    public double getBalance() {
        return balance;
    }
}
```
Encapsulation hides data and provides access via methods.

### 🧬 Inheritance & Polymorphism
```java
class Animal {
    void makeSound() {
        System.out.println("some sound");
    }
}

class Dog extends Animal {
    void makeSound() {
        System.out.println("woof");
    }
}
```
Inheritance allows classes to inherit behavior. Polymorphism enables method overriding.

### 🧊 Abstraction
```java
abstract class Shape {
    abstract double area();
}

class Circle extends Shape {
    double radius;

    Circle(double r) { radius = r; }

    double area() {
        return Math.PI * radius * radius;
    }
}
```
Abstract classes define a common interface.

### 🧾 Interface
```java
interface Printable {
    void print();
}

class Document implements Printable {
    public void print() {
        System.out.println("printing document...");
    }
}
```
Interfaces enforce a contract for implementing classes.

---

## 🚦 Java Concurrency

### 🔁 Basic Thread Methods

| Method | Description |
|--------|-------------|
| `start()` | Starts the thread |
| `run()` | Defines task executed by thread |
| `sleep(ms)` | Pauses thread |
| `join()` | Waits for thread to finish |
| `isAlive()` | Checks if thread is still running |
| `setName()` / `getName()` | Thread naming |
| `setPriority(int)` | Sets thread priority |
| `yield()` | Suggests yielding CPU time |

### 👯 Threads and Runnable
```java
public class MyRunnable implements Runnable {
    public void run() {
        // Task code here
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(new MyRunnable());
        Thread t2 = new Thread(new MyRunnable());
        t1.start();
        t2.start();
    }
}
```
Use `Runnable` for concurrent task execution.

### 🔒 Synchronized Block & Method
```java
public class SyncExample {
    private int count = 0;
    private final Object lock = new Object();

    public void increment() {
        synchronized (lock) {
            count++;
        }
    }
}
```
Synchronize blocks/methods to prevent race conditions.

### 🕒 wait(), notify(), notifyAll()
```java
synchronized (lock) {
    while (!condition) {
        lock.wait();
    }
    // Do work
    lock.notify();
}
```
Thread coordination via object monitors.

### 🌐 Volatile Keyword
```java
private static volatile boolean flag = false;
```
Ensures visibility across threads.

### 💥 Deadlock Example
```java
synchronized (lock1) {
    synchronized (lock2) { ... }
}
```
Avoid circular lock dependencies.

### 🧮 Atomic Variables
```java
AtomicInteger counter = new AtomicInteger(0);
counter.incrementAndGet();
```
Thread-safe atomic operations without locks.

### 🔁 Reentrant Locks
```java
ReentrantLock lock = new ReentrantLock();
lock.lock();
try { ... } finally { lock.unlock(); }
```

### 🧠 ReadWrite Locks
```java
ReadWriteLock rwLock = new ReentrantReadWriteLock();
rwLock.readLock().lock();
rwLock.readLock().unlock();
```

### 📚 Concurrent Collections

- `ConcurrentHashMap`
- `CopyOnWriteArrayList`
- `BlockingQueue`
- `ConcurrentLinkedQueue`

### 🧰 Summary Table

| Situation | Tool | Benefit |
|----------|------|---------|
| Shared data | `synchronized` / `ReentrantLock` | Prevent race conditions |
| Thread wait | `wait()/notify()` | Control execution flow |
| Timeout lock | `tryLock()` | Avoid deadlock |
| Atomic counters | `AtomicInteger` | Fast & thread-safe |
| Thread pools | `ExecutorService` | Manage thread lifecycle |
| Safe data structures | `ConcurrentHashMap` | Avoid manual synchronization |

---

📝 **Note**: Refer to `java.util.concurrent` for advanced concurrency utilities.
