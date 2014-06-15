package com.oldratlee.fucking.concurrency;

import com.oldratlee.fucking.concurrency.util.Utils;

/**
 * @author ding.lid
 */
public class NoPublishDemo {
    boolean stop = false;

    public static void main(String[] args) {
        // LoadMaker.makeLoad();

        NoPublishDemo t = new NoPublishDemo();

        Thread thread = new Thread(t.getConcurrencyCheckTask());
        thread.start();

        Utils.sleep(1000);
        System.out.println("Set stop to true in main!");
        t.stop = true;
        System.out.println("Exit main.");
    }

    ConcurrencyCheckTask getConcurrencyCheckTask() {
        return new ConcurrencyCheckTask();
    }

    private class ConcurrencyCheckTask implements Runnable {
        @Override
        public void run() {
            System.out.println("ConcurrencyCheckTask started!");
            // 如果主线中stop的值可见，则循环会退出。
            // 在我的开发机上，几乎必现循环不退出！（解法：在running属性上加上volatile）
            while (!stop) {
            }
            System.out.println("ConcurrencyCheckTask stopped!");
        }
    }
}
