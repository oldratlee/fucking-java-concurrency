package com.oldratlee.fucking.concurrency;

import com.oldratlee.fucking.concurrency.util.Utils;

/**
 * <a href="https://hllvm-group.iteye.com/group/topic/34932">请问R大 有没有什么工具可以查看正在运行的类的c/汇编代码</a>提到了<b>代码提升</b>的问题。
 *
 * @author Jerry Lee (oldratlee at gmail dot com)
 */
public class NoPublishDemo {
    boolean stop = false;

    public static void main(String[] args) {
        // LoadMaker.makeLoad();

        NoPublishDemo demo = new NoPublishDemo();

        Thread thread = new Thread(demo.getConcurrencyCheckTask());
        thread.start();

        Utils.sleep(1000);
        System.out.println("Set stop to true in main!");
        demo.stop = true;
        System.out.println("Exit main.");
    }

    ConcurrencyCheckTask getConcurrencyCheckTask() {
        return new ConcurrencyCheckTask();
    }

    @SuppressWarnings({"StatementWithEmptyBody", "WhileLoopSpinsOnField"})
    private class ConcurrencyCheckTask implements Runnable {
        @Override
        public void run() {
            System.out.println("ConcurrencyCheckTask started!");
            // If the value of stop is visible in the main thread, the loop will exit.
            // On my dev machine, the loop almost never exits!
            // Simple and safe solution:
            //   add volatile to the running field.
            while (!stop) {
            }
            System.out.println("ConcurrencyCheckTask stopped!");
        }
    }
}
