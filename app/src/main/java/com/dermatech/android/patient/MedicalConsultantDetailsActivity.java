package com.dermatech.android.patient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dermatech.android.ImageUtils;
import com.dermatech.android.R;
import com.dermatech.android.chat.ChatingActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MedicalConsultantDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_consultant_details);
        getSupportActionBar().hide();

        ImageView backArrow = findViewById(R.id.backArrow);
        ImageView doctorImage = findViewById(R.id.doctorImage);
        ImageView degree_credential = findViewById(R.id.degree_credential);
        TextView name = findViewById(R.id.name);
        TextView phone = findViewById(R.id.phone);
        TextView email = findViewById(R.id.email);
        Button consultNow = findViewById(R.id.consultNow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        doctorImage.setImageBitmap(ImageUtils.convert(AdapterSugestedMedicalConsultant.clickedMedicalConsultant.consultantImage+""));
        degree_credential.setImageBitmap(ImageUtils.convert(AdapterSugestedMedicalConsultant.clickedMedicalConsultant.degree_credential+""));
        name.setText(AdapterSugestedMedicalConsultant.clickedMedicalConsultant.username);
        phone.setText(AdapterSugestedMedicalConsultant.clickedMedicalConsultant.phone);
        email.setText(AdapterSugestedMedicalConsultant.clickedMedicalConsultant.email);
        consultNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ChatingActivity.class);
                intent.putExtra("startChat",true);
                intent.putExtra("patientId", FirebaseAuth.getInstance().getCurrentUser().getUid());
                intent.putExtra("medicalConsultantId",AdapterSugestedMedicalConsultant.clickedMedicalConsultant.id);
                startActivity(intent);
            }
        });


    }
}