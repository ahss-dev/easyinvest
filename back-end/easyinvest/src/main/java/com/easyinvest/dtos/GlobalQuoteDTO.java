package com.easyinvest.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GlobalQuoteDTO {

    @JsonProperty("05. price")
    private String price;

    public String getPrice() {
        return price;
    }
}
