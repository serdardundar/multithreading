package com.gh.sd.join;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class ThreadJoin2 {

    public static void main(String[] args) throws InterruptedException {

        BigInteger result = calculateResult(BigInteger.TWO, BigInteger.valueOf(5L), BigInteger.TWO, BigInteger.valueOf(5L));
        System.out.println(result);
    }


    public static BigInteger calculateResult(BigInteger base1, BigInteger power1, BigInteger base2, BigInteger power2) throws InterruptedException {
        BigInteger result = BigInteger.ZERO;

        List<PowerCalculatingThread> threads = new ArrayList<>();

        threads.add(new PowerCalculatingThread(base1, power1));
        threads.add(new PowerCalculatingThread(base2, power2));

        threads.forEach(Thread::start);
        for (PowerCalculatingThread thread : threads) {
            //by doing this, we can calculate the latest addition
            thread.join();
        }

        for (PowerCalculatingThread thread : threads) {
            result = result.add(thread.getResult());
        }

        return result;
    }


    private static class PowerCalculatingThread extends Thread {
        private BigInteger result = BigInteger.ONE;
        private final BigInteger base;
        private final BigInteger power;

        public PowerCalculatingThread(BigInteger base, BigInteger power) {
            this.base = base;
            this.power = power;
        }

        @Override
        public void run() {

            result = BigInteger.ONE;

            for (BigInteger i = BigInteger.ZERO; i.compareTo(power) != 0; i = i.add(BigInteger.ONE)) {
                result = result.multiply(base);
            }

            System.out.println("result : " + result);
        }

        public BigInteger getResult() {
            return result;
        }
    }
}
