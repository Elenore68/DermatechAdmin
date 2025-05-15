package com.dermatech.android.admin;

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
import com.dermatech.android.admin.articles.ManageArticlesFragment;
import com.dermatech.android.admin.drugs.ManageDrugsFragment;
import com.dermatech.android.admin.questions.ManageQuestionsFragment;
import com.dermatech.android.admin.tips.ManageTipsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeAdminActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);
        getSupportActionBar().hide();
        TextView screenTitle = findViewById(R.id.screenTitle);
        ImageView sign_out = findViewById(R.id.sign_out);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        screenTitle.setText("Home");
        getSupportFragmentManager().beginTransaction().replace(R.id.container,new ManageUsersFragment()).commit();


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.home){
                    screenTitle.setText("Home");
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,new ManageUsersFragment()).commit();
                }else if(item.getItemId() == R.id.articles){
                    screenTitle.setText("Articles");
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,new ManageArticlesFragment()).commit();
                }else if(item.getItemId() == R.id.tips){
                    screenTitle.setText("Tips");
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,new ManageTipsFragment()).commit();
                }else if(item.getItemId() == R.id.drugs){
                    screenTitle.setText("Drugs");
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,new ManageDrugsFragment()).commit();
                }else if(item.getItemId() == R.id.questions){
                    screenTitle.setText("Questions");
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,new ManageQuestionsFragment()).commit();
                }
                return true;
            }
        });
        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(HomeAdminActivity.this)
                        .setTitle("Closing application")
                        .setMessage("Are you sure you want to exit?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(HomeAdminActivity.this, WelcomeActivity.class);
                                startActivity(intent);
                            }
                        }).setNegativeButton("No", null).show();
            }
        });


    }


}