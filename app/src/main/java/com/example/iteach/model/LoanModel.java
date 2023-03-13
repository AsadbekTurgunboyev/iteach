package com.example.iteach.model;

public class LoanModel {

    String store_name, product_price, overall_price, payment, money_left;

    public LoanModel(String store_name, String product_price, String overall_price, String payment, String money_left) {
        this.store_name = store_name;
        this.product_price = product_price;
        this.overall_price = overall_price;
        this.payment = payment;
        this.money_left = money_left;
    }

    public LoanModel() {
    }

    public String getOverall_price() {
        return overall_price;
    }

    public void setOverall_price(String overall_price) {
        this.overall_price = overall_price;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getMoney_left() {
        return money_left;
    }

    public void setMoney_left(String money_left) {
        this.money_left = money_left;
    }
}
