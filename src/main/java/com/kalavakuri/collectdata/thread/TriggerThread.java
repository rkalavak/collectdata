package com.kalavakuri.collectdata.thread;

import com.kalavakuri.collectdata.info.FailedData;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TriggerThread implements Runnable {

    private static List<String> failedNirmalBangSymbols = null;
    private static String currentRunningCode = null;
    private static List<String> completelyFailedSymbols = null;

    @Override
    public void run() {

        failedNirmalBangSymbols = new CopyOnWriteArrayList<>();
        completelyFailedSymbols = new CopyOnWriteArrayList<>();

        String nirmalBangCode;

        for (double i = 100000; i <= 999999; i = i + 2001) {

            ExecutorService executorService = Executors.newFixedThreadPool(2000);
            CountDownLatch countDownLatch = new CountDownLatch(2000);

            for (double j = i; (j <= i + 2000 && j <= 999999); j++) {

                nirmalBangCode = String.valueOf(j).replace(".0", "");
                currentRunningCode = nirmalBangCode;
                CollectDataThread collectDataThread = new CollectDataThread(nirmalBangCode, failedNirmalBangSymbols, countDownLatch, completelyFailedSymbols);
                executorService.submit(collectDataThread);
            }

            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(0);
            }

            executorService.shutdown();

            while (failedNirmalBangSymbols.size() != 0) {

                ExecutorService executorServiceRetry = Executors.newFixedThreadPool(failedNirmalBangSymbols.size());
                CountDownLatch countDownLatchRetry = new CountDownLatch(failedNirmalBangSymbols.size());

                failedNirmalBangSymbols.forEach(v -> {

                    CollectDataThread collectDataThread = new CollectDataThread(v, failedNirmalBangSymbols, countDownLatchRetry, completelyFailedSymbols);
                    executorServiceRetry.submit(collectDataThread);
                });

                try {
                    countDownLatchRetry.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.exit(0);
                }

                executorServiceRetry.shutdown();
            }
        }
    }

    public static FailedData getFailedNirmalBangSymbols() {

        FailedData failedData = new FailedData();
        failedData.setFailedNirmalBangSymbols(failedNirmalBangSymbols);
        failedData.setCompletelyFailedSymbols(completelyFailedSymbols);
        return failedData;
    }

    public static String getCurrentRunningCode() {
        return currentRunningCode;
    }
}
