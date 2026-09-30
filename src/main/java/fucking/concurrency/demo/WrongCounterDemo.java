package fucking.concurrency.demo;

/**
 * @author Jerry Lee (oldratlee at gmail dot com)
 */
public class WrongCounterDemo {
    private static final int INC_COUNT = 100_000_000;

    private static volatile int counter = 0;

    public static void main(String[] args) throws Exception {
        System.out.println("Start task threads!");
        Thread thread1 = new Thread(new ConcurrencyCheckTask());
        thread1.start();
        Thread thread2 = new Thread(new ConcurrencyCheckTask());
        thread2.start();

        thread1.join();
        thread2.join();

        int actualCounter = counter;
        int expectedCount = INC_COUNT * 2;
        if (actualCounter != expectedCount) {
            // Even with volatile on the counter field,
            // On my dev machine, it almost always occurs!
            // Simple and safe solution:
            //   use AtomicInteger
            System.err.printf("Fuck! Got wrong count!! actual %,d, expected %,d.%n",
                    actualCounter, expectedCount);
        } else {
            System.out.println("Wow... Got right count!");
        }
    }

    private static class ConcurrencyCheckTask implements Runnable {
        @Override
        @SuppressWarnings("NonAtomicOperationOnVolatileField")
        public void run() {
            for (int i = 0; i < INC_COUNT; ++i) {
                ++counter;
            }
        }
    }
}
