package com.dermatech.android.patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dermatech.android.ImageUtils;
import com.dermatech.android.R;
import com.dermatech.android.User;
import com.dermatech.android.admin.AdapterMedicalConsultant;
import com.dermatech.android.admin.drugs.AdapterDrug;
import com.dermatech.android.model.Drug;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DiagnosisResultActivity extends AppCompatActivity {
    ArrayList<Drug> drugs = new ArrayList<>();
    ArrayList<User> medicalConsultants = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosis_result);
        getSupportActionBar().hide();

        ImageView backArrow = findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
        ImageView imageViewForSkin = findViewById(R.id.imageViewForSkin);
        TextView skinResult = findViewById(R.id.skinResult);
        TextView skinDetailsResult = findViewById(R.id.skinDetailsResult);
        RecyclerView recyclerViewSuggestedDrugs = findViewById(R.id.recyclerViewSuggestedDrugs);
        RecyclerView recyclerViewSuggestedDoctors = findViewById(R.id.recyclerViewSuggestedDoctors);
        Button save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("diagnosis").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(ScanFragment.diagnosis.id).setValue(ScanFragment.diagnosis).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(DiagnosisResultActivity.this, "Diagnosis Saved Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });

        try {



        imageViewForSkin.setImageBitmap(ImageUtils.convert(ScanFragment.diagnosis.skinDiseaseImage));
        skinResult.setText("You have: "+ScanFragment.diagnosis.skinDiseaseType);
        if(ScanFragment.diagnosis.skinDiseaseType.equals("Eczema")){
            String some = "A chronic inflammatory skin condition causing dry, red, itchy patches";
            skinDetailsResult.setText(some);
        }else if(ScanFragment.diagnosis.skinDiseaseType.equals("Acne")){
            String some = "A common skin condition caused by clogged hair follicles with oil and dead skin cells";
            skinDetailsResult.setText(some);
        }else if(ScanFragment.diagnosis.skinDiseaseType.equals("Fungi Athlete Foot")){
            String some = "A fungal skin infection is usually found between the toes";
            skinDetailsResult.setText(some);
        }else if(ScanFragment.diagnosis.skinDiseaseType.equals("Fungi Nail Fungus")){
            String some = "A fungal infection that makes nails discolored, thick, and brittle. It often starts as a white or yellow spot under the nail";
            skinDetailsResult.setText(some);
        }else if(ScanFragment.diagnosis.skinDiseaseType.equals("Fungi Ringworm")){
            String some = "A contagious fungal infection that causes a red, circular, and itchy rash on the skin. Despite the name, it’s not caused by a worm.";
            skinDetailsResult.setText(some);
        }else if(ScanFragment.diagnosis.skinDiseaseType.equals("Unknown")){
            String some = "Eczema Sprinklr provides video call solution in integration with two vendors as of today: AWS (Amazon Web";
            skinDetailsResult.setText(some);
        }

        recyclerViewSuggestedDrugs.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        AdapterSugestedDrug adapterDrug = new AdapterSugestedDrug(this,drugs);
        recyclerViewSuggestedDrugs.setAdapter(adapterDrug);

        FirebaseDatabase.getInstance().getReference().child("drugs").orderByChild("age").equalTo(ScanFragment.diagnosis.age).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                drugs.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Drug drug = dataSnapshot.getValue(Drug.class);
                    if(drug.type.equals(ScanFragment.diagnosis.skinDiseaseType)) {
                        drugs.add(drug);
                    }
                }
                adapterDrug.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        recyclerViewSuggestedDoctors.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        AdapterSugestedMedicalConsultant adapterMedicalConsultant = new AdapterSugestedMedicalConsultant(this,medicalConsultants);
        recyclerViewSuggestedDoctors.setAdapter(adapterMedicalConsultant);

        FirebaseDatabase.getInstance().getReference().child("users").orderByChild("userType").equalTo("MedicalConsultant").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                medicalConsultants.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    if(user.status.equals("Accepted")) {
                        medicalConsultants.add(user);
                    }
                }
                adapterMedicalConsultant.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        }catch (Exception e){

        }
    }


}