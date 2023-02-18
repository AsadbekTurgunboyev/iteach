package com.example.iteach.model;

public class PaymentReceiverModel {
    public String name;
    public String surname;
    public String desc;
    public String key;

    public PaymentReceiverModel(String name, String surname, String desc, String key) {
        this.name = name;
        this.surname = surname;
        this.desc = desc;
        this.key = key;
    }

    public PaymentReceiverModel() {}

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
}
