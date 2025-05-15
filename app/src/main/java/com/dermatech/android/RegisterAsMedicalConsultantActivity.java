package com.dermatech.android;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

public class RegisterAsMedicalConsultantActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    ImageView consultantImage,degree_credential;
    String base64ConsultantImage = "",base64_degree_credential = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_as_medical_consultant);
        getSupportActionBar().hide();
        ImageView backArrow = findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
         consultantImage = findViewById(R.id.consultantImage);
         degree_credential = findViewById(R.id.degree_credential);
        Spinner gender = findViewById(R.id.gender);
        EditText national_id = findViewById(R.id.national_id);
        EditText name = findViewById(R.id.name);
        EditText phone = findViewById(R.id.phone);
        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);
        Button register = findViewById(R.id.register);

        consultantImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                launcherConsultantImage.launch(intent);
            }
        });



        degree_credential.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                launcherDegreeCredential.launch(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(base64ConsultantImage.isEmpty()){
                    Toast.makeText(RegisterAsMedicalConsultantActivity.this, "Select User Image", Toast.LENGTH_SHORT).show();
                }else if(base64_degree_credential.isEmpty()){
                    Toast.makeText(RegisterAsMedicalConsultantActivity.this, "Select degree credential image", Toast.LENGTH_SHORT).show();
                }if (name.getText().toString().isEmpty()) {
                    name.setError("Enter name");
                } else if (phone.getText().toString().isEmpty()) {
                    phone.setError("Enter phone");
                } else if (national_id.getText().toString().isEmpty()) {
                    national_id.setError("Enter national id");
                }else if (gender.getSelectedItem().toString().equals("Gender")) {
                    Toast.makeText(RegisterAsMedicalConsultantActivity.this, "Select your gender", Toast.LENGTH_SHORT).show();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()) {
                    email.setError("Enter valid email address");
                } else if (password.getText().toString().length() < 6) {
                    password.setError("Enter password with min 6 char");
                } else {
                    mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(RegisterAsMedicalConsultantActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        User user = new User(mAuth.getCurrentUser().getUid(), name.getText().toString(), phone.getText().toString(), email.getText().toString(), "MedicalConsultant",base64ConsultantImage,base64_degree_credential,gender.getSelectedItem().toString(),national_id.getText().toString(),"Registered");
                                        mDatabase.child("users").child(user.id).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(RegisterAsMedicalConsultantActivity.this, "Register Successfully", Toast.LENGTH_SHORT).show();

                                                } else {
                                                    Toast.makeText(RegisterAsMedicalConsultantActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(RegisterAsMedicalConsultantActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

    }

    ActivityResultLauncher<Intent> launcherConsultantImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult o) {
            if(o.getResultCode() == RESULT_OK){
                Uri imageUri = o.getData().getData();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    base64ConsultantImage = ImageUtils.convert(bitmap);
                    consultantImage.setImageURI(o.getData().getData());

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    });
    ActivityResultLauncher<Intent> launcherDegreeCredential = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult o) {
            if(o.getResultCode() == RESULT_OK){
                try {
                    Uri imageUri = o.getData().getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    base64_degree_credential = ImageUtils.convert(bitmap);
                    degree_credential.setImageURI(o.getData().getData());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }            }
        }
    });
}