package fucking.concurrency.demo;

/**
 * @author Jerry Lee (oldratlee at gmail dot com)
 */
public class InconsistentReadDemo {
    private static long count = 1;

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) {
        new Thread(new ConcurrencyCheckTask()).start();

        while (true) {
            count++;
        }
    }

    private static class ConcurrencyCheckTask implements Runnable {
        @Override
        public void run() {
            long occurTimes = 0;

            for (long i = 0; ; i++) {
                // 2 consecutive reads in the same thread
                long read1 = count;
                long read2 = count;
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
