package com.dermatech.android.model;

import java.util.ArrayList;

public class Diagnosis {
    public String id;
    public String skinDiseaseType;
    public String skinDiseaseImage;
    public String dateTime;
    public ArrayList<Question> questions;
    public String uid;
    public String age;

    public Diagnosis() {
    }

    public Diagnosis(String id, String skinDiseaseType, String skinDiseaseImage, String dateTime, ArrayList<Question> questions,String uid,String age) {
        this.id = id;
        this.skinDiseaseType = skinDiseaseType;
        this.skinDiseaseImage = skinDiseaseImage;
        this.dateTime = dateTime;
        this.questions = questions;
        this.uid = uid;
        this.age = age;
    }
}
