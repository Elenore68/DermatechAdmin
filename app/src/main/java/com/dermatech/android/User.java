package com.dermatech.android;

public class User {
    public String id;
    public String username;

    public String phone;
    public String email;
    public String userType;
    public String consultantImage;
    public String degree_credential;
    public String gender;
    public String national_id;
    public String status;
    public String image;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String id, String username, String phone, String email, String userType) {
        this.id = id;
        this.username = username;
        this.phone = phone;
        this.email = email;
        this.userType = userType;
    }

    public User(String id, String username, String phone, String email, String userType, String consultantImage, String degree_credential, String gender, String national_id, String status) {
        this.id = id;
        this.username = username;
        this.phone = phone;
        this.email = email;
        this.userType = userType;
        this.consultantImage = consultantImage;
        this.degree_credential = degree_credential;
        this.gender = gender;
        this.national_id = national_id;
        this.status = status;
    }
}
