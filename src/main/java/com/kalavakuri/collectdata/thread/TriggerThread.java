package com.kalavakuri.collectdata.thread;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TriggerThread implements Runnable {

    private static List<String> failedNirmalBangSymbols = null;
    private static String currentRunningCode = null;

    @Override
    public void run() {

        failedNirmalBangSymbols = new CopyOnWriteArrayList<>();

        String nirmalBangCode = "";

        for (double i = 100000; i <= 999999; i = i + 1001) {

            ExecutorService executorService = Executors.newFixedThreadPool(1000);
            CountDownLatch countDownLatch = new CountDownLatch(1000);

            for (double j = i; (j <= i + 1000 && j <= 999999); j++) {

                nirmalBangCode = String.valueOf(j).replace(".0", "");
                currentRunningCode = nirmalBangCode;
                CollectDataThread collectDataThread = new CollectDataThread(nirmalBangCode, failedNirmalBangSymbols, countDownLatch);
                executorService.submit(collectDataThread);
            }

            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(0);
            }

            executorService.shutdown();
        }

        failedNirmalBangSymbols.forEach(System.out::println);

        System.out.println("Retrying for failed codes.");

        ExecutorService executorService = Executors.newFixedThreadPool(failedNirmalBangSymbols.size());
        CountDownLatch countDownLatch = new CountDownLatch(failedNirmalBangSymbols.size());

        failedNirmalBangSymbols.forEach(v -> {

            CollectDataThread collectDataThread = new CollectDataThread(v, failedNirmalBangSymbols, countDownLatch);
            executorService.submit(collectDataThread);
        });

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(0);
        }

        System.out.println("Remaining failed jobs");

        failedNirmalBangSymbols.forEach(System.out::println);

        executorService.shutdown();
    }

    public static List<String> getFailedNirmalBangSymbols() {
        return failedNirmalBangSymbols;
    }

    public static String getCurrentRunningCode() {
        return currentRunningCode;
    }
}
