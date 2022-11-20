package fucking.concurrency.demo;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Beat Richartz
 */
public class ReentrantLockLivelockDemo {
    private static final Lock lock1 = new ReentrantLock();
    private static final Lock lock2 = new ReentrantLock();

    public static void main(String[] args) throws Exception {
        Thread thread1 = new Thread(ReentrantLockLivelockDemo::concurrencyCheckTask1);
        thread1.start();
        Thread thread2 = new Thread(ReentrantLockLivelockDemo::concurrencyCheckTask2);
        thread2.start();
    }

    private static void concurrencyCheckTask1() {
        System.out.println("Started concurrency check task 1");
        int counter = 0;

        while (counter++ < 10_000) {
            try {
                if (lock1.tryLock(50, TimeUnit.MILLISECONDS)) {
                    System.out.println("Task 1 acquired lock 1");
                    Thread.sleep(50);
                    if (lock2.tryLock()) {
                        System.out.println("Task 1 acquired lock 2");
                    } else {
                        System.out.println("Task 1 failed to acquire lock 2, releasing lock 1");
                        lock1.unlock();
                        continue;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }

            break;
        }

        System.err.printf("Fuck! No meaningful work done in %s iterations of task 1.%n", counter);
        lock2.unlock();
        lock1.unlock();
    }

    private static void concurrencyCheckTask2() {
        System.out.println("Started concurrency check task 2");

        int counter = 0;
        while (counter++ < 10_000) {
            try {
                if (lock2.tryLock(50, TimeUnit.MILLISECONDS)) {
                    System.out.println("Task 2 acquired lock 2");
                    Thread.sleep(50);
                    if (lock1.tryLock()) {
                        System.out.println("Task 2 acquired lock 1");
                    } else {
                        System.out.println("Task 2 failed to acquire lock 1, releasing lock 2");
                        lock2.unlock();
                        continue;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }

            break;
        }

        System.err.printf("Fuck! No meaningful work done in %s iterations of task 2.%n", counter);
        lock2.unlock();
        lock1.unlock();
    }
}
