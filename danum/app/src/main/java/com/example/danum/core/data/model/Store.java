package com.example.danum.core.data.model;

public class Store {
    private int id;
    private String name;
    private String address;
    private double latitude;
    private double longitude;
    private int type;
    private String imageResource;

    public Store(int id, String name, String address, double latitude, double longitude, int type, String imageResource) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.imageResource = imageResource;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getType() {
        return type;
    }

    public String getImageResource() {
        return imageResource;
    }
}
