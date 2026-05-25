package com.easyinvest.entities;

import com.easyinvest.exceptions.InsufficientBalanceException;
import jakarta.persistence.*;
import org.springframework.http.HttpStatus;

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

    public Wallet() {
    }

    public User getUser() {
        return user;
    }

    public java.util.UUID getId() {
        return id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void addBalance(BigDecimal value) {
        this.balance = this.balance.add(value);
    }

    public void credit(BigDecimal amount) {
        if (amount == null|| amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor inválido");
        }
        this.balance = this.balance.add(amount);
    }

    public void debit(BigDecimal amount){
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor inválido");
        }

        if (this.balance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Saldo Insuficiente");
        }

        this.balance = this.balance.subtract(amount);
    }
}
