package com.example.todolist;

import com.google.gson.annotations.SerializedName;

public class Seller {

    @SerializedName("name")
    private String name;

    @SerializedName("price")
    private Double price;

    @SerializedName("city")
    private String city;

    @SerializedName("phone")
    private String phoneNumber;

    public Seller(String name, Double price, String city, String phoneNumber) {
        this.name = name;
        this.price = price;
        this.city = city;
        this.phoneNumber = phoneNumber;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Seller's name: " + name + '\n'+
                "price: " + price + '\n' +
                "city: " + city + '\n' +
                "phoneNumber: " + phoneNumber;
    }
}
