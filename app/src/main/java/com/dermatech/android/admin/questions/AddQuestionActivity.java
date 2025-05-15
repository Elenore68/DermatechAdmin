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
import com.dermatech.android.admin.drugs.AddDrugActivity;
import com.dermatech.android.model.Question;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

public class AddQuestionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);
        getSupportActionBar().hide();

        EditText question = findViewById(R.id.question);
        Spinner questionType = findViewById(R.id.questionType);
        Spinner skinDiseaseType = findViewById(R.id.skinDiseaseType);
        EditText option1 = findViewById(R.id.option1);
        EditText option2 = findViewById(R.id.option2);
        EditText option3 = findViewById(R.id.option3);

        Button add = findViewById(R.id.add);

        questionType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(questionType.getSelectedItem().toString().equals("Multiple Choice")){
                    option1.setVisibility(View.VISIBLE);
                    option2.setVisibility(View.VISIBLE);
                    option3.setVisibility(View.VISIBLE);
                }else{
                    option1.setVisibility(View.GONE);
                    option2.setVisibility(View.GONE);
                    option3.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (skinDiseaseType.getSelectedItem().equals("Select Skin Diseases Type")) {
                    Toast.makeText(AddQuestionActivity.this, "Select Skin Diseases Type", Toast.LENGTH_SHORT).show();
                }else if (questionType.getSelectedItem().equals("Select Question Types")) {
                    Toast.makeText(AddQuestionActivity.this, "Select Question Types", Toast.LENGTH_SHORT).show();
                } else if (question.getText().toString().isEmpty()) {
                    question.setError("Enter question");
                } else{
                    if(questionType.getSelectedItem().equals("Multiple Choice")) {
                        if (option1.getText().toString().isEmpty()) {
                            option1.setError("Enter option 1");
                        } else if (option2.getText().toString().isEmpty()) {
                            option2.setError("Enter option 2");
                        }else if (option3.getText().toString().isEmpty()) {
                            option3.setError("Enter option 3");
                        }else {
                            String id = FirebaseDatabase.getInstance().getReference().child("questions").push().getKey();
                            Question questionObject = new Question(id,question.getText().toString(),questionType.getSelectedItem().toString(),skinDiseaseType.getSelectedItem().toString(),option1.getText().toString(),option2.getText().toString(),option3.getText().toString());
                            FirebaseDatabase.getInstance().getReference().child("questions").child(questionObject.id).setValue(questionObject).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(AddQuestionActivity.this, "Question Added Successfully", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            });
                        }
                    }else{
                        String id = FirebaseDatabase.getInstance().getReference().child("questions").push().getKey();
                        Question questionObject = new Question(id,question.getText().toString(),questionType.getSelectedItem().toString(),skinDiseaseType.getSelectedItem().toString());
                        FirebaseDatabase.getInstance().getReference().child("questions").child(questionObject.id).setValue(questionObject).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(AddQuestionActivity.this, "Question Added Successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        });
                    }
                }
            }
        });

    }
}