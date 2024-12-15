package com.example.danum.core.data.model;

public class Order {
    private final int imageResource;
    private final String title;
    private final String price;
    private final int quantity;
    private final String pickupTime;
    private final String status;

    public Order(int imageResource, String title, String price, int quantity, String pickupTime, String status) {
        this.imageResource = imageResource;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
        this.pickupTime = pickupTime;
        this.status = status;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getPickupTime() {
        return pickupTime;
    }

    public String getStatus() {
        return status;
    }
}
