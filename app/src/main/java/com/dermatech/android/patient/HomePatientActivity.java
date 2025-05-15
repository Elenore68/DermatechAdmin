package com.dermatech.android.patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dermatech.android.R;
import com.dermatech.android.WelcomeActivity;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class HomePatientActivity extends AppCompatActivity {
   static DonutProgress donut_progress;
    static TextView screenTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_patient);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        getSupportActionBar().hide();
        screenTitle = findViewById(R.id.screenTitle);
        donut_progress = findViewById(R.id.donut_progress);
        ImageView sign_out = findViewById(R.id.sign_out);
        screenTitle.setText("Home");
        getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();
        RelativeLayout titleSection  = findViewById(R.id.titleSection);
        titleSection.setVisibility(View.GONE);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                sign_out.setVisibility(View.GONE);
                donut_progress.setVisibility(View.GONE);
                if(item.getItemId() == R.id.home){
                    titleSection.setVisibility(View.GONE);
                    screenTitle.setText("Home");
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();
                }else if(item.getItemId() == R.id.chats){
                    titleSection.setVisibility(View.VISIBLE);

                    screenTitle.setText("Chats");
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,new ChatsFragment()).commit();
                }else if(item.getItemId() == R.id.scan){
                    titleSection.setVisibility(View.VISIBLE);
                    donut_progress.setVisibility(View.VISIBLE);
                    donut_progress.setProgress(1);
                    screenTitle.setText("Scan");
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,new ScanFragment()).commit();
                }else if(item.getItemId() == R.id.history){
                    titleSection.setVisibility(View.VISIBLE);
                    screenTitle.setText("History");
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,new HistoryFragment()).commit();
                }else if(item.getItemId() == R.id.profile){
                    titleSection.setVisibility(View.VISIBLE);
                    sign_out.setVisibility(View.VISIBLE);
                    screenTitle.setText("Profile");
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,new PatientProfileFragment()).commit();
                }
                return true;
            }
        });

        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(HomePatientActivity.this)
                        .setTitle("Closing application")
                        .setMessage("Are you sure you want to exit?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(HomePatientActivity.this, WelcomeActivity.class);
                                startActivity(intent);
                            }
                        }).setNegativeButton("No", null).show();
            }
        });
    }
}