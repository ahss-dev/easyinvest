package com.easyinvest.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MarketResponseDTO {

    @JsonProperty("Global Quote")
    private GlobalQuoteDTO globalQuote;

    public GlobalQuoteDTO getGlobalQuote() {
        return globalQuote;
    }
}