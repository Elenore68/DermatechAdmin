package com.dermatech.android.patient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dermatech.android.ImageUtils;
import com.dermatech.android.R;
import com.dermatech.android.admin.articles.AdapterArticle;

public class ArticleDetailsForPatientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details_for_patient);
        getSupportActionBar().hide();

        ImageView image = findViewById(R.id.image);
        ImageView backArrow = findViewById(R.id.backArrow);
        TextView title = findViewById(R.id.title);
        TextView brief = findViewById(R.id.brief);
        TextView details = findViewById(R.id.details);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        image.setImageBitmap(ImageUtils.convert(AdapterForPatientArticle.clickedArticle.image));
        title.setText(AdapterForPatientArticle.clickedArticle.title);
        brief.setText(AdapterForPatientArticle.clickedArticle.brief);
        details.setText(AdapterForPatientArticle.clickedArticle.details);



    }
}