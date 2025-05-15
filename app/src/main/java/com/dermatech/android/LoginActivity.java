package com.dermatech.android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dermatech.android.admin.HomeAdminActivity;
import com.dermatech.android.medical_consultant.HomeMedicalConsultantActivity;
import com.dermatech.android.patient.HomePatientActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
     private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        TextView forget = findViewById(R.id.forgetPassword);
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });
        ImageView backArrow = findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);
        Button login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        User user = task.getResult().getValue(User.class);
                                        if(user.userType.equals("Patient")){
                                            getSharedPreferences("com.dermatech.android",MODE_PRIVATE).edit().putString("userType",user.userType).commit();
                                            getSharedPreferences("com.dermatech.android",MODE_PRIVATE).edit().putString("username",user.username).commit();
                                            Intent intent = new Intent(LoginActivity.this, HomePatientActivity.class);
                                            startActivity(intent);
                                        }else if(user.userType.equals("MedicalConsultant")){
                                            getSharedPreferences("com.dermatech.android",MODE_PRIVATE).edit().putString("userType",user.userType).commit();
                                            Intent intent = new Intent(LoginActivity.this, HomeMedicalConsultantActivity.class);
                                            startActivity(intent);
                                        }else if(user.userType.equals("Admin")){
                                            getSharedPreferences("com.dermatech.android",MODE_PRIVATE).edit().putString("userType",user.userType).commit();
                                            Intent intent = new Intent(LoginActivity.this, HomeAdminActivity.class);
                                            startActivity(intent);
                                        }
                                        Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                        ;
            }
        });


    }
}