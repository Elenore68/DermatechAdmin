package com.dermatech.android.medical_consultant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.dermatech.android.R;
import com.dermatech.android.model.Diagnosis;
import com.dermatech.android.patient.AdapterHistory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PatientHistoryActivity extends AppCompatActivity {
    ArrayList<Diagnosis> diagnoses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_history);
        getSupportActionBar().hide();

        String id = getIntent().getStringExtra("patientId");

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        AdapterHistory adapterHistory = new AdapterHistory(this,diagnoses);
        recyclerView.setAdapter(adapterHistory);

        ImageView backArrow = findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        FirebaseDatabase.getInstance().getReference().child("diagnosis").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                diagnoses.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Diagnosis diagnosisObj = dataSnapshot.getValue(Diagnosis.class);
                    diagnoses.add(diagnosisObj);
                }
                adapterHistory.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}