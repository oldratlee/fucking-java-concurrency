package fucking.concurrency.demo;

/**
 * @author Jerry Lee (oldratlee at gmail dot com)
 * @see ReentrantLockLivelockDemo
 */
@SuppressWarnings("InfiniteLoopStatement")
public class SymmetricLockDeadlockDemo {
    public static void main(String[] args) throws Exception {
        final Object lock1 = new Object();
        final Object lock2 = new Object();

        Thread thread1 = new Thread(new ConcurrencyCheckTask(1, lock1, lock2));
        thread1.start();
        Thread thread2 = new Thread(new ConcurrencyCheckTask(2, lock2, lock1));
        thread2.start();
    }

    private static class ConcurrencyCheckTask implements Runnable {
        private final int id;
        private final Object lockFirst;
        private final Object lockSecond;

        private ConcurrencyCheckTask(int id, Object lockFirst, Object lockSecond) {
            this.id = id;
            this.lockFirst = lockFirst;
            this.lockSecond = lockSecond;
        }

        @Override
        public void run() {
            System.out.println("ConcurrencyCheckTask" + id + " started!");
            while (true) {
                synchronized (lockFirst) {
                    synchronized (lockSecond) {
                        System.out.println("Hello" + id);
                    }
                }
            }
        }
    }
}
