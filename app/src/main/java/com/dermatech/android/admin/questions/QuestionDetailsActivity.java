package com.dermatech.android.admin.questions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.dermatech.android.R;
import com.dermatech.android.admin.drugs.AdapterDrug;
import com.dermatech.android.admin.drugs.DrugDetailsActivity;
import com.dermatech.android.model.Question;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

public class QuestionDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_details);
        getSupportActionBar().hide();


        EditText question = findViewById(R.id.question);
        Spinner questionType = findViewById(R.id.questionType);
        Spinner skinDiseaseType = findViewById(R.id.skinDiseaseType);
        EditText option1 = findViewById(R.id.option1);
        EditText option2 = findViewById(R.id.option2);
        EditText option3 = findViewById(R.id.option3);

        Button delete = findViewById(R.id.delete);
        Button update = findViewById(R.id.update);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("questions").child(AdapterQuestion.clickedQuestion.id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(QuestionDetailsActivity.this, "Question Deleted Successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
            }
        });
        question.setText(AdapterQuestion.clickedQuestion.question);
        selectValue(skinDiseaseType,AdapterQuestion.clickedQuestion.skinDiseaseType);
        selectValue(questionType,AdapterQuestion.clickedQuestion.questionType);
        questionType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (questionType.getSelectedItem().toString().equals("Multiple Choice")) {
                    option1.setVisibility(View.VISIBLE);
                    option2.setVisibility(View.VISIBLE);
                    option3.setVisibility(View.VISIBLE);
                    option1.setText(AdapterQuestion.clickedQuestion.option1);
                    option2.setText(AdapterQuestion.clickedQuestion.option2);
                    option3.setText(AdapterQuestion.clickedQuestion.option3);

                } else {
                    option1.setVisibility(View.GONE);
                    option2.setVisibility(View.GONE);
                    option3.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (skinDiseaseType.getSelectedItem().equals("Select Skin Diseases Type")) {
                    Toast.makeText(QuestionDetailsActivity.this, "Select Skin Diseases Type", Toast.LENGTH_SHORT).show();
                } else if (questionType.getSelectedItem().equals("Select Question Types")) {
                    Toast.makeText(QuestionDetailsActivity.this, "Select Question Types", Toast.LENGTH_SHORT).show();
                } else if (question.getText().toString().isEmpty()) {
                    question.setError("Enter question");
                } else {
                    if (questionType.getSelectedItem().equals("Multiple Choice")) {
                        if (option1.getText().toString().isEmpty()) {
                            option1.setError("Enter option 1");
                        } else if (option2.getText().toString().isEmpty()) {
                            option2.setError("Enter option 2");
                        } else if (option3.getText().toString().isEmpty()) {
                            option3.setError("Enter option 3");
                        }  else {
                            AdapterQuestion.clickedQuestion.question = question.getText().toString();
                            AdapterQuestion.clickedQuestion.skinDiseaseType = skinDiseaseType.getSelectedItem().toString();
                            AdapterQuestion.clickedQuestion.option1 = option1.getText().toString();
                            AdapterQuestion.clickedQuestion.option2 = option2.getText().toString();
                            AdapterQuestion.clickedQuestion.option3 = option3.getText().toString();
                             FirebaseDatabase.getInstance().getReference().child("questions").child(AdapterQuestion.clickedQuestion.id).setValue(AdapterQuestion.clickedQuestion).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(QuestionDetailsActivity.this, "Question Updated Successfully", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            });
                        }
                    } else {
                        AdapterQuestion.clickedQuestion.question = question.getText().toString();
                        AdapterQuestion.clickedQuestion.skinDiseaseType = skinDiseaseType.getSelectedItem().toString();
                        AdapterQuestion.clickedQuestion.questionType = questionType.getSelectedItem().toString();
                        FirebaseDatabase.getInstance().getReference().child("questions").child(AdapterQuestion.clickedQuestion.id).setValue(AdapterQuestion.clickedQuestion).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(QuestionDetailsActivity.this, "Question Updated Successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        });
                    }
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
}