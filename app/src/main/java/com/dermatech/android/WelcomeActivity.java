package com.dermatech.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dermatech.android.admin.HomeAdminActivity;
import com.dermatech.android.medical_consultant.HomeMedicalConsultantActivity;
import com.dermatech.android.patient.HomePatientActivity;
import com.google.firebase.auth.FirebaseAuth;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
           String userType = getSharedPreferences("com.dermatech.android", MODE_PRIVATE).getString("userType", "");
            if(userType.equals("Patient")){
                Intent intent = new Intent(WelcomeActivity.this, HomePatientActivity.class);
                startActivity(intent);
            }else if(userType.equals("MedicalConsultant")){
                Intent intent = new Intent(WelcomeActivity.this, HomeMedicalConsultantActivity.class);
                startActivity(intent);
            }else if(userType.equals("Admin")){
                Intent intent = new Intent(WelcomeActivity.this, HomeAdminActivity.class);
                startActivity(intent);
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getSupportActionBar().hide();

        Button buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        Button buttonRegister = findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this,SelectUserTypeActivity.class);
                startActivity(intent);
            }
        });
    }
}