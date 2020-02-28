package com.maxpetroleum.tmapp.Model;

public class Rate {
    String name;
    Long rate;

    public Rate() {
    }

    public Rate(String name, Long rate) {
        this.name = name;
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getRate() {
        return rate;
    }

    public void setRate(Long rate) {
        this.rate = rate;
    }
}
