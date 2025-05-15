package com.dermatech.android.admin.drugs;

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
import com.dermatech.android.admin.tips.AdapterTip;
import com.dermatech.android.admin.tips.TipDetailsActivity;
import com.dermatech.android.model.Drug;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

public class DrugDetailsActivity extends AppCompatActivity {
    ImageView addImage;
    String imageBase64;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug_detalis);
        getSupportActionBar().hide();
        addImage = findViewById(R.id.addImage);
        Spinner types = findViewById(R.id.types);
        EditText genericName = findViewById(R.id.genericName);
        EditText tradeName = findViewById(R.id.tradeName);
        EditText duration = findViewById(R.id.duration);
        EditText howToUse = findViewById(R.id.howToUse);
        Button update = findViewById(R.id.update);
        Button delete = findViewById(R.id.delete);
        
        //setData
        imageBase64 = AdapterDrug.clickedDrug.image;
        addImage.setImageBitmap(ImageUtils.convert(imageBase64));
        selectValue(types,AdapterDrug.clickedDrug.type);
        genericName.setText(AdapterDrug.clickedDrug.genericName);
        tradeName.setText(AdapterDrug.clickedDrug.tradeName);
        duration.setText(AdapterDrug.clickedDrug.duration);
        howToUse.setText(AdapterDrug.clickedDrug.howToUse);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                launcherAddImage.launch(intent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("drugs").child(AdapterDrug.clickedDrug.id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(DrugDetailsActivity.this, "Drug Deleted Successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageBase64.isEmpty()){
                    Toast.makeText(DrugDetailsActivity.this, "Select Drug Image", Toast.LENGTH_SHORT).show();
                }else if (types.getSelectedItem().equals("Select Skin Diseases Type")) {
                    Toast.makeText(DrugDetailsActivity.this, "Select Skin Diseases Type", Toast.LENGTH_SHORT).show();
                } else if (genericName.getText().toString().isEmpty()) {
                    genericName.setError("Enter generic Name");
                } else if (tradeName.getText().toString().isEmpty()) {
                    tradeName.setError("Enter trade Name");
                }else if (duration.getText().toString().isEmpty()) {
                    duration.setError("Enter duration");
                }else if (howToUse.getText().toString().isEmpty()) {
                    howToUse.setError("Enter how To Use");
                } else {
                    AdapterDrug.clickedDrug.tradeName = tradeName.getText().toString();
                    AdapterDrug.clickedDrug.genericName = genericName.getText().toString();
                    AdapterDrug.clickedDrug.type = types.getSelectedItem().toString();
                    AdapterDrug.clickedDrug.image = imageBase64;
                    AdapterDrug.clickedDrug.howToUse = howToUse.getText().toString();
                    AdapterDrug.clickedDrug.duration = duration.getText().toString();
                    FirebaseDatabase.getInstance().getReference().child("drugs").child(AdapterDrug.clickedDrug.id).setValue(AdapterDrug.clickedDrug).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(DrugDetailsActivity.this, "Drug Added Successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });
                }
            }
        });
    }
    private void selectValue(Spinner spinner, Object value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
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