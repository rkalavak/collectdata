package com.kalavakuri.collectdata.thread;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class CollectDataThread implements Runnable {

    private final String nirmalBangCode;
    private final List<String> failedNirmalBangSymbols;
    private final CountDownLatch countDownLatch;

    public CollectDataThread(String nirmalBangCode, List<String> failedNirmalBangSymbols, CountDownLatch countDownLatch) {
        this.nirmalBangCode = nirmalBangCode;
        this.failedNirmalBangSymbols = failedNirmalBangSymbols;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {

        try {
            extractDocument(nirmalBangCode);
            failedNirmalBangSymbols.remove(nirmalBangCode);
        } catch (Exception e) {
            failedNirmalBangSymbols.add(nirmalBangCode);
        } finally {
            countDownLatch.countDown();
        }
    }

    private void extractDocument(String nirmalBangCode) throws IOException {

        Document doc = returnDocument(nirmalBangCode);

        Elements topw100 = doc.getElementsByClass("topw100");

        if (topw100.size() != 0) {

            String nseSymbol = topw100.get(0).getElementsByTag("span").get(0).text().split(":")[1].trim();

            if (!"NA".equalsIgnoreCase(nseSymbol)) {
                System.out.println(nseSymbol + "\t" + nirmalBangCode);
            }
        }
    }

    private Document returnDocument(String nirmalBangCode) throws IOException {

        Connection.Response response = Jsoup.connect("https://www.nirmalbang.com/company-profile/bonus-issues.aspx?finCode=" + nirmalBangCode)
                .ignoreContentType(true)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.75 Safari/537.36")
                .timeout(90 * 1000).header("Accept", "application/json")
                .followRedirects(true).maxBodySize(0).execute();

        return response.parse();
    }
}
