package com.dermatech.android.medical_consultant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dermatech.android.R;
import com.dermatech.android.WelcomeActivity;
import com.dermatech.android.patient.ChatsFragment;
import com.dermatech.android.patient.HistoryFragment;
import com.dermatech.android.patient.HomeFragment;
import com.dermatech.android.patient.HomePatientActivity;
import com.dermatech.android.patient.PatientProfileFragment;
import com.dermatech.android.patient.ScanFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeMedicalConsultantActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_medical_consultant);
        getSupportActionBar().hide();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        TextView screenTitle = findViewById(R.id.screenTitle);
        ImageView sign_out = findViewById(R.id.sign_out);
        screenTitle.setText("Chats");
        getSupportFragmentManager().beginTransaction().replace(R.id.container,new ChatsFragment()).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                sign_out.setVisibility(View.GONE);
              if(item.getItemId() == R.id.chats){
                    screenTitle.setText("Chats");
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,new ChatsFragment()).commit();
                }else if(item.getItemId() == R.id.profile){
                    sign_out.setVisibility(View.VISIBLE);
                    screenTitle.setText("Profile");
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,new ConsultantProfileFragment()).commit();
                }
                return true;
            }
        });

        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(HomeMedicalConsultantActivity.this)
                        .setTitle("Closing application")
                        .setMessage("Are you sure you want to exit?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(HomeMedicalConsultantActivity.this, WelcomeActivity.class);
                                startActivity(intent);
                            }
                        }).setNegativeButton("No", null).show();
            }
        });
    }
}