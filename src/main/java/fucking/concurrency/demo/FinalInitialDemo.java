package fucking.concurrency.demo;


/**
 * @author hyy (hjlbupt at 163 dot com)
 * @author Jerry Lee (oldratlee at gmail dot com)
 */
public class FinalInitialDemo {
    public static void main(String[] args) throws Exception {
        DataProvider dataProvider = new DataProvider();
        new Thread(dataProvider).start();

        for (long i = 0; ; i++) {
            final Data data = dataProvider.data;
            if (data.isFieldDefaultValueNotInitValue()) {
                System.err.println("Fuck! instruction reordering occurred.");
            }
            if (i % 1_000_000_000 == 0) System.out.printf("read  %,15d times%n", i + 1);
        }
    }

    private static class DataProvider implements Runnable {
        Data data = new Data();

        @Override
        public void run() {
            for (long i = 0; ; i++) {
                data = new Data();

                if (i % 100_000_000 == 0) System.out.printf("write %,15d times%n", i + 1);
            }
        }
    }

    private static class Data {
        private int no;
        private boolean flag;
        private int[] array1;

        public Data() {
            this.no = 42;
            this.flag = true;
            array1 = new int[10];
        }

        boolean isFieldDefaultValueNotInitValue() {
            return no == 0 || !flag || array1 == null;
        }
    }
}
