package com.easyinvest.dtos;

import com.easyinvest.enums.Sex;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class UserCreateDTO {

    @NotBlank(message = "Nome obrigatorio")
    @Size(min = 3)
    private String name;

    @NotBlank(message = "E-mail obrigatorio")
    @Email(message = "E-mail inválido")
    private String email;

    @NotBlank
    @Size(min = 6, message = "A senha inválida")
    private String password;

    @NotBlank(message = "CPF Obrigatório")
    @Size(min = 11, max = 11, message = "CPF inválido")
    private String cpf;

    @NotBlank(message = "Endereço obrigatório")
    private String address;

    @NotBlank(message = "Telefone obrigatório")
    @Size(min = 11, message = "Telefone completo com DDD")
    private String phone;

    @NotNull(message = "Informe o sexo")
    private Sex sex;

    public UserCreateDTO() {}

    public UserCreateDTO(
            String name,
            String email,
            String password,
            String cpf,
            String address,
            String phone,
            Sex sex
    ) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.cpf = cpf;
        this.address = address;
        this.phone = phone;
        this.sex = sex;
    }

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

    public Sex getSex(){return sex;}

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

    public void setSex(Sex sex) {
        this.sex = sex;
    }
}