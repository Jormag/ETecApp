package com.example.cristian.etecapp;


import java.util.ArrayList;

public class Card {
    private long id;
    private String name;
    private String price;
    private String description;
    private ArrayList<String> shopArray;
    private int color_resource;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getShopArray() {
        return shopArray;
    }

    public void setShopArray(ArrayList<String> shopArray) {
        this.shopArray = shopArray;
    }

    public int getColorResource() {
        return color_resource;
    }

    public void setColorResource(int color_resource) {
        this.color_resource = color_resource;
    }
}
