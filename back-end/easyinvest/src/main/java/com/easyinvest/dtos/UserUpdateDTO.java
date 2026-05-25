package com.easyinvest.dtos;

import com.easyinvest.enums.Sex;

public class UserUpdateDTO {

    private String name;
    private String email;
    private String phone;
    private String address;
    private Sex sex;

    public UserUpdateDTO(String rickAlves, String email, String number, String address, Sex masculino) {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Sex getSex() {return sex;}

    public void setSex(Sex sex) {this.sex = sex;}
}