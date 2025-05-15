package com.dermatech.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SelectUserTypeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user_type);
        getSupportActionBar().hide();
        Button patient = findViewById(R.id.patient);
        Button medicalConsultant = findViewById(R.id.medicalConsultant);
        medicalConsultant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectUserTypeActivity.this, RegisterAsMedicalConsultantActivity.class);
                startActivity(intent);
            }
        });
        patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectUserTypeActivity.this, RegisterAsPatientActivity.class);
                startActivity(intent);
            }
        });
    }
}