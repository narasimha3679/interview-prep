import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Demonstrates ThreadLocal isolation across a thread pool and the importance of remove().
 */
public class ThreadLocalDemo {

    private static final ThreadLocal<String> TEST_ID = new ThreadLocal<>();

    public static void main(String[] args) throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(2);

        for (int i = 1; i <= 6; i++) {
            int task = i;
            pool.submit(() -> {
                try {
                    TEST_ID.set("test-" + task);
                    System.out.println(Thread.currentThread().getName()
                            + " -> " + TEST_ID.get());
                } finally {
                    TEST_ID.remove();
                }
            });
        }

        pool.shutdown();
        if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
            pool.shutdownNow();
        }
    }
}
