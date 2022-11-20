package fucking.concurrency.demo;

/**
 * @author Jerry Lee(oldratlee at gmail dot com)
 */
public class InvalidLongDemo {
    long count = 0;

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) {
        // LoadMaker.makeLoad();

        InvalidLongDemo demo = new InvalidLongDemo();

        Thread thread = new Thread(demo.getConcurrencyCheckTask());
        thread.start();

        for (int i = 0; ; i++) {
            @SuppressWarnings("UnnecessaryLocalVariable")
            final long l = i;
            demo.count = l << 32 | l;
        }
    }

    ConcurrencyCheckTask getConcurrencyCheckTask() {
        return new ConcurrencyCheckTask();
    }

    private class ConcurrencyCheckTask implements Runnable {
        @Override
        @SuppressWarnings("InfiniteLoopStatement")
        public void run() {
            int c = 0;
            for (int i = 0; ; i++) {
                long l = count;
                long high = l >>> 32;
                long low = l & 0xFFFFFFFFL;
                if (high != low) {
                    c++;
                    System.err.printf("Fuck! Got invalid long!! check time=%s, happen time=%s(%s%%), count value=%s|%s%n",
                            i + 1, c, (float) c / (i + 1) * 100, high, low);
                } else {
                    // If remove this output, invalid long is not observed on my dev machine
                    System.out.printf("Emm... %s|%s%n", high, low);
                }
            }
        }
    }

}
