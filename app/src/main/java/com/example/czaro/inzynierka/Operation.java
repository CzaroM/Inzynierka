package com.example.czaro.inzynierka;

public class Operation {
    private int id;
    private double price;
    private String category;
    private String balance;
    private String title;
    private String date;
    private String note;

    public Operation() {
    }

    public Operation(double price, String category, String balance, String title, String date, String note) {
        this.price = price;
        this.category = category;
        this.balance = balance;
        this.title = title;
        this.date = date;
        this.note = note;
    }

    public Operation(int id, double price, String category, String balance, String title, String date, String note) {
        this.id = id;
        this.price = price;
        this.category = category;
        this.balance = balance;
        this.title = title;
        this.date = date;
        this.note = note;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
