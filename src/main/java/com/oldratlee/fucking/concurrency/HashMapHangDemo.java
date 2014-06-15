package com.oldratlee.fucking.concurrency;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author ding.lid
 * @see <a href="http://coolshell.cn/articles/9606.html">Java HashMap的死循环</a>@<a href="http://weibo.com/haoel">左耳朵耗子</a>
 */
public class HashMapHangDemo {
    final Map<Integer, Object> holder = new HashMap<Integer, Object>();

    public static void main(String[] args) {
        // LoadMaker.makeLoad();

        HashMapHangDemo t = new HashMapHangDemo();
        for (int i = 0; i < 100; i++) {
            t.holder.put(i, null);
        }

        Thread thread = new Thread(t.getConcurrencyCheckTask());
        thread.start();
        thread = new Thread(t.getConcurrencyCheckTask());
        thread.start();

        System.out.println("Start get in main!");
        for (int i = 0; ; ++i) {
            for (int j = 0; j < 10000; ++j) {
                t.holder.get(j);

                // 如果出现hashmap的get hang住问题，则下面的输出就不会再出现了。
                // 在我的开发机上，很容易在第一轮就观察到这个问题。
                System.out.printf("get key %s in round %s\n", j, i);
            }
        }
    }

    ConcurrencyCheckTask getConcurrencyCheckTask() {
        return new ConcurrencyCheckTask();
    }

    private class ConcurrencyCheckTask implements Runnable {
        Random random = new Random();

        @Override
        public void run() {
            System.out.println("Add loop started in task!");
            while (true) {
                holder.put(random.nextInt() % (1024 * 1024 * 100), null);
            }
        }
    }
}