package com.example.assessment2;

import com.google.firebase.database.PropertyName;

public class BasketItem {
    // Declarații de câmpuri pentru fiecare atribut al unui element din coș
    private String name;
    private String image;
    private String artistName;
    private float price;
    private int quantity;
    private int id;
    private float total;

    // Constructor care inițializează toate câmpurile
    public BasketItem(String name, String artistName, String image, float price, int quantity, int id, float total) {
        this.name = name;
        this.image = image;
        this.price = price;
        this.quantity = quantity;
        this.id = id;
        this.artistName = artistName;
        this.total = total;
    }

    // Getter pentru câmpul total
    public float getTotal() {
        return total;
    }

    // Setter pentru câmpul total
    public void setTotal(float total) {
        this.total = total;
    }

    // Setter pentru câmpul name
    public void setName(String name) {
        this.name = name;
    }

    // Setter pentru câmpul image
    public void setImage(String image) {
        this.image = image;
    }

    // Setter pentru câmpul artistName
    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    // Setter pentru câmpul price
    public void setPrice(float price) {
        this.price = price;
    }

    // Setter pentru câmpul quantity
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Setter pentru câmpul id
    public void setId(int id) {
        this.id = id;
    }

    // Constructor implicit necesar pentru Firebase
    public BasketItem() {
        // Constructor implicit
    }

    // Getter pentru câmpul name
    public String getName() {
        return name;
    }

    // Getter pentru câmpul image
    public String getImage() {
        return image;
    }

    // Getter pentru câmpul artistName
    public String getArtistName() {
        return artistName;
    }

    // Getter pentru câmpul price
    public float getPrice() {
        return price;
    }

    // Getter pentru câmpul quantity
    public int getQuantity() {
        return quantity;
    }

    // Getter pentru câmpul id
    public int getId() {
        return id;
    }
}
