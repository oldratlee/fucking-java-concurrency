package fucking.concurrency.demo;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Jerry Lee (oldratlee at gmail dot com)
 * @see <a href="http://coolshell.cn/articles/9606.html">Infinite loop of Java HashMap</a> by <a href="http://github.com/haoel">@haoel</a>
 */
public class HashMapHangDemo {
    final Map<Integer, Object> holder = new HashMap<>();

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) {
        HashMapHangDemo demo = new HashMapHangDemo();
        for (int i = 0; i < 100; i++) {
            demo.holder.put(i, null);
        }

        Thread thread = new Thread(demo.getConcurrencyCheckTask());
        thread.start();
        thread = new Thread(demo.getConcurrencyCheckTask());
        thread.start();

        System.out.println("Start get in main!");
        for (int i = 0; ; ++i) {
            for (int j = 0; j < 10000; ++j) {
                demo.holder.get(j);

                // If the hashmap occurs hang problem, the following output will not appear again.
                // On my dev machine, this problem is easily observed in the first round.
                System.out.printf("Got key %s in round %s%n", j, i);
            }
        }
    }

    ConcurrencyTask getConcurrencyCheckTask() {
        return new ConcurrencyTask();
    }

    private class ConcurrencyTask implements Runnable {
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
