package com.gh.sd.interrupt;

public class ThreadInterrupt1 {
    public static void main(String[] args) {
        var thread = new Thread(new BlockingTask());
        thread.start();
        thread.interrupt();
    }

    private static class BlockingTask implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(1000000);
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted");
            }
        }
    }

}
