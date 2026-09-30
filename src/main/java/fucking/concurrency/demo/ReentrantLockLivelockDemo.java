package fucking.concurrency.demo;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Beat Richartz
 * @see SymmetricLockDeadlockDemo
 */
public class ReentrantLockLivelockDemo {
    public static void main(String[] args) throws Exception {
        final Lock lock1 = new ReentrantLock();
        final Lock lock2 = new ReentrantLock();

        Thread thread1 = new Thread(new ConcurrencyCheckTask(1, lock1, lock2));
        thread1.start();
        Thread thread2 = new Thread(new ConcurrencyCheckTask(2, lock2, lock1));
        thread2.start();
    }

    private static class ConcurrencyCheckTask implements Runnable {
        private final int id;
        private final Lock lockFirst;
        private final Lock lockSecond;

        private ConcurrencyCheckTask(int id, Lock lockFirst, Lock lockSecond) {
            this.id = id;
            this.lockFirst = lockFirst;
            this.lockSecond = lockSecond;
        }

        @Override
        public void run() {
            try {
                System.out.println("Started concurrency check task " + id);
                int total = 100;
                int occurTimes = 0;
                for (int i = 0; i < total; i++) {
                    if (!work()) occurTimes++;
                }
                if (occurTimes > 0) {
                    System.err.printf("Fuck! No actual progress in %s of %s iterations of task %s.%n",
                            occurTimes, total, id);
                } else {
                    System.out.printf("Emm... Actual progress in all %s iterations of task %s!%n", total, id);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private boolean work() throws InterruptedException {
            // simulate work before acquiring the first lock
            Thread.sleep(2);

            if (!lockFirst.tryLock(5, TimeUnit.MILLISECONDS)) {
                System.err.println("Task " + id + " failed to acquire the first lock, wasting this attempt's work");
                return false;
            }
            try {
                // simulate work after acquiring the first lock
                Thread.sleep(10);

                if (!lockSecond.tryLock()) {
                    System.out.println("Task " + id + " failed to acquire the second lock, wasting this attempt's work");
                    return false;
                }
                try {
                    // simulate work after acquiring the second lock
                    Thread.sleep(10);
                } finally {
                    lockSecond.unlock();
                }
            } finally {
                lockFirst.unlock();
            }
            return true;
        }
    }
}
