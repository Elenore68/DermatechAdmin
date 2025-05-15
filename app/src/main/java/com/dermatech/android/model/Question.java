package com.dermatech.android.model;

public class Question {
    public String id;
    public String question;
    public String questionType;
    public String skinDiseaseType;
    public String answer;
    public String option1;
    public String option2;
    public String option3;


    public Question(String id, String question, String questionType, String skinDiseaseType) {
        this.id = id;
        this.question = question;
        this.questionType = questionType;
        this.skinDiseaseType = skinDiseaseType;
    }

    public Question(String id, String question, String questionType, String skinDiseaseType, String option1, String option2, String option3) {
        this.id = id;
        this.question = question;
        this.questionType = questionType;
        this.skinDiseaseType = skinDiseaseType;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;

    }

    public Question() {
    }
}
