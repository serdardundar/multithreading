package com.gh.sd.example;

public class ThreadCreation1 {
    public static void main(String[] args) throws InterruptedException {
        var thread = new Thread(() -> {
            System.out.println("We are now in thread " + Thread.currentThread().getName());
            System.out.println("Current thread priority: " + Thread.currentThread().getPriority());
        });

        var misbehavingThread = new Thread(() -> {
            throw new RuntimeException("Intentional Exception");
        });

        thread.setName("New Worker Thread");
        thread.setPriority(Thread.MAX_PRIORITY);

        misbehavingThread.setUncaughtExceptionHandler((t, e) ->
            System.out.println("A Critical Exception happened in the thread " + t.getName()
                + " the error is " + e.getMessage()));

        System.out.println("We are in thread: " + Thread.currentThread().getName() + " before starting a new thread");
        thread.start();
        System.out.println("We are in thread: " + Thread.currentThread().getName() + " after starting a new thread");

        misbehavingThread.start();

        Thread.sleep(10000);
        System.out.println( Thread.currentThread().getName() + " ended its job");
    }
}