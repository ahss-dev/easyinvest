package com.easyinvest.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "wallets")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private java.util.UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private BigDecimal balance;

    public Wallet(User user, BigDecimal balance) {
        this.user = user;
        this.balance = balance;
        user.setWallet(this);
    }

    public User getUser() {
        return user;
    }

    public java.util.UUID getId() { return id; }

    public BigDecimal getBalance() { return balance; }

    public void addBalance(BigDecimal value) {
        this.balance = this.balance.add(value);
    }

    public void subtractBalance(BigDecimal value) {
        this.balance = this.balance.subtract(value);
    }
}
