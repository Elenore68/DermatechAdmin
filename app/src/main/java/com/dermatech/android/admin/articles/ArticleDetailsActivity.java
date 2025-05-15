package com.dermatech.android.admin.articles;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dermatech.android.ImageUtils;
import com.dermatech.android.R;
import com.dermatech.android.WelcomeActivity;
import com.dermatech.android.admin.HomeAdminActivity;
import com.dermatech.android.model.Article;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

public class ArticleDetailsActivity extends AppCompatActivity {
    ImageView addImage;
    String imageBase64;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);
        getSupportActionBar().hide();

        addImage = findViewById(R.id.addImage);
        EditText title = findViewById(R.id.title);
        EditText brief = findViewById(R.id.brief);
        EditText details = findViewById(R.id.details);
        Button update = findViewById(R.id.update);
        Button delete = findViewById(R.id.delete);
        //set data
        imageBase64 = AdapterArticle.clickedArticle.image;
        addImage.setImageBitmap(ImageUtils.convert(imageBase64));
        title.setText(AdapterArticle.clickedArticle.title);
        brief.setText(AdapterArticle.clickedArticle.brief);
        details.setText(AdapterArticle.clickedArticle.details);
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
                new AlertDialog.Builder(ArticleDetailsActivity.this)
                        .setTitle("Delete")
                        .setMessage("Are you sure you want to delete this article?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase.getInstance().getReference().child("articles").child(AdapterArticle.clickedArticle.id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(ArticleDetailsActivity.this, "Article Deleted Successfully", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                            }
                        }).setNegativeButton("No", null).show();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageBase64.isEmpty()){
                    Toast.makeText(ArticleDetailsActivity.this, "Select Article Image", Toast.LENGTH_SHORT).show();
                }else if (title.getText().toString().isEmpty()) {
                    title.setError("Enter title");
                } else if (brief.getText().toString().isEmpty()) {
                    brief.setError("Enter brief");
                }else if (details.getText().toString().isEmpty()) {
                    details.setError("Enter details");
                } else {
                    AdapterArticle.clickedArticle.image = imageBase64;
                    AdapterArticle.clickedArticle.title = title.getText().toString();
                    AdapterArticle.clickedArticle.brief = brief.getText().toString();
                    AdapterArticle.clickedArticle.details = details.getText().toString();

                    FirebaseDatabase.getInstance().getReference().child("articles").child(AdapterArticle.clickedArticle.id).setValue(AdapterArticle.clickedArticle).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ArticleDetailsActivity.this, "Article Updated Successfully", Toast.LENGTH_SHORT).show();
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