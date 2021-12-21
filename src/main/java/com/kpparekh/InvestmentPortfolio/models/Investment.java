package com.kpparekh.InvestmentPortfolio.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "investments")
public class Investment {
    private Long id;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id", unique=true, nullable=false)
    public Long getId() {
        return id;
    }

    @Column(name = "quote")
    private String quote;

    @Column(name = "quantity")
    private double quantity;

    @Column(name = "price")
    private double price;

    @Column(name = "change")
    private double change;

//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
//    @JsonFormat(pattern = "yyyy-mm-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "date")
    private LocalDate date;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "buydate")
    private LocalDate buydate;

    public LocalDate getBuydate() {
        return buydate;
    }

    public void setBuydate(LocalDate buydate) {
        this.buydate = buydate;
    }

    @Column(name = "userid")
    private long userid;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public Investment() {}

    public Investment(String quote, double quantity, double price, double change, LocalDate date, LocalDate buydate, long userid) {
        this.quote = quote;
        this.quantity = quantity;
        this.price = price;
        this.change = change;
        this.date = date;
        this.buydate = buydate;
        this.userid = userid;
    }

    public void setId(Long id) {
        this.id = id;
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

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }
}
