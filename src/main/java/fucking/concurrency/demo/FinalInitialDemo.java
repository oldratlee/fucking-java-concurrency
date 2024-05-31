package fucking.concurrency.demo;


/**
 * @author hyy (hjlbupt at 163 dot com)
 */
public class FinalInitialDemo {

    private int a;
    private boolean flag;
    private FinalInitialDemo demo;

    public FinalInitialDemo() {
        a = 1;
        flag = true;
    }

    public void writer() {
        demo = new FinalInitialDemo();
    }

    public void reader() {
        if (flag) {
            int i = a * a;
            if (i == 0) {
                // On my dev machine, the variable initial always success.
                // To solve this problem, add final to the `a` field and `flag` field.
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
