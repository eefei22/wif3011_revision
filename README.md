Object Oriented Programming (OOP) Concepts

| Concept | Meaning / Purpose | Important Keywords / Syntax |
| --- | --- | --- |
| Class | Blueprint/template for creating objects. Defines attributes and methods. | class, 
public class ClassName {} |
| Object | An instance of a class. Represents a real entity with its own data. | ClassName obj = new ClassName(); |
| Attribute | Variables that belong to a class. Also called fields or properties. | String name;, int age; |
| Method | Functions that belong to a class. Define what an object can do. | public void methodName() { ... } |
| Encapsulation | Hiding internal data using private and accessing it via public methods. | private, public, getX(), setX() |
| Inheritance | A class (child) inherits attributes and methods from another class (parent). | extends, super() |
| Polymorphism | Methods can behave differently depending on the object or input. | @Override, method overloading, inheritance |

| Constructor Overloading | Create different ways to initialize objects, which accepts different parameters | this() |
| --- | --- | --- |
| Method Overloading | Use same method name with different input types | Same method name, with @Override |
| super | Call parent methods or constructor | super() |
| this | Refer to current object | this |
| Abstract Class | Partial abstraction, reusable base class | abstract |
| Interface | Full abstraction, defines what should be done | interface, implements |

Examples

| public class Animal {              // Class
    private String name;           // Attribute (Encapsulated)
    
    public Animal(String name) {   // Constructor
        this.name = name;
    }

    public void speak() {          // Method
        System.out.println("Animal speaks");
    }
}

public class Dog extends Animal {  // Inheritance
    public Dog(String name) {
        super(name);
    }

    @Override                      // Polymorphism
    public void speak() {
        System.out.println("Woof!");
    }
} |
| --- |

Class and Objects

| Class Person {
    String name;
    int age;
    void greet() {
        System.out.println(“hellp, my name is” + name);
    }
} |
| --- |

For modeling real-world entities as objects

Constructors

| class Student {
    String name;
    int id;
    Student(String name, int id){
        this.name = name;
        this.id = id;
    }
} |
| --- |

Use constructors to initialise objects with data during creation

Encapsulation

| class BankAccount{
    private double balance;

    public void deposit(double amount) {
        if (amount > 0) balance += amount;
    }
    public double getBalance() {
        return balance;
    }
} |
| --- |

For hiding data and provide access via methods

Inheritance

| class Animal{
    void makeSound() {
        System.out.println(“some sound”);
    }
}
class Dog extends Animal{
    void makeSound(){
        System.out.println(“woof”);
    }
} |
| --- |

For creating specialized classes from general ones

Abstraction

| abstract class Shape{
    abstract double area();
}
class Circle extends Shape{
    double radius;

    Circle(double r) {radius = r}
    double area() {return Math.PI * radius *radius; }
} |
| --- |

For defining a common interface for a group of subclasses

Interface

| interface Printable{
    void print();
}
class Document implements Printable{
    public void print(){
        System.out.println(“printing document...");
    }
} |
| --- |

For enforcing a contract for classes

Concurrent and Parallel Programming

Basic Thread Methods

| Method | Description |
| --- | --- |
| start() | Starts the thread (calls run() internally). |
| run() | Contains the code to be executed in the thread. Usually overridden. |
| sleep(ms) | Pauses the thread for a specified time (in milliseconds). |
| join() | Waits for a thread to finish execution. |
| isAlive() | Returns true if the thread is still running. |
| setName(String) | Sets the thread's name. |
| getName() | Gets the thread's name. |
| setPriority(int) | Sets the thread’s priority (1 to 10). |
| yield() | Suggests that the current thread should pause and let others run. |

Examples

Threads and Runnable

| public class MyRunnable implements Runnable{
    public void run(){
    ...
    //code to be executed by thread
    ...
    }
    public static void main(String[] args){
        //create runnable instances
        MyRunnable run1 = new MyRunnable();
        MyRunnable run2 = new MyRunnable();

        //create threads for runnable instances
        Thread thread1 = new Thread(run1);
        Thread thread2 = new Thread(run2);

        //start the threads
        thread1.start()
        thread2.start()
    }
} |
| --- |

Threads represent lightweight, independent paths of execution that can perform tasks concurrently. The runnable interface is a functional interface that represents a task or piece of code that can be executed by a thread.

Synchronized Keyword

| A synchronized block of code ensures that only one Thread can execute them at a time. To enter a synchronized block, a thread must acquire the object’s lock; after doing so, all code within the block can be exclusively and atomically manipulated by said thread. Upon exiting the synchronized block, the lock is returned to the object for other threads to acquire.
-----------------------------------------------------------------------------
public class SynchronizedBlockExample{
    private int count = 0;
    private Object lock = new Object();

    public void performTask(){
        synchronized (lock) {
            for (int i = 0; i < 1000; i++) {
                count++;
            }
        }
    }
}

The synchronized keyword can also be specified on a method level. For non-static methods, lock is acquired from the object instance. For static methods, lock is acquired from the Class which the object instance is from. synchronized locks are reentrant, if the thread already holds the lock, it can successfully acquire it again.
-----------------------------------------------------------------------------
public class SynchronizedExample{
    private int count = 0;

    //synchronized instance method
    public synchronized void increment(){
        count++;
    }

    //synchronized static method
    public static synchronized void decrement(){
        count–;
    }
    public synchronized void reentranceExample(){
        increment();
        decrement();
    }
} |
| --- |

Thread Lifecycle Methods

| start()
wait()
notify()
notifyAll()
join()
yield()
sleep()
interrupt() | The following example demonstrates the usage of wait(), and notify() to coordinate two threads to print alternate numbers.
---------------------------------------------------------------
public class WaitNotifyExample{
    private static final Object lock = new Object();
    private static boolean isOddTurn = true;

    public static void main(String [] args){
        Thread oddThread = new Thread(() -> {
            for (int i = 1; i < 10; i += 2){
                synchronized (lock) {
                    while (!isOddTurn){
                        try {
                            lock.wait(); //wait till odd thread’s turn
                        } catch (InterruptException e){
                            Thread.currentThread().interrupt();
                        }
                    }
                    System.out.println(“Odd: “ + i);
                    isOddTurn = false; //satisfy waiting condition
                    lock.notify(); //notify the even thread
                }
            }
        });
        Thread evenThread = new Thread(() -> {
            for (int = 2; i <= 10; i+=2){
                synchronized (lock) {
                    while(isOddTurn){
                        try {
                            lock.wait(); //wait till even thread’s turn
                        } catch (InterruptException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    System.out.println(“Even: “ + i);
                    isOddTurn = true; //satisfy waiting condition
                    lock.notify() //notify the odd thread
                }
            }
        });

        oddThread.start()
        evenThread.start()
    }
} |
| --- | --- |

Things to note:

In order to use wait(), notify(), notifyAll() on an object, the lock must be acquired first – both threads synchronized (lock)

Always wait inside a loop that checks the condition being waited on

Always ensure that the waiting condition is satisfied before calling notify() or notifyAll()

Thread Lifecycle Demo

| public class ThreadLifecycleDemo {

    private static final Object lock = new Object();

    public static void main(String[] args) {

        Thread waitingThread = new Thread(new WaitingThread(), "WaitingThread");
        Thread notifierThread = new Thread(new NotifierThread(), "NotifierThread");
        Thread controllerThread = new Thread(new ControllerThread(waitingThread, notifierThread), "ControllerThread");

        waitingThread.start();    // --> start() 
        notifierThread.start();   // --> start()
        controllerThread.start(); // --> start()
    }

    // Thread that waits on a lock
    static class WaitingThread implements Runnable {
        @Override
        public void run() {
            synchronized (lock) {
                try {
                    System.out.println(Thread.currentThread().getName() + ": Waiting...");
                    lock.wait();  // --> wait()
                    System.out.println(Thread.currentThread().getName() + ": Notified and Resumed!");
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName() + ": Interrupted while waiting.");
                }
            }
        }
    }

    // Thread that notifies one or all waiting threads
    static class NotifierThread implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(1000); // --> sleep()
                synchronized (lock) {
                    System.out.println(Thread.currentThread().getName() + ": Notifying waiting thread...");
                    lock.notify(); // --> notify()
                    // lock.notifyAll(); // (alternatively, use notifyAll())
                }
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + ": Interrupted during sleep.");
            }
        }
    }

    // Thread that controls other threads
    static class ControllerThread implements Runnable {
        private final Thread t1, t2;

        ControllerThread(Thread t1, Thread t2) {
            this.t1 = t1;
            this.t2 = t2;
        }

        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread().getName() + ": Yielding...");
                Thread.yield(); // --> yield()

                System.out.println(Thread.currentThread().getName() + ": Waiting for NotifierThread to finish...");
                t2.join();      // --> join()

                System.out.println(Thread.currentThread().getName() + ": Interrupting WaitingThread (if still alive)...");
                t1.interrupt(); // --> interrupt()

            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + ": Interrupted.");
            }
        }
    }
} |
| --- |

The volatile Keyword

| public class VolatileExample {
    private static volatile boolean flag = false;

    public static void main(String[] args) {
        Thread writerThread = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            flag = true;
            System.out.println(“flag set to true by writerThread.”);
        });

        Thread readerThread = new Thread(() -> {
            while (!flag) {
                //wait till flag becomes true
            }
            System.out.println(“Flag is true, readerThread can proceed.”);
        });

        writerThread.start();
        readerThread.start();
    }
} |
| --- |

When a variable is declared as volatile, it guarantees that any read or write operation on that variable is directly performed on the main memory, ensuring atomic updates and visibility of changes to all threads.

Deadlock

| Deadlock happens when two or more threads are unable to proceed with their execution because they are each waiting for the other(s) to release a resource or a lock, resulting in an indefinite standstill. The following example demonstrates a deadlock scenario involving two threads and two locks; 
In this example: thread1 acquires lock1 and the waits for lock2 and thread2 acquires lock2 and the waits for lock1
----------------------------------------------------------------------------
public class DeadlockExample {
    private static final Object lock1 = new Object();
    private static final Object lock2 = new Object();
 
    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            synchronized (lock1) {
                System.out.println("Thread 1: Holding lock 1...");
                try { Thread.sleep(100); } catch (InterruptedException e) {}
                System.out.println("Thread 1: Waiting for lock 2...");
                synchronized (lock2) {
                    System.out.println("Thread 1: Acquired lock 2.");
                }
            }
        });
 

        Thread thread2 = new Thread(() -> {
            synchronized (lock2) {
                System.out.println("Thread 2: Holding lock 2...");
                try { Thread.sleep(100); } catch (InterruptedException e) {}
                System.out.println("Thread 2: Waiting for lock 1...");
                synchronized (lock1) {
                    System.out.println("Thread 2: Acquired lock 1.");
                }
            }
        });
 
        thread1.start();
        thread2.start();
    }
}

Deadlock can be avoided or resolved by various techniques:
Use a timeout – release any holding locks if a thread cannot acquire a lock within a specific time
Lock ordering – establish a consistent order for threads to acquire lock, prevent circular waiting
Using abstractions – java.util.concurrent packages
Example below demonstrates use of java.util.concurrent package
----------------------------------------------------------------------------
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DeadlockResolutionExample {
    private static final Lock lock1 = new ReentrantLock();
    private static final Lock lock2 = new ReentrantLock();

    public static void main (String[] argos){
        Runnable acquireLocks = () -> {
            lock1.lock();
            try {
               sysout(Thread.currentThread().getName() + “: holding lock 1”);
               try {
                   Thread.sleep(100);
               } catch (InterruptedException e) {
               }
               sysout(Thread.currentThread().getName()+“: waitin for lock2”);

               //attempt to acquire lock2 with a timeout of 500ms
               boolean acquiredLock2 = lock2.tryLock(500, TimeUnit.MILESECS);
               if (acquiredLock2) {
                   try {
                       sysout(Thread.currentThread().getName() + “: 
                        Acquired lock 2. “);
                   } finally {
                       lock2.unlock();
                   }
               } else {
                    System.out.println(Therad.currentThread().getName() + “: 
                    Timeout while waiting for lock 2.” );
               }
           } finally {
                lock1.unlock();
           }
        };

        //consistent order for acquiring locks and use of timeouts
        Thread thread1 = new Thread(acquireLocks);
        Thread thread2 = new Thread(acquireLocks);

        thread1.start();
        thread2.start();
    }
} |
| --- |

Atomics

Common Atomic Classes

| AtomicInteger | An integer value that can be atomically incremented, decremented, or updated |
| --- | --- |
| AtomicLong | A long value that supports atomic operations |
| AtomicBoolean | A boolean value with atomic operations for setting and getting |
| AtomicReference | A generic reference type that supports atomic updates |
| AtomicStampedReference | A variant of AtomicReference that includes version stamp to detect changes |
| AtomicIntegerArray,
AtomicLongArray, 
AtomicReferenceArray | Arrays of atomic values |

Demo

| import java.util.concurrent.atomic.AtomicInteger;
    public class AtomicExample{
        public static void main(String[] args) {
            AtomicInteger atomicInt = new AtomicInteger(0);

            //increment
            int incVal = atomicInt.incrementAndGet();
            System.out.println(“Incremented value: “ + incVal);

            //add a specific value 
            int addedVal = atomicInt.addAndGet(5);
            System.out.println(“Added value: “ + addedVal);

            //compare and set 
            boolean updated = atomicInt.compareandSet(10, 15)
            System.out.println(“ Value updated? “ + updated);

            //get the current value
            int currentVal = atomicInt.get();
            System.out.println(“Current value: “ + currentValue);    
        }} |
| --- |

Locks

| Locks provide more flexible and advanced locking mechanisms compared to synchronized blocks. Utilising two interfaces, Lock, and ReadWriteLock from the  java.util.concurrent.locks package. Implementation classes are ReentrantLock and ReentrantReadWriteLock respectively.
-----------------------------------------------------------------------------
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockExample {
    private static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        Runnable task = () -> {
            lock.lock(); 
            try {
                System.out.println(“Thread “ + Thread.currentThread().getID() 
                + “ acquired the lock”); 

                //perform some critical section operations
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();    
            } finally {
                lock.unlock();     //release lock
                System.out.println(“Thread “ + Thread.currentThread().getId()
                + “released the lock “);
            }
        };
        //create multiple threads to access the critical section 
        for (int i = 0; i < 3; i++) {
            new Thread(task).start();
        }
    }
}

ReentrantReadWriteLock provides separate locks for reading and writing. Multiple threads can read a shared resource simultaneously but only one thread can write to the resource at a time.
-----------------------------------------------------------------------------
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockExample{
    private static ReadWriteLock rwl = new ReentrantReadWriteLock();
    private static String sharedData = “Initial Data”;

    public static void main(String[] args) {
        Runnable reader = () -> {
            readWriteLock.readlLock.lock();
            try {
                sysout.pln(“Reader Thread “+ Thread.currentThread().getId() + 
                “is reading: “ + sharedData);
            } finally {
                readWriteLock.readLock().unlock();
            }
        };
        Runnable writer = () -> {
            readWriteLock.writeLock().lock();
            try {
                sharedData = “new data”;
                sysout.pln(“Writer Thread “ + Thread.currentThread().getId() 
                + “ is writing: “ + sharedData);
            } finally {
                readWriteLock.writeLock().unlock();
            }
        };
        for (int i = 0; i < 3; i++) {
            new Thread(reader).start();
        }
        for (int i = 0; i < 2; i++) {
            new Thread(writer).start();
        }
    }
} |
| --- |

Concurrent Collection Class

| ConcurrentHashMap
ConcurrentSkipListMap
BlockingQueue 
LinkedBlockingQueue
DelayQueue
PriorityBlockingQueue
SynchronousQueue
ConcurrentLinkedQueue
ConcurrentLinkedDeque
CopyOnWriteArrayList
CopyOnWriterArraySet
ConcurrentSkipsListSet |
| --- |

| Tool | Use |
| --- | --- |
| synchronized blocks | Automatically acquire and release locks on objects |
| ReentrantLock | Manual control over locking and unlocking |
| wait() / notify() | Thread coordination and signaling |
| AtomicInteger, ConcurrentHashMap | Thread-safe data structures from java.util.concurrent |
| ExecutorService | Thread pool management to reduce manual thread creation |

| Situation | Use This | How It Helps |
| --- | --- | --- |
| 🔐 Need mutual exclusion for shared data | synchronized or ReentrantLock | Prevent race conditions |
| 📣 Need one thread to wait until another is done | wait() / notify() or CountDownLatch | Synchronize timing |
| ⏱ Need timeout-based locking | ReentrantLock.tryLock(timeout) | Avoid deadlocks |
| 🧠 Need atomic operations (e.g. counters) | AtomicInteger | Avoid manual locking |
| 🧵 Need to run tasks but don’t want to manage threads | ExecutorService, ThreadPoolExecutor | Cleaner thread lifecycle |
| 👥 Need thread-safe collections | ConcurrentHashMap, CopyOnWriteArrayList | Safe concurrent data access |