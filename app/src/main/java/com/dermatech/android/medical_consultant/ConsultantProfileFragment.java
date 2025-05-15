package com.dermatech.android.medical_consultant;

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
import com.dermatech.android.R;
import com.dermatech.android.RegisterAsMedicalConsultantActivity;
import com.dermatech.android.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;


public class ConsultantProfileFragment extends Fragment {

    ImageView consultantImage,degree_credential;
    String base64ConsultantImage = "",base64_degree_credential = "";
    User user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_consultant_profile, container, false);

        consultantImage = view.findViewById(R.id.consultantImage);
        degree_credential = view.findViewById(R.id.degree_credential);
        consultantImage.setOnClickListener(new View.OnClickListener() {
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
        EditText national_id = view.findViewById(R.id.national_id);
        EditText gender = view.findViewById(R.id.gender);
        Button update = view.findViewById(R.id.update);

        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    user = task.getResult().getValue(User.class);
                    name.setText(user.username);
                    phone.setText(user.phone);
                    email.setText(user.email);
                    gender.setText(user.gender);
                    gender.setText(user.national_id);
                    base64ConsultantImage = user.consultantImage;
                    base64_degree_credential = user.degree_credential;
                    consultantImage.setImageBitmap(ImageUtils.convert(user.consultantImage));
                    degree_credential.setImageBitmap(ImageUtils.convert(user.degree_credential));
                }
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(base64ConsultantImage.isEmpty()){
                    Toast.makeText(getActivity(), "Select User Image", Toast.LENGTH_SHORT).show();
                }else if(base64_degree_credential.isEmpty()){
                    Toast.makeText(getActivity(), "Select degree credential image", Toast.LENGTH_SHORT).show();
                }if (name.getText().toString().isEmpty()) {
                    name.setError("Enter name");
                } else if (phone.getText().toString().isEmpty()) {
                    phone.setError("Enter phone");
                } else if (national_id.getText().toString().isEmpty()) {
                    national_id.setError("Enter national id");
                }else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()) {
                    email.setError("Enter valid email address");
                }  else {
                    user.username = name.getText().toString();
                    user.phone = phone.getText().toString();
                    user.consultantImage = base64ConsultantImage;
                    user.degree_credential = base64_degree_credential;
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
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                            base64_degree_credential = ImageUtils.convert(bitmap);
                            degree_credential.setImageURI(o.getData().getData());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }            }
                }
            });
}