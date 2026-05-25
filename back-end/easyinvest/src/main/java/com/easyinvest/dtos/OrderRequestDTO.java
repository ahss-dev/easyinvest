package com.easyinvest.dtos;

import com.easyinvest.entities.User;
import com.easyinvest.enums.TransactionType;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public class OrderRequestDTO {

    @NotNull
    private UUID assetId;

    @NotNull
    private Integer quantity;

    @NotNull
    private TransactionType transactionType;

    public UUID getAssetId() {
        return assetId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setAssetId(UUID assetId) {
        this.assetId = assetId;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }
}