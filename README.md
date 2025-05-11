# Java Concurrent Programming Cheatsheet

## Thread Basics

### Creating a Thread
```java
Thread t = new Thread(() -> {
    System.out.println("Running in thread");
});
t.start();
```

### Extending `Thread` vs Implementing `Runnable`
- **`Thread`**: Subclass when thread has its own behavior.
- **`Runnable`**: Preferred; allows sharing among threads.

```java
class MyRunnable implements Runnable {
    public void run() {
        System.out.println("Runnable running");
    }
}
```

## Thread Lifecycle

**States**: `New → Runnable → Running → Blocked/Waiting → Terminated`

### Key Methods:
- `start()` – Begins thread.
- `run()` – Code inside this is executed.
- `sleep(ms)` – Pauses thread.
- `join()` – Waits for thread to finish.
- `interrupt()` – Interrupts a sleeping/waiting thread.


## Synchronization and Locks

### `synchronized` Keyword
```java
synchronized(lockObject) {
    // critical section
}
```

- Prevents race conditions.
- Ensures only one thread can access the block.

### `ReentrantLock`
```java
Lock lock = new ReentrantLock();
lock.lock();
try {
    // critical section
} finally {
    lock.unlock();
}
```
- More flexible than `synchronized`.
- Allows tryLock(), fair locking.

## Common Concurrency Issues

| Problem | Description |
|--------|-------------|
| Race Condition | Two threads modify shared data unpredictably |
| Deadlock | Two+ threads wait forever for each other's lock |
| Starvation | A thread never gets CPU/lock time |
| Livelock | Threads continuously react to each other without progressing |


## Thread Coordination

### `wait()` / `notify()` / `notifyAll()`
```java
synchronized(obj) {
    while (conditionNotMet) {
        obj.wait(); // releases lock
    }
    // condition met
    obj.notify(); // wakes one waiting thread
}
```

## Atomic Operations

### `AtomicInteger`
```java
AtomicInteger counter = new AtomicInteger();
counter.incrementAndGet(); // atomic +1
counter.getAndAdd(5);      // atomic +5
```
- Avoids `synchronized` overhead for counters.

## Executor Framework

### Thread Pool with Executors
```java
ExecutorService service = Executors.newFixedThreadPool(5);
service.execute(() -> {
    // task
});
service.shutdown();
```

### Scheduled Executor
```java
ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
scheduler.scheduleAtFixedRate(task, 0, 10, TimeUnit.SECONDS);
```

## 🔁 Producer-Consumer using Semaphore
```java
Semaphore sem = new Semaphore(1); // permits
sem.acquire(); // wait for permit
// critical section
sem.release(); // give back permit
```

## Programmer Thought Process

1. **Problem Statement → Thread Roles**
   - Ex: Guest, Cleaner, Controller, QueueMonitor.

2. **Shared Resources? → Locks/Semaphores**
   - Synchronize only the minimum necessary block.

3. **Do I Need Mutual Exclusion?**
   - Use `synchronized`, `ReentrantLock`, or atomic types.

4. **Thread Safety or Performance?**
   - Use `ExecutorService` or thread-safe collections (`ConcurrentHashMap`, `BlockingQueue`).

5. **Test & Debug Concurrently**
   - Add `Thread.sleep()` to simulate real time.
   - Use `Thread.currentThread().getName()` for logs.