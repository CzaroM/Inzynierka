package com.example.czaro.inzynierka;

public class PermamentOperation{
    private int id;
    private double price;
    private String category;
    private String balance;
    private String title;
    private String date;
    private String note;
    private String frequency;
    private String endDate;

    public PermamentOperation() {
    }

    public PermamentOperation(double price, String category, String balance, String title, String date, String note, String frequency, String endDate) {
        this.price = price;
        this.category = category;
        this.balance = balance;
        this.title = title;
        this.date = date;
        this.note = note;
        this.frequency = frequency;
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
