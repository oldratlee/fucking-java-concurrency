package fucking.concurrency.demo;

/**
 * @author Jerry Lee (oldratlee at gmail dot com)
 */
public class WrongCounterDemo {
    private static final int INC_COUNT = 100000000;

    private volatile int counter = 0;

    public static void main(String[] args) throws Exception {
        WrongCounterDemo demo = new WrongCounterDemo();

        System.out.println("Start task thread!");
        Thread thread1 = new Thread(demo.getConcurrencyCheckTask());
        thread1.start();
        Thread thread2 = new Thread(demo.getConcurrencyCheckTask());
        thread2.start();

        thread1.join();
        thread2.join();

        int actualCounter = demo.counter;
        int expectedCount = INC_COUNT * 2;
        if (actualCounter != expectedCount) {
            // Even if volatile is added to the counter field,
            // On my dev machine, it's almost must occur!
            // Simple and safe solution:
            //   use AtomicInteger
            System.err.printf("Fuck! Got wrong count!! actual %s, expected: %s.%n", actualCounter, expectedCount);
        } else {
            System.out.println("Wow... Got right count!");
        }
    }

    ConcurrencyCheckTask getConcurrencyCheckTask() {
        return new ConcurrencyCheckTask();
    }

    private class ConcurrencyCheckTask implements Runnable {
        @Override
        @SuppressWarnings("NonAtomicOperationOnVolatileField")
        public void run() {
            for (int i = 0; i < INC_COUNT; ++i) {
                ++counter;
            }
        }
    }
}
