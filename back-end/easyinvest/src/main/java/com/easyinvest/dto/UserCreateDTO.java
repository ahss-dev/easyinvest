package com.easyinvest.dto;

import com.easyinvest.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserCreateDTO {

    @NotBlank(message = "Nome obrigatorio")
    private String name;

    @NotBlank(message = "E-mail obrigatorio")
    @Email(message = "E-mail inválido")
    private String email;

    @NotBlank
    @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres")
    private String password;

    @NotBlank(message = "CPF Obrigatório")
    private String cpf;

    @NotNull(message = "Endereço obrigatório")
    private String address;

    @NotNull(message = "Telefone obrigatório")
    private String phone;

    public UserCreateDTO() {}

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getCpf() {
        return cpf;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}