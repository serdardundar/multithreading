package com.gh.sd.io.bound;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Not too much differences than {@link IoBoundApplication} but this implementation
 * is much less efficient.
 */
public class IoBoundApplicationV2 {
    private static final int NUMBER_OF_TASKS = 10_000;

    public static void main(String[] args) {
        System.out.printf("Running %d tasks\n", NUMBER_OF_TASKS);

        long start = System.currentTimeMillis();
        performTasks();
        System.out.printf("Tasks took %dms to complete\n", System.currentTimeMillis() - start);
    }

    private static void performTasks() {
        try (ExecutorService executorService = Executors.newFixedThreadPool(1000)) {

            for (int i = 0; i < NUMBER_OF_TASKS; i++) {
                executorService.submit(() -> {
                    for (int j = 0; j < 100; j++) {
                        blockingIoOperation();
                    }
                });
            }
        }
    }

    // Simulates a long blocking IO
    private static void blockingIoOperation() {
        System.out.println("Executing a blocking task from thread: " + Thread.currentThread());
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}