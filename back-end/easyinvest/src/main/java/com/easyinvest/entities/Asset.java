package com.easyinvest.entities;


import com.easyinvest.enums.AssetType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "assets")
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String ticker;

    @Column(nullable = false)
    private String companyName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssetType assetType;

    @Column(nullable = false)
    private BigDecimal currentPrice;

    public Asset(String ticker, String companyName, AssetType assetType,  BigDecimal currentPrice) {
        this.changeTicker(ticker);
        this.companyName = companyName;
        this.assetType = assetType;
        this.changeCurrentPrice(currentPrice);
    }

    public Asset() {}

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void changeCurrentPrice(BigDecimal currentPrice) {
        if (currentPrice == null || currentPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Preço inválido");
        }
        this.currentPrice = currentPrice;
    }

    public String getTicker() {
        return ticker;
    }

    public String getCompanyName() {
        return companyName;
    }

    public AssetType getAssetType() {
        return assetType;
    }

    public void changeTicker(String ticker) {
        if (ticker == null || ticker.isBlank()) {
            throw new IllegalArgumentException("Ticker inválido");
        }
        this.ticker = ticker.toUpperCase();
    }

    public void changeCompanyName(String companyName) {
        if (companyName == null || companyName.isBlank()) {
            throw new IllegalArgumentException("Nome da empresa inválido");
        }
        this.companyName = companyName;
    }

    public void setAssetType(AssetType assetType) {
        this.assetType = assetType;
    }
}
