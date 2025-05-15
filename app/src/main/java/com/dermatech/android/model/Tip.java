package com.dermatech.android.model;

public class Tip {
    public String id;
    public String type;
    public String tip;

    public String image;
    public Tip(){}
    public Tip(String id, String type, String tip, String image) {
        this.id = id;
        this.type = type;
        this.tip = tip;
        this.image = image;
    }
}
