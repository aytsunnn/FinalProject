package com.example.finalproject.data;

public class Book {
    private long id;
    private String title;
    private String description;
    private double price;
    private boolean favorite;
    private String favoriteDate;

    public Book(long id, String title, String description, double price, boolean b) {
        this(-1, title, description, price, false, null);
    }

    public Book(long id, String title, String desc, double price, boolean favorite, String favDate) {
        this.id = id;
        this.title = title;
        this.description = desc;
        this.price = price;
        this.favorite = favorite;
        this.favoriteDate = favDate;
    }

    public long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public boolean isFavorite() { return favorite; }
    public String getFavoriteDate() { return favoriteDate; }
    public void setFavoriteDate(String date) { this.favoriteDate = date; }

    public void setId(long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(double price) { this.price = price; }
    public void setFavorite(boolean favorite) { this.favorite = favorite; }
}
