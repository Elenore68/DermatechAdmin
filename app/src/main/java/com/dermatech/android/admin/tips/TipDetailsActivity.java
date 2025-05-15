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
import com.dermatech.android.admin.articles.AdapterArticle;
import com.dermatech.android.model.Tip;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

public class TipDetailsActivity extends AppCompatActivity {
    ImageView addImage;
    String imageBase64;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_details);
        getSupportActionBar().hide();

        addImage = findViewById(R.id.addImage);
        Spinner types = findViewById(R.id.types);
        EditText tip = findViewById(R.id.tip);
        Button delete = findViewById(R.id.delete);
        Button update = findViewById(R.id.update);

        imageBase64 = AdapterTip.clickedTip.image;
        addImage.setImageBitmap(ImageUtils.convert(imageBase64));
        selectValue(types,AdapterTip.clickedTip.type);
        tip.setText(AdapterTip.clickedTip.tip);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("tips").child(AdapterTip.clickedTip.id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(TipDetailsActivity.this, "Tip Deleted Successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
            }
        });

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                launcherAddImage.launch(intent);
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageBase64.isEmpty()){
                    Toast.makeText(TipDetailsActivity.this, "Select Tip Image", Toast.LENGTH_SHORT).show();
                }else if (types.getSelectedItem().equals("Select Skin Diseases Type")) {
                    Toast.makeText(TipDetailsActivity.this, "Select Skin Diseases Type", Toast.LENGTH_SHORT).show();
                } else if (tip.getText().toString().isEmpty()) {
                    tip.setError("Enter tip");
                } else {
                   AdapterTip.clickedTip.tip = tip.getText().toString();
                   AdapterTip.clickedTip.type = types.getSelectedItem().toString();
                   AdapterTip.clickedTip.image = imageBase64;
                    FirebaseDatabase.getInstance().getReference().child("tips").child(AdapterTip.clickedTip.id).setValue(AdapterTip.clickedTip).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(TipDetailsActivity.this, "Tip Updated Successfully", Toast.LENGTH_SHORT).show();
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