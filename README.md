# Java Concurrent Programming Cheatsheet (Complete)

## Thread Basics

### Creating a Thread
```java
Thread t = new Thread(() -> {
    System.out.println("Running in thread");
});
t.start();
```

### Runnable Interface
```java
public class MyRunnable implements Runnable {
    public void run() {
        System.out.println("Runnable running");
    }
    public static void main(String[] args) {
        MyRunnable run1 = new MyRunnable();
        Thread thread1 = new Thread(run1);
        thread1.start();
    }
}
```

### Thread Methods Summary

| Method         | Description                                      |
|----------------|--------------------------------------------------|
| `start()`      | Starts the thread, invokes `run()` internally   |
| `run()`        | Entry point of thread logic                      |
| `sleep(ms)`    | Pauses thread for duration                      |
| `join()`       | Waits for thread to complete                     |
| `yield()`      | Hints that thread is willing to yield CPU       |
| `interrupt()`  | Interrupts sleeping/waiting thread              |
| `isAlive()`    | Checks if thread is still running                |
| `setName()`    | Names a thread                                   |
| `setPriority()`| Sets thread priority (1–10)                     |

## Atomic Operations

### Example: `AtomicInteger`
```java
AtomicInteger counter = new AtomicInteger();
counter.incrementAndGet(); // atomic +1
counter.getAndAdd(5);      // atomic +5
counter.compareAndSet(10, 15); // conditional update
```

## Synchronization & Locks

### Synchronized Block
```java
synchronized(lockObject) {
    // only one thread at a time
}
```

### ReentrantLock
```java
Lock lock = new ReentrantLock();
lock.lock();
try {
    // critical section
} finally {
    lock.unlock();
}
```

### ReentrantReadWriteLock
```java
ReadWriteLock rwLock = new ReentrantReadWriteLock();
rwLock.readLock().lock();
rwLock.readLock().unlock();
rwLock.writeLock().lock();
rwLock.writeLock().unlock();
```

## Thread Coordination

### wait(), notify(), notifyAll()
```java
synchronized (lock) {
    while (!condition) {
        lock.wait();  // must own the lock
    }
    // ...
    lock.notify(); // or notifyAll()
}
```

## Thread Lifecycle Example

```java
Thread waitingThread = new Thread(() -> {
    synchronized(lock) {
        lock.wait();
    }
});
Thread notifierThread = new Thread(() -> {
    synchronized(lock) {
        lock.notify();
    }
});
```

## Volatile Keyword

```java
private static volatile boolean flag = false;
```
- Ensures visibility across threads
- Prevents caching of variable value

## Deadlocks

### Classic Deadlock
```java
synchronized(lock1) {
    synchronized(lock2) { ... }
}
```

### Avoiding Deadlock
- Timeout (`tryLock`)
- Lock ordering
- `Concurrent` package utilities

## Executors

### Fixed Thread Pool
```java
ExecutorService service = Executors.newFixedThreadPool(5);
service.execute(() -> { ... });
service.shutdown();
```

### Scheduled Tasks
```java
ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
scheduler.scheduleAtFixedRate(task, 0, 10, TimeUnit.SECONDS);
```

## Concurrent Collections

- `ConcurrentHashMap`
- `CopyOnWriteArrayList`
- `BlockingQueue`, `PriorityBlockingQueue`
- `ConcurrentLinkedQueue`, `ConcurrentSkipListMap`

## Sample Use Cases

### Cleaner-Guest Problem
- Use `Semaphore` and `wait()/notifyAll()` to manage access and cleaning.

### Alternating Even-Odd Printer
- Use shared lock and `wait()/notify()` to switch turns.

## Strategy Guide

| Situation                                   | Use This                         | Benefit                              |
|--------------------------------------------|----------------------------------|--------------------------------------|
| Need mutual exclusion                      | `synchronized`, `ReentrantLock`  | Avoid race conditions                |
| One thread waits for another               | `wait()/notify()`, `CountDownLatch` | Synchronize timing               |
| Timeout-based locking                      | `tryLock(timeout)`               | Prevent deadlock                     |
| Shared counters                            | `AtomicInteger`                  | Avoid full locks                     |
| Thread management                          | `ExecutorService`                | Simplify thread lifecycle            |
| Shared collection with many readers/writers| `ConcurrentHashMap`, `CopyOnWriteArrayList` | Thread-safe data access     |

