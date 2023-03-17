package com.example.iteach.model;

public class Resource {

    String name;

    String store_name;
    String quantity;
    String price;

    String currency;
    String overall_price;
    String payment;

    public Resource() {
    }

    public Resource(String name, String store_name, String quantity, String price, String currency, String overall_price, String payment) {
        this.name = name;
        this.store_name = store_name;
        this.quantity = quantity;
        this.price = price;
        this.currency = currency;
        this.overall_price = overall_price;
        this.payment = payment;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getOverall_price() {
        return overall_price;
    }

    public void setOverall_price(String overall_price) {
        this.overall_price = overall_price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }
}
