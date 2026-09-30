package fucking.concurrency.demo;

import java.util.Random;

/**
 * @author Jerry Lee (oldratlee at gmail dot com)
 */
@SuppressWarnings("InfiniteLoopStatement")
public class InvalidCombinationStateDemo {
    public static void main(String[] args) {
        CombinationStatTask task = new CombinationStatTask();
        Thread thread = new Thread(task);
        thread.start();

        Random random = new Random();
        while (true) {
            int rand = random.nextInt(1000);
            task.state1 = rand;
            task.state2 = rand * 2;
        }
    }

    private static class CombinationStatTask implements Runnable {
        // For a combination state, adding volatile does not solve the problem
        private volatile int state1;
        private volatile int state2;

        @Override
        public void run() {
            int occurTimes = 0;
            for (long i = 0; ; i++) {
                int i1 = state1;
                int i2 = state2;
                if (i1 * 2 != i2) {
                    occurTimes++;
                    System.err.printf("Fuck! Got invalid combination state!!"
                                    + " check times=%s, occur times=%s(%s%%), state1=%s, state2=%s%n",
                            i + 1, occurTimes, (float) occurTimes / (i + 1) * 100, i1, i2);
                } else {
                    // If the output below is removed,
                    // the probability of an invalid combination on my dev machine goes from ~5% to ~0.1%
                    System.out.printf("Emm... state1=%s, state2=%s%n", i1, i2);
                }
            }
        }
    }

}
