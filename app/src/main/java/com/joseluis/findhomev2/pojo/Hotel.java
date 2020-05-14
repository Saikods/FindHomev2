package com.joseluis.findhomev2.pojo;

import java.util.ArrayList;

public class Hotel {
    private String hotel_name;
    private String street_name;
    private String description;
    private String city;
    private int tlp_number;
    private float rating;
    private float price_night;
    private ArrayList<String> images;
    private ArrayList<String> services;

    public Hotel() {
    }

    public Hotel(String hotel_name, String street_name, String description, String city, int tlp_number, float rating, float price_night, ArrayList<String> images, ArrayList<String> services) {
        this.hotel_name = hotel_name;
        this.street_name = street_name;
        this.description = description;
        this.city = city;
        this.tlp_number = tlp_number;
        this.rating = rating;
        this.price_night = price_night;
        this.images = images;
        this.services = services;
    }

    public String getHotel_name() {
        return hotel_name;
    }

    public String getStreet_name() {
        return street_name;
    }

    public String getDescription() {
        return description;
    }

    public String getCity() {
        return city;
    }

    public int getTlp_number() {
        return tlp_number;
    }

    public float getRating() {
        return rating;
    }

    public float getPrice_night() {
        return price_night;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public ArrayList<String> getServices() {
        return services;
    }
}
