package com.oldratlee.fucking.concurrency.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ding.lid
 */
public class Utils {
    static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    static final int THREAD_COUNT = CPU_COUNT * 2;
    static final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
    static volatile boolean isLoadMade = false;

    @SuppressWarnings("InfiniteLoopStatement")
    public static synchronized void makeLoad() {
        if (isLoadMade) return;

        for (int i = 0; i < THREAD_COUNT; ++i) {
            executorService.submit((Runnable) () -> {
                for (int i1 = 1; ; ++i1) {
                    if (i1 % 1000000 == 0) {
                        sleep(1);
                    }
                }
            });
        }
    }

    public static void sleep(long l) {
        try {
            Thread.sleep(l);
        } catch (InterruptedException ignored) {
        }
    }
}
