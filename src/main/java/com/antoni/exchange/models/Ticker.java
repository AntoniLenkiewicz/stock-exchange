package com.antoni.exchange.models;

public class Ticker {
    private int id;
    private String name;

    public Ticker() {}

    public Ticker(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public void setId (int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }
}
