package com.example.onlinegyogyszertar;

import android.graphics.drawable.Drawable;

public class Product {

    private String name;
    private int price;
    private Drawable image;


    public Product(String name, int price, Drawable image) {
        this.name = name;
        this.price = price;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public Drawable getImage() {
        return this.image;
    }
}
