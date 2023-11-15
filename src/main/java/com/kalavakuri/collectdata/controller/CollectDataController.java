package com.kalavakuri.collectdata.controller;

import com.kalavakuri.collectdata.thread.TriggerThread;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CollectDataController {

    @GetMapping("/trigger")
    public String collectData() {

        TriggerThread triggerThread = new TriggerThread();
        Thread thread = new Thread(triggerThread);
        thread.start();

        return "Success";
    }

    @GetMapping("/getFailedCodes")
    public List<String> getFailedNirmalBangSymbols() {
        return TriggerThread.getFailedNirmalBangSymbols();
    }

    @GetMapping("/getCurrentRunningCode")
    public String getCurrentRunningCode() {
        return TriggerThread.getCurrentRunningCode();
    }
}
