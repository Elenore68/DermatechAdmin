package com.dermatech.android.model;

public class Article {
    public String id;
    public String title;
    public String brief;
    public String details;
    public String image;
    public Article(){}
    public Article(String id, String title, String brief, String details, String image) {
        this.id = id;
        this.title = title;
        this.brief = brief;
        this.details = details;
        this.image = image;
    }
}
