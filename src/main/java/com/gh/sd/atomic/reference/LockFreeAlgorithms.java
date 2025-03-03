package com.gh.sd.atomic.reference;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Demonstrates lock-free and standard stack implementations with concurrent operations.
 */
public class LockFreeAlgorithms {
    private static final int INITIAL_STACK_SIZE = 100_000;
    private static final int EXECUTION_TIME_SECONDS = 10;
    private static final int PUSHING_THREADS = 2;
    private static final int POPPING_THREADS = 2;

    public static void main(String[] args) throws InterruptedException {
        StackDemo.builder()
                .withInitialSize(INITIAL_STACK_SIZE)
                .withPushingThreads(PUSHING_THREADS)
                .withPoppingThreads(POPPING_THREADS)
                .withExecutionTime(EXECUTION_TIME_SECONDS)
                .build()
                .run();
    }
}

/**
 * Builder class for stack demonstration configuration
 */
class StackDemo {
    private final int initialSize;
    private final int pushingThreads;
    private final int poppingThreads;
    private final int executionTimeSeconds;
    private final Random random;

    private StackDemo(Builder builder) {
        this.initialSize = builder.initialSize;
        this.pushingThreads = builder.pushingThreads;
        this.poppingThreads = builder.poppingThreads;
        this.executionTimeSeconds = builder.executionTimeSeconds;
        this.random = new Random();
    }

    public static Builder builder() {
        return new Builder();
    }

    public void run() throws InterruptedException {
        LockFreeStack<Integer> stack = initializeStack();
        List<Thread> threads = createAndStartThreads(stack);
        
        TimeUnit.SECONDS.sleep(executionTimeSeconds);
        
        System.out.printf("%,d operations were performed in %d seconds %n", 
            stack.getCounter(), executionTimeSeconds);
    }

    private LockFreeStack<Integer> initializeStack() {
        LockFreeStack<Integer> stack = new LockFreeStack<>();
        IntStream.range(0, initialSize)
                .forEach(i -> stack.push(random.nextInt()));
        return stack;
    }

    private List<Thread> createAndStartThreads(LockFreeStack<Integer> stack) {
        List<Thread> threads = Stream.concat(
            createPushingThreads(stack),
            createPoppingThreads(stack)
        ).collect(Collectors.toList());

        threads.forEach(Thread::start);
        return threads;
    }

    private Stream<Thread> createPushingThreads(LockFreeStack<Integer> stack) {
        return IntStream.range(0, pushingThreads)
                .mapToObj(i -> createDaemonThread(() -> {
                    while (!Thread.currentThread().isInterrupted()) {
                        stack.push(random.nextInt());
                    }
                }));
    }

    private Stream<Thread> createPoppingThreads(LockFreeStack<Integer> stack) {
        return IntStream.range(0, poppingThreads)
                .mapToObj(i -> createDaemonThread(() -> {
                    while (!Thread.currentThread().isInterrupted()) {
                        stack.pop();
                    }
                }));
    }

    private Thread createDaemonThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setDaemon(true);
        return thread;
    }

    static class Builder {
        private int initialSize;
        private int pushingThreads;
        private int poppingThreads;
        private int executionTimeSeconds;

        public Builder withInitialSize(int size) {
            this.initialSize = size;
            return this;
        }

        public Builder withPushingThreads(int threads) {
            this.pushingThreads = threads;
            return this;
        }

        public Builder withPoppingThreads(int threads) {
            this.poppingThreads = threads;
            return this;
        }

        public Builder withExecutionTime(int seconds) {
            this.executionTimeSeconds = seconds;
            return this;
        }

        public StackDemo build() {
            return new StackDemo(this);
        }
    }
}

