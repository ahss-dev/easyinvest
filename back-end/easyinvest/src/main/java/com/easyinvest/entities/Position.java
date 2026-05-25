package com.easyinvest.entities;

import com.easyinvest.exceptions.InvalidQuantityException;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Entity
@Table(name = "positions")
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Asset asset;

    private Integer quantity = 0;

    private BigDecimal averagePrice;

    private BigDecimal profit;

    public Position() {}

    public Position(User user, Asset asset) {
        this.user = user;
        this.asset = asset;
        this.quantity = 0;
    }

    public UUID getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Asset getAsset() {
        return asset;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void addQuantity(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new InvalidQuantityException("Quantidade inválida");
        }
        this.quantity += quantity;
    }

    public void removeQuantity(Integer quantity) {
        if (quantity > this.quantity) {
            throw new InvalidQuantityException("Quantidade maior do que a posição comprada");
        }
        this.quantity -= quantity;
    }

    public boolean isEmpty() {
        return this.quantity == 0;
    }

    public BigDecimal getAveragePrice() {
        return averagePrice;
    }

    public void updateAveragePrice(BigDecimal newPrice, Integer newQuantity) {

        if (this.quantity == 0) {
            this.averagePrice = newPrice;
            return;
        }

        BigDecimal currentTotal = this.averagePrice.multiply(BigDecimal.valueOf(this.quantity));

        BigDecimal newTotal = newPrice.multiply(BigDecimal.valueOf(newQuantity));

        BigDecimal investedTotal = currentTotal.add(newTotal);

        Integer quantityTotal = this.quantity += newQuantity;

        this.averagePrice = investedTotal.divide(BigDecimal.valueOf(quantityTotal), 2, RoundingMode.HALF_UP);
    }
}
