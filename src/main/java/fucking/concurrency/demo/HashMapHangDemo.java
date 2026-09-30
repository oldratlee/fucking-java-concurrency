package fucking.concurrency.demo;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Jerry Lee (oldratlee at gmail dot com)
 * @see <a href="http://coolshell.cn/articles/9606.html">Infinite loop of Java HashMap</a>
 * by <a href="http://github.com/haoel">@haoel</a>
 */
public class HashMapHangDemo {
    private static final Map<Integer, Object> holder = new HashMap<>();

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            holder.put(i, null);
        }

        // start 2 tasks
        new Thread(new ConcurrencyTask()).start();
        new Thread(new ConcurrencyTask()).start();

        System.out.println("Start the get loop in main!");
        for (int i = 0; ; ++i) {
            for (int key = 0; key < 10_000; ++key) {
                holder.get(key);

                // If the HashMap hangs, the following output will not appear again.
                // On my dev machine, this problem is easily observed in the first round.
                System.out.printf("Get key %s in round %s%n", key, i);
            }
        }
    }

    private static class ConcurrencyTask implements Runnable {
        private final Random random = new Random();

        @Override
        @SuppressWarnings("InfiniteLoopStatement")
        public void run() {
            System.out.println("Add loop started in task!");
            while (true) {
                holder.put(random.nextInt() % (1024 * 1024 * 100), null);
            }
        }
    }
}
