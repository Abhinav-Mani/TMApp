package com.maxpetroleum.tmapp.Model;

public class Rate {
    String name;
    Double rate;

    public Rate() {
    }

    public Rate(String name, Double rate) {
        this.name = name;
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }
}
