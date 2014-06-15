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

    public static synchronized void makeLoad() {
        if (isLoadMade) return;

        for (int i = 0; i < THREAD_COUNT; ++i) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    for (int i = 1; ; ++i) {
                        if (i % 1000000 == 0) {
                            sleep(1);
                        }
                    }
                }
            });
        }
    }

    public static void sleep(long l) {
        try {
            Thread.sleep(l);
        } catch (InterruptedException e) {
        }
    }
}
