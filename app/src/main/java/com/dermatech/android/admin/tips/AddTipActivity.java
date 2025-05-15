package com.dermatech.android.admin.tips;

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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.dermatech.android.ImageUtils;
import com.dermatech.android.R;
import com.dermatech.android.admin.articles.AddArticlesActivity;
import com.dermatech.android.model.Article;
import com.dermatech.android.model.Tip;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

public class AddTipActivity extends AppCompatActivity {
    ImageView addImage;
    String imageBase64;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tip);

        getSupportActionBar().hide();

        addImage = findViewById(R.id.addImage);
        Spinner types = findViewById(R.id.types);
        EditText tip = findViewById(R.id.tip);
        Button add = findViewById(R.id.add);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                launcherAddImage.launch(intent);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageBase64.isEmpty()){
                    Toast.makeText(AddTipActivity.this, "Select Tip Image", Toast.LENGTH_SHORT).show();
                }else if (types.getSelectedItem().equals("Select Skin Diseases Type")) {
                    Toast.makeText(AddTipActivity.this, "Select Skin Diseases Type", Toast.LENGTH_SHORT).show();
                } else if (tip.getText().toString().isEmpty()) {
                    tip.setError("Enter tip");
                } else {
                    String id = FirebaseDatabase.getInstance().getReference().child("tips").push().getKey();
                    Tip tipObject = new Tip(id,types.getSelectedItem().toString(),tip.getText().toString(),imageBase64);
                    FirebaseDatabase.getInstance().getReference().child("tips").child(tipObject.id).setValue(tipObject).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(AddTipActivity.this, "Tip Added Successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });
                }
            }
        });
    }
    ActivityResultLauncher<Intent> launcherAddImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if(o.getResultCode() == RESULT_OK){
                        try {
                            Uri imageUri = o.getData().getData();
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                            imageBase64 = ImageUtils.convert(bitmap);
                            addImage.setImageURI(o.getData().getData());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
}