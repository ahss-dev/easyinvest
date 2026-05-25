package com.easyinvest.dtos;

import java.math.BigDecimal;

public class PositionResponseDTO {

    private String ticker;
    private Integer quantity;
    private BigDecimal averagePrice;
    private BigDecimal totalInvested;
    private BigDecimal profitLoss;

    public PositionResponseDTO(String ticker, Integer quantity, BigDecimal averagePrice, BigDecimal totalInvested, BigDecimal profitLoss, BigDecimal loss) {
        this.ticker = ticker;
        this.quantity = quantity;
        this.averagePrice = averagePrice;
        this.totalInvested = totalInvested;
        this.profitLoss = profitLoss;
    }

    public String getTicker() {
        return ticker;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getAveragePrice() {
        return averagePrice;
    }

    public BigDecimal getTotalInvested() { return totalInvested; }

    public BigDecimal getProfitLoss() { return profitLoss; }
}