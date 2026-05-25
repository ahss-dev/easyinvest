package com.easyinvest.dtos;

import com.easyinvest.enums.TradeType;
import com.easyinvest.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderResponseDTO {

    private String ticker;
    private TransactionType transactionType;
    private Integer quantity;
    private BigDecimal price;
    private LocalDateTime operationDate;
    private TradeType tradeType;

    public OrderResponseDTO(String ticker, TradeType tradeType, TransactionType transactionType,
                            Integer quantity, BigDecimal price,
                            LocalDateTime operationDate) {
        this.ticker = ticker;
        this.transactionType = transactionType;
        this.quantity = quantity;
        this.price = price;
        this.operationDate = operationDate;
        this.tradeType = tradeType;
    }

    public String getTicker() {
        return ticker;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public LocalDateTime getOperationDate() {
        return operationDate;
    }

    public TradeType getTradeType() { return tradeType; }
}
