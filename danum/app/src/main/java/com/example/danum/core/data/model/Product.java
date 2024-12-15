package com.example.danum.core.data.model;

public class Product {
    private int id;
    private String name;
    private int price;
    private int quantity;
    private int imageResource;

    public Product(int id, String name, int price, int quantity, int imageResource) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageResource = imageResource;
    }
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getImageResource() {
        return imageResource;
    }
}