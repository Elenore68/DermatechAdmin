package com.dermatech.android.patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dermatech.android.ImageUtils;
import com.dermatech.android.R;
import com.dermatech.android.model.Drug;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DiagnosisDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosis_details);
        getSupportActionBar().hide();

        ImageView backArrow = findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ImageView img = findViewById(R.id.img);
        TextView diseaseName = findViewById(R.id.diseaseName);
        TextView dateTime = findViewById(R.id.dateTime);

        img.setImageBitmap(ImageUtils.convert(AdapterHistory.clickedDiagnosis.skinDiseaseImage));
        diseaseName.setText(AdapterHistory.clickedDiagnosis.skinDiseaseType);
        dateTime.setText(AdapterHistory.clickedDiagnosis.dateTime);
        RecyclerView recyclerViewQuestions = findViewById(R.id.recyclerViewQuestions);
        RecyclerView recyclerViewSuggestedDrugs = findViewById(R.id.recyclerViewSuggestedDrugs);
        recyclerViewQuestions.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        AdapterShowQuestion adapterShowQuestion = new AdapterShowQuestion(this,AdapterHistory.clickedDiagnosis.questions);
        recyclerViewQuestions.setAdapter(adapterShowQuestion);
        ArrayList<Drug> drugs = new ArrayList<>();

        recyclerViewSuggestedDrugs.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        AdapterShowSugestedDrug adapterShowSugestedDrug = new AdapterShowSugestedDrug(this,drugs);
        recyclerViewSuggestedDrugs.setAdapter(adapterShowSugestedDrug);
        FirebaseDatabase.getInstance().getReference().child("drugs").orderByChild("age").equalTo(AdapterHistory.clickedDiagnosis.age).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                drugs.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Drug drug = dataSnapshot.getValue(Drug.class);
                    drugs.add(drug);
                }
                adapterShowSugestedDrug.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}