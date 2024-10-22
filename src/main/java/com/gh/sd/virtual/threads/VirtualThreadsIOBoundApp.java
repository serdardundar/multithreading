package com.gh.sd.virtual.threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The same implementation as {@link com.gh.sd.io.bound.IoBoundApplication}
 * but we used Virtual Threads in this class instead.
 */
public class VirtualThreadsIOBoundApp {

    private static final int NUMBER_OF_TASKS = 1000;

    public static void main(String[] args) {
        System.out.printf("Running %d tasks\n", NUMBER_OF_TASKS);

        long start = System.currentTimeMillis();
        performTasks();
        System.out.printf("Tasks took %dms to complete\n", System.currentTimeMillis() - start);
    }

    private static void performTasks() {
        try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {

            for (int i = 0; i < NUMBER_OF_TASKS; i++) {
                executorService.submit(VirtualThreadsIOBoundApp::blockingIoOperation);
            }
        }
    }

    // Simulates a long blocking IO
    private static void blockingIoOperation() {
        System.out.println("Executing a blocking task from thread: " + Thread.currentThread());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
