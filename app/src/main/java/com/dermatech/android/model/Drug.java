package com.dermatech.android.model;

public class Drug {
    public String id;
    public String genericName;
    public String tradeName;
    public String duration;
    public String howToUse;
    public String type;
    public String age;

    public String image;

    public Drug() {
    }

    public Drug(String id, String genericName, String tradeName, String duration, String howToUse, String type, String age, String image) {
        this.id = id;
        this.genericName = genericName;
        this.tradeName = tradeName;
        this.duration = duration;
        this.howToUse = howToUse;
        this.type = type;
        this.age = age;
        this.image = image;
    }
}
