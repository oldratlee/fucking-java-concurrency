package fucking.concurrency.demo.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Jerry Lee (oldratlee at gmail dot com)
 */
public class LoadMaker {
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int THREAD_COUNT = CPU_COUNT * 2;

    private static final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);

    private static volatile boolean isLoadMade = false;

    public static synchronized void makeLoad() {
        isLoadMade = true;

        for (int taskCount = 0; taskCount < THREAD_COUNT; ++taskCount) {
            executorService.submit(() -> {
                for (int i = 1; ; ++i) {
                    if (i % 1_000_000 == 0) {
                        if (!isLoadMade) return;
                        sleep(1);
                    }
                }
            });
        }
    }

    public static void stopLoad() {
        isLoadMade = false;
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }
}
