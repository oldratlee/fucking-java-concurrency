package fucking.concurrency.demo;

/**
 * <a href="https://hllvm-group.iteye.com/group/topic/34932">请问R大 有没有什么工具可以查看正在运行的类的c/汇编代码</a>提到了<b>代码提升</b>的问题。
 *
 * @author Jerry Lee (oldratlee at gmail dot com)
 */
public class NoPublishDemo {
    private static boolean stop = false;

    public static void main(String[] args) throws Exception {
        // LoadMaker.makeLoad();

        new Thread(new ConcurrencyCheckTask()).start();

        Thread.sleep(1000);
        System.out.println("Set stop to true in main!");
        stop = true;
        System.out.println("Exit main.");
    }

    private static class ConcurrencyCheckTask implements Runnable {
        @Override
        @SuppressWarnings({"WhileLoopSpinsOnField", "StatementWithEmptyBody"})
        public void run() {
            System.out.println("ConcurrencyCheckTask started!");
            // If the task thread sees the value of stop written by main, the loop will exit.
            // On my dev machine, the loop almost never exits!
            // Simple and safe solution:
            //   add volatile to the `stop` field.
            while (!stop) {
            }
            System.out.println("ConcurrencyCheckTask stopped!");
        }
    }
}
