package com.kalavakuri.collectdata.info;

import java.io.Serializable;
import java.util.List;

public class FailedData implements Serializable {

    private List<String> failedNirmalBangSymbols;
    private List<String> completelyFailedSymbols;

    public List<String> getFailedNirmalBangSymbols() {
        return failedNirmalBangSymbols;
    }

    public void setFailedNirmalBangSymbols(List<String> failedNirmalBangSymbols) {
        this.failedNirmalBangSymbols = failedNirmalBangSymbols;
    }

    public List<String> getCompletelyFailedSymbols() {
        return completelyFailedSymbols;
    }

    public void setCompletelyFailedSymbols(List<String> completelyFailedSymbols) {
        this.completelyFailedSymbols = completelyFailedSymbols;
    }
}
