package com.dermatech.android.model;

public class Chat {
    public String id;
    public String patientID;
    public String medicalConsultantID;
    public int medicalConsultantCountUnRaed;
    public int patientCountUnRaed;

    public Chat(String id, String patientID, String medicalConsultantID, int medicalConsultantCountUnRaed, int patientCountUnRaed) {
        this.id = id;
        this.patientID = patientID;
        this.medicalConsultantID = medicalConsultantID;
        this.medicalConsultantCountUnRaed = medicalConsultantCountUnRaed;
        this.patientCountUnRaed = patientCountUnRaed;
    }

    public Chat(){};
}
