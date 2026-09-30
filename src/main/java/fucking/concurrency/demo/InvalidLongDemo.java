package fucking.concurrency.demo;

/**
 * @author Jerry Lee(oldratlee at gmail dot com)
 */
public class InvalidLongDemo {
    private static long count = 0;

    public static void main(String[] args) {
        // LoadMaker.makeLoad();

        new Thread(new ConcurrencyCheckTask()).start();

        for (int i = 0; ; i++) {
            long x = i;
            count = x << 32 | x;
        }
    }

    private static class ConcurrencyCheckTask implements Runnable {
        @Override
        public void run() {
            int occurTimes = 0;
            for (int i = 0; ; i++) {
                long x = count;
                long high = x >>> 32;
                long low = x & 0xFFFFFFFFL;
                if (high != low) {
                    occurTimes++;
                    System.err.printf("Fuck! Got invalid long!! check times=%s, occur times=%s(%s%%), high=%s, low=%s%n",
                            i + 1, occurTimes, (float) occurTimes / (i + 1) * 100, high, low);
                } else {
                    // If this output is removed, an invalid long is not observed on my dev machine
                    System.out.printf("Emm... high=%s, low=%s%n", high, low);
                }
            }
        }
    }

}
