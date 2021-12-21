package com.kpparekh.InvestmentPortfolio.models;

public class Graph {
    private String name;
    private double pv;
    private double Investment;

    public Graph(String name, double pv, double close) {
        this.name = name;
        this.pv = pv;
        this.Investment = close;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPv() {
        return pv;
    }

    public void setPv(double pv) {
        this.pv = pv;
    }

    public double getInvestment() {
        return Investment;
    }

    public void setInvestment(double investment) {
        this.Investment = investment;
    }
}
