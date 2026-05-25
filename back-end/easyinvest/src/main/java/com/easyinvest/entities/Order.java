package com.easyinvest.entities;


import com.easyinvest.enums.TradeType;
import com.easyinvest.enums.TransactionType;
import com.easyinvest.exceptions.AssetNotFoundException;
import com.easyinvest.exceptions.InvalidQuantityException;
import com.easyinvest.exceptions.InvalidTransactionTypeException;
import com.easyinvest.exceptions.UserNotFoundException;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "asset_id",  nullable = false)
    private Asset asset;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(nullable = false)
    private BigDecimal priceAtPurchase;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, updatable = false)
    private LocalDateTime operationDate;

    @Enumerated(EnumType.STRING)
    private TradeType tradeType;

    public Order(User user, TransactionType transactionType, BigDecimal priceAtPurchase,
                 Integer quantity) {
        this.user = user;
        this.transactionType = transactionType;
        this.priceAtPurchase = priceAtPurchase;
        this.quantity = quantity;
    }

    public Order() {
    }

    public UUID getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void changeUser(User user) {
        if (user == null) {
            throw new UserNotFoundException("Usuário obrigatório");
        }
        this.user = user;
    }

    public Asset getAsset() {
        return asset;
    }

    public void changeAsset(Asset asset) {
        if (asset == null) {
            throw new AssetNotFoundException("Ativo obrigatório");
        }
        this.asset = asset;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void changeTransactionType(TransactionType transactionType) {
        if (transactionType == null) {
            throw new InvalidTransactionTypeException("Tipo de transação obrigatório");
        }
        this.transactionType = transactionType;
    }

    public BigDecimal getPriceAtPurchase() {
        return priceAtPurchase;
    }

    public void changePriceAtPurchase(BigDecimal priceAtPurchase) {
        if (priceAtPurchase == null || priceAtPurchase.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Preço inválido");
        }
        this.priceAtPurchase = priceAtPurchase;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void changeQuantity(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new InvalidQuantityException("Quantidade inválida");
        }
        this.quantity = quantity;
    }

    public LocalDateTime getOperationDate() {
        return operationDate;
    }

    public void changeOperationDate(LocalDateTime operationDate) {
        if (operationDate == null) {
            throw new IllegalArgumentException("Data inválida");
        }
        this.operationDate = operationDate;
    }

    @PrePersist
    public void prePersist() {
        this.operationDate = LocalDateTime.now();
    }

    public TradeType getTradeType() {
        return tradeType;
    }

    public void changeTradeType(TradeType tradeType) {
        if (tradeType == null) {
            throw new IllegalArgumentException("Tipo de Trade Obrigatório");
        }
        this.tradeType = tradeType;
    }
}