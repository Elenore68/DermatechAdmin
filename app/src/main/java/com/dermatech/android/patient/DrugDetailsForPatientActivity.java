package com.dermatech.android.patient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dermatech.android.ImageUtils;
import com.dermatech.android.R;

public class DrugDetailsForPatientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug_details_for_patient);
        getSupportActionBar().hide();

        ImageView drugImage = findViewById(R.id.drugImage);
        ImageView backArrow = findViewById(R.id.backArrow);
        TextView genericName = findViewById(R.id.genericName);
        TextView tradeName = findViewById(R.id.tradeName);
        TextView duration = findViewById(R.id.duration);
        TextView howToUse = findViewById(R.id.howToUse);

        drugImage.setImageBitmap(ImageUtils.convert(AdapterSugestedDrug.clickedDrug.image));
        genericName.setText(AdapterSugestedDrug.clickedDrug.genericName);
        tradeName.setText(AdapterSugestedDrug.clickedDrug.tradeName);
        duration.setText(AdapterSugestedDrug.clickedDrug.duration);
        howToUse.setText(AdapterSugestedDrug.clickedDrug.howToUse);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}