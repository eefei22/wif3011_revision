# 🧵 Java Concurrency Examples

This document provides runnable Java code examples for key concurrency concepts.

---
## 📘 Basic Thread

```java
public class BasicThreadExample {
    public static void main(String[] args) {
        Thread t = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("Hello from thread " + i);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        t.start();
    }
}
```

---
## 📘 Synchronized Method

```java
public class SynchronizedCounter {
    private int count = 0;

    public synchronized void increment() {
        count++;
    }

    public int getCount() {
        return count;
    }

    public static void main(String[] args) throws InterruptedException {
        SynchronizedCounter counter = new SynchronizedCounter();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) counter.increment();
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) counter.increment();
        });

        t1.start(); t2.start();
        t1.join(); t2.join();

        System.out.println("Final count: " + counter.getCount());
    }
}
```

---
## 📘 Wait and Notify

```java
public class WaitNotifyExample {
    private static final Object lock = new Object();
    private static boolean isReady = false;

    public static void main(String[] args) {
        Thread waiter = new Thread(() -> {
            synchronized (lock) {
                while (!isReady) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                System.out.println("Waiter thread proceeding.");
            }
        });

        Thread notifier = new Thread(() -> {
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
            synchronized (lock) {
                isReady = true;
                lock.notify();
                System.out.println("Notifier thread notified.");
            }
        });

        waiter.start();
        notifier.start();
    }
}
```

---
## 📘 Volatile Variable

```java
public class VolatileExample {
    private static volatile boolean flag = false;

    public static void main(String[] args) {
        Thread writer = new Thread(() -> {
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
            flag = true;
            System.out.println("Flag set to true.");
        });

        Thread reader = new Thread(() -> {
            while (!flag) {}
            System.out.println("Flag detected as true.");
        });

        writer.start();
        reader.start();
    }
}
```

---
## 📘 ReentrantLock (Manual Locking)

```java
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockExample {
    private static final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        Runnable task = () -> {
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + " acquired the lock.");
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
                System.out.println(Thread.currentThread().getName() + " released the lock.");
            }
        };

        new Thread(task).start();
        new Thread(task).start();
    }
}
```

---
## 📘 AtomicInteger (Atomic Operations)

```java
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerExample {
    public static void main(String[] args) throws InterruptedException {
        AtomicInteger count = new AtomicInteger(0);

        Runnable task = () -> {
            for (int i = 0; i < 1000; i++) {
                count.incrementAndGet();
            }
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);
        t1.start(); t2.start();
        t1.join(); t2.join();

        System.out.println("Final count: " + count.get());
    }
}
```

---
