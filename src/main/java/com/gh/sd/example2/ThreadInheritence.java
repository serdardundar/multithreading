package com.gh.sd.example2;

public class ThreadInheritence {

    public static void main(String[] args) {
        var thread = new NewThread();
        thread.start();
    }

    private static class NewThread extends Thread {
        @Override
        public void run() {
            System.out.println("Hello from " + this.getName());
        }
    }
}
