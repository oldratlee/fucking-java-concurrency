package fucking.concurrency.demo;

/**
 * @author Jerry Lee (oldratlee at gmail dot com)
 */
public class InconsistentReadDemo {
    private int count = 1;

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
            int occurTimes = 0;
            for (int i = 0; ; i++) {
                // 2 consecutive reads in the same thread
                int read1 = count;
                int read2 = count;
                if (read1 != read2) {
                    occurTimes++;
                    // On my dev machine,
                    // a batch of inconsistent reads can be observed when the process starts
                    System.err.printf("Fuck! Got inconsistent read!! check times=%s, occur times=%s(%s%%), read1=%s, read2=%s%n",
                            i + 1, occurTimes, (float) occurTimes / (i + 1) * 100, read1, read2);
                }
            }
        }
    }
}
