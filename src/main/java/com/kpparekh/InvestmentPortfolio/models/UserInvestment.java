package com.kpparekh.InvestmentPortfolio.models;

import java.time.LocalDate;

public class UserInvestment {

    private String quote;
    private double quantity;
    private double price;
    private double change;
    private LocalDate buyDate;
    private Long id;

    public UserInvestment(String quote, double quantity, double price, double change, LocalDate buyDate, Long id) {
        this.quote = quote;
        this.quantity = quantity;
        this.price = price;
        this.change = change;
        this.buyDate = buyDate;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(LocalDate buyDate) {
        this.buyDate = buyDate;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
