package com.easyinvest.entities;
import java.util.UUID;
import jakarta.persistence.*;
import com.easyinvest.enums.Sex;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private java.util.UUID id;
    private String name;
    private String password;
    @Column(unique = true, nullable = false)
    private String email;

    private String address;
    private String phone;
    @Column(unique = true, nullable = false)
    private String cpf;
    @Enumerated(EnumType.STRING)
    private Sex sex;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Wallet wallet;

    public User(String name, String email, String password, String cpf) {
        changeName(name);
        changeEmail(email);
        changePassword(password);
        changeCpf(cpf);
    }

    public User (){}

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void changeName(String name) {
        if (name == null || name.isBlank() || name.length() < 3) {
            throw new IllegalArgumentException("Digite um nome!");
        }
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void changePassword(String hashedPassword) {
        if (hashedPassword == null || hashedPassword.isBlank()) {
            throw new IllegalArgumentException("Senha inválida!");
        }
        this.password = hashedPassword;
    }

    public String getEmail() {
        return email;
    }

    public void changeEmail(String email) {
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new IllegalArgumentException("Digite um email!");
        }
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public Sex getSex() {return sex;}

    public void updateContactInfo(String phone, String address, Sex sex) {
        if ( phone.isBlank() || phone.length() != 11 || !phone.matches("\\d{11}") ) {
            throw new IllegalArgumentException("Digite um número válido!");
        } else if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Digite um endereço!");
        }
        this.address = address;
        this.sex = sex;
        this.phone = phone;
    }

    public String getCpf() {
        return cpf;
    }

    public void changeCpf(String cpf) {
        if (cpf == null || cpf.isBlank() || cpf.length() < 11) {
            throw new IllegalArgumentException("Digite um CPF!");
        }
        this.cpf = cpf;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }
}