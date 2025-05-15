package com.dermatech.android.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dermatech.android.ImageUtils;
import com.dermatech.android.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

public class MedicalConsultantAcceptOrRejectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_consultant_accept_or_reject);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView gender = findViewById(R.id.gender);
        TextView national_id = findViewById(R.id.national_id);
        TextView name = findViewById(R.id.name);
        TextView phone = findViewById(R.id.phone);
        TextView email = findViewById(R.id.email);
        ImageView consultantImage = findViewById(R.id.consultantImage);
        ImageView  degree_credential = findViewById(R.id.degree_credential);

        Button accept = findViewById(R.id.accept);
        Button reject = findViewById(R.id.reject);


        //set data
        gender.setText(AdapterMedicalConsultant.clickedMedicalConsultant.gender);
        national_id.setText(AdapterMedicalConsultant.clickedMedicalConsultant.national_id);
        name.setText(AdapterMedicalConsultant.clickedMedicalConsultant.username);
        getSupportActionBar().setTitle(AdapterMedicalConsultant.clickedMedicalConsultant.username);
        phone.setText(AdapterMedicalConsultant.clickedMedicalConsultant.phone);
        email.setText(AdapterMedicalConsultant.clickedMedicalConsultant.email);
        consultantImage.setImageBitmap(ImageUtils.convert(AdapterMedicalConsultant.clickedMedicalConsultant.consultantImage));
        degree_credential.setImageBitmap(ImageUtils.convert(AdapterMedicalConsultant.clickedMedicalConsultant.degree_credential));
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("users").child(AdapterMedicalConsultant.clickedMedicalConsultant.id).child("status").setValue("Accepted").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(MedicalConsultantAcceptOrRejectActivity.this, "Medical Consultant Accepted Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("users").child(AdapterMedicalConsultant.clickedMedicalConsultant.id).child("status").setValue("Rejected").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(MedicalConsultantAcceptOrRejectActivity.this, "Medical Consultant Rejected", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });
    }
}