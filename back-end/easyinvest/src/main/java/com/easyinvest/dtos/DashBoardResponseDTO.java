package com.easyinvest.dtos;

import java.math.BigDecimal;

public class DashBoardResponseDTO {

    private String userName;
    private BigDecimal walletBalance;
    private BigDecimal totalInvested;
    private BigDecimal totalEquity;
    private BigDecimal totalProfitLoss;

    public DashBoardResponseDTO(String userName, BigDecimal walletBalance,
                                BigDecimal totalInvested, BigDecimal totalEquity,
                                BigDecimal totalProfitLoss) {
        this.userName = userName;
        this.walletBalance = walletBalance;
        this.totalInvested = totalInvested;
        this.totalEquity = totalEquity;
        this.totalProfitLoss = totalProfitLoss;
    }

    public String getUserName() {
        return userName;
    }

    public BigDecimal getWalletBalance() {
        return walletBalance;
    }

    public BigDecimal getTotalInvested() {
        return totalInvested;
    }

    public BigDecimal getTotalEquity() {
        return totalEquity;
    }

    public BigDecimal getTotalProfitLoss() {
        return totalProfitLoss;
    }
}
