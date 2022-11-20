package fucking.concurrency.demo;

/**
 * @author Jerry Lee (oldratlee at gmail dot com)
 */
public class InconsistentReadDemo {
    int count = 1;

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) {
        InconsistentReadDemo demo = new InconsistentReadDemo();

        Thread thread = new Thread(demo.getConcurrencyCheckTask());
        thread.start();

        while (true) {
            demo.count++;
        }
    }

    ConcurrencyCheckTask getConcurrencyCheckTask() {
        return new ConcurrencyCheckTask();
    }

    private class ConcurrencyCheckTask implements Runnable {
        @Override
        @SuppressWarnings({"InfiniteLoopStatement", "ConstantConditions"})
        public void run() {
            int c = 0;
            for (int i = 0; ; i++) {
                // 2 consecutive reads in the same thread
                int c1 = count;
                int c2 = count;
                if (c1 != c2) {
                    c++;
                    // On my dev machine,
                    // a batch of inconsistent reads can be observed when the process starts
                    System.err.printf("Fuck! Got inconsistent read!! check time=%s, happen time=%s(%s%%), 1=%s, 2=%s%n",
                            i + 1, c, (float) c / (i + 1) * 100, c1, c2);
                }
            }
        }
    }
}
