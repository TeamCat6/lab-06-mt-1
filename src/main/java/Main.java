import java.util.List;

public class Main {

    private static final int VAR_NUMBER = 6;
    private static final long N = 100_000_000;

    private long threadsResult;

    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();
        System.out.println(getNumberByArithmetic());
        System.out.println(System.currentTimeMillis() - start);

        start = System.currentTimeMillis();
        System.out.println(getNumberByLoop());
        System.out.println(System.currentTimeMillis() - start);

        Main main = new Main();
        List<Integer> threadsCount = List.of(2, 4, 8, 16, 32);

        for (int threadCount : threadsCount) {
            start = System.currentTimeMillis();
            main.populateNumberByThreads(threadCount);
            System.out.println(main.threadsResult);
            System.out.println(System.currentTimeMillis() - start);
        }
    }

    private static long getNumberByArithmetic() {
        //S(N) = ((n(1) + n(N)) · N) / 2

        return (VAR_NUMBER + VAR_NUMBER * N) * N / 2;
    }

    private static long getNumberByLoop() {
        long result = 0;

        for (long i = 1; i <= N; i++) {
            result += i * VAR_NUMBER;
        }

        return result;
    }

    private void populateNumberByThreads(int threadCount) throws InterruptedException {
        this.threadsResult = 0;
        long partSize = N / threadCount;

        for (int i = 0; i < threadCount; i++) {
            Thread thread = new Thread(new ArithmeticRunnable(i, this, partSize));
            thread.start();
            thread.join();
        }
    }

    public static class ArithmeticRunnable implements Runnable {

        private final int i;
        private final Main main;
        private final long partSize;

        public ArithmeticRunnable(int i, Main main, long partSize) {
            this.i = i;
            this.main = main;
            this.partSize = partSize;
        }

        @Override
        public void run() {
            long result = 0;
            long start = (partSize * i) + 1;
            long end = partSize * (i + 1);

            for (long i = start; i <= end; i++) {
                result += i * VAR_NUMBER;
            }

            synchronized (this) {
                main.threadsResult += result;
            }
        }
    }
}
