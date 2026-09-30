package fucking.concurrency.demo;


/**
 * @author hyy (hjlbupt at 163 dot com)
 */
public class FinalInitialDemo {

    private int a;
    private boolean flag;
    private FinalInitialDemo demo;

    private FinalInitialDemo() {
        a = 1;
        flag = true;
    }

    private void writer() {
        demo = new FinalInitialDemo();
    }

    private void reader() {
        if (flag) {
            int i = a * a;
            if (i == 0) {
                // On my dev machine, variable initialization always succeeds.
                // To solve this problem, make the `a` and `flag` fields final.
                System.out.println("Fuck! instruction reordering occurred.");
            }
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) throws Exception {
        while (true) {
            FinalInitialDemo demo = new FinalInitialDemo();
            Thread threadA = new Thread(demo::writer);
            Thread threadB = new Thread(demo::reader);

            threadA.start();
            threadB.start();

            threadA.join();
            threadB.join();
        }
    }
}
