package fucking.concurrency.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.CompletableFuture.supplyAsync;
/**
 * @author Eric Lin (linqinghua4 at gmail dot com)
 */
public class CyclicThreadPoolDeadLockDemo {
    public static void main(String[] args) throws InterruptedException {
        if (args.length > 0 && "good".equals(args[0])) {
            goodCase();
        } else {
            badCase();
        }
    }

    static void badCase() throws InterruptedException {
        int poolSize = 16;
        ThreadPoolExecutor pool1 = new ThreadPoolExecutor(poolSize, poolSize,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>());
        ThreadPoolExecutor pool2 = new ThreadPoolExecutor(poolSize, poolSize,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>());
        List<Future<Integer>> futures = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            int finalI = i;
            Future<Integer> future = pool1.submit(() -> {
                System.out.println("step1, i = " + finalI);
                return 1 + getUnchecked(pool2.submit(() -> {
                    System.out.println("step2, i = " + finalI);
                    return 2 + getUnchecked(pool1.submit(() -> {
                        System.out.println("step3, i = " + finalI);
                        return 3;
                    }));
                }));
            });
            futures.add(future);
        }
        // 无法计算，死锁
        int result = futures.stream()
                .mapToInt(CyclicThreadPoolDeadLockDemo::getUnchecked)
                .sum();
        System.out.println("result = " + result);
        // 无法关闭
        pool1.awaitTermination(20, TimeUnit.SECONDS);
        pool2.awaitTermination(20, TimeUnit.SECONDS);
    }

    static void goodCase() throws InterruptedException {
        int poolSize = 16;
        ThreadPoolExecutor pool1 = new ThreadPoolExecutor(poolSize, poolSize,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>());
        ThreadPoolExecutor pool2 = new ThreadPoolExecutor(poolSize, poolSize,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>());
        List<Future<Integer>> futures = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            int finalI = i;
            CompletableFuture<Integer> cf1 = supplyAsync(() -> {
                System.out.println("step1, i = " + finalI);
                return 1;
            }, pool1);
            CompletableFuture<Integer> cf2 = supplyAsync(() -> {
                System.out.println("step2, i = " + finalI);
                return 2;
            }, pool2);
            CompletableFuture<Integer> cf3 = supplyAsync(() -> {
                System.out.println("step3, i = " + finalI);
                return 3;
            }, pool1);
            Future<Integer> future =
                    cf1.thenComposeAsync(x ->
                            cf2.thenComposeAsync(y ->
                                    cf3.thenApply(z ->
                                            x + y + z), pool2), pool1);
            futures.add(future);
        }
        System.out.println("size1 = " + pool1.getQueue().size());
        System.out.println("size2 = " + pool2.getQueue().size());
        int result = futures.stream()
                .mapToInt(CyclicThreadPoolDeadLockDemo::getUnchecked)
                .sum();
        System.out.println("result = " + result);
        pool1.awaitTermination(20, TimeUnit.SECONDS);
        pool2.awaitTermination(20, TimeUnit.SECONDS);
    }

    static <T> T getUnchecked(Future<T> future) {
        try {
            return future.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}