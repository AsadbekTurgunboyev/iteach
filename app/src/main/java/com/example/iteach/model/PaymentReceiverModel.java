package com.example.iteach.model;

public class PaymentReceiverModel {
    public String name;
    public String surname;
    public String desc;
    public String key;
    public String money_left;

    public PaymentReceiverModel(String name, String surname, String desc, String key, String money_left) {
        this.name = name;
        this.surname = surname;
        this.desc = desc;
        this.key = key;
        this.money_left = money_left;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMoney_left() {
        return money_left;
    }

    public void setMoney_left(String money_left) {
        this.money_left = money_left;
    }

    public PaymentReceiverModel() {}
}
