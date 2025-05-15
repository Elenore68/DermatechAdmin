package com.dermatech.android.patient;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dermatech.android.ImageUtils;
import com.dermatech.android.LoginActivity;
import com.dermatech.android.R;
import com.dermatech.android.RegisterAsPatientActivity;
import com.dermatech.android.User;
import com.dermatech.android.admin.HomeAdminActivity;
import com.dermatech.android.medical_consultant.HomeMedicalConsultantActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;


public class PatientProfileFragment extends Fragment {

    ImageView image;
    String base64Image;
    User user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_patient_profile, container, false);

         image = view.findViewById(R.id.image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                launcherImage.launch(intent);
            }
        });


        EditText name = view.findViewById(R.id.name);
        EditText phone = view.findViewById(R.id.phone);
        EditText email = view.findViewById(R.id.email);
        Button update = view.findViewById(R.id.update);


        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    user = task.getResult().getValue(User.class);
                    name.setText(user.username);
                    phone.setText(user.phone);
                    email.setText(user.email);
                    if(user.image != null){
                        image.setImageBitmap(ImageUtils.convert(user.image));
                    }
                }
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().isEmpty()) {
                    name.setError("Enter name");
                } else if (phone.getText().toString().isEmpty()) {
                    phone.setError("Enter phone");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()) {
                    email.setError("Enter valid email address");
                }  else {
                    user.username = name.getText().toString();
                    user.phone = phone.getText().toString();
                    user.image = base64Image;
                    FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        return view;
    }
    ActivityResultLauncher<Intent> launcherImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if(o.getResultCode() == RESULT_OK){
                        Uri imageUri = o.getData().getData();

                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                            base64Image = ImageUtils.convert(bitmap);
                            image.setImageURI(o.getData().getData());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }
            });
}