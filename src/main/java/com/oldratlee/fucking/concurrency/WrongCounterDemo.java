package com.oldratlee.fucking.concurrency;

/**
 * @author ding.lid
 */
public class WrongCounterDemo {
    private static final int INC_COUNT = 100000000;

    volatile int counter = 0;

    public static void main(String[] args) throws Exception {
        WrongCounterDemo demo = new WrongCounterDemo();

        System.out.println("Start task thread!");
        Thread thread1 = new Thread(demo.getConcurrencyCheckTask());
        thread1.start();
        Thread thread2 = new Thread(demo.getConcurrencyCheckTask());
        thread2.start();

        thread1.join();
        thread2.join();

        int finalCounter = demo.counter;
        if (finalCounter != INC_COUNT * 2) {
            // 在我的开发机上，几乎必现！即使counter上加了volatile。
            System.err.printf("Fuck! Got wrong count!! expected: %s, actual %s", INC_COUNT * 2, finalCounter);
        } else {
            System.out.println("Wow... Got right count!");
        }
    }

    ConcurrencyCheckTask getConcurrencyCheckTask() {
        return new ConcurrencyCheckTask();
    }

    private class ConcurrencyCheckTask implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < INC_COUNT; ++i) {
                ++counter;
            }
        }
    }
}
