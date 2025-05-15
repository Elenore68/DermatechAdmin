package com.dermatech.android.patient;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dermatech.android.R;
import com.dermatech.android.admin.questions.QuestionDetailsActivity;
import com.dermatech.android.model.Question;

import java.util.ArrayList;

public class AdapterQuestionForPatient extends RecyclerView.Adapter<AdapterQuestionForPatient.MyViewHolder> {

    Context context;
    ArrayList<Question> questions;

    static Question clickedQuestion;

    public AdapterQuestionForPatient(Context context, ArrayList<Question> questions) {
        this.context = context;
        this.questions = questions;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_question_yes_no,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.question.setText(questions.get(position).question);
        if(questions.get(position).questionType.equals("Yes/No")){
            holder.option1.setText("Yes");
            holder.option3.setText("No");
            holder.option2.setVisibility(View.GONE);
        }else  if(questions.get(position).questionType.equals("Multiple Choice")){
            holder.option1.setText("Child (<12)");
            holder.option2.setText("Teenager (12–18)");
            holder.option3.setText("Adult (>18)");
            holder.option3.setVisibility(View.VISIBLE);
        }

        holder.option1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    questions.get(position).answer = holder.option1.getText().toString();
                }
            }
        });
        holder.option2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    questions.get(position).answer = holder.option2.getText().toString();
                }
            }
        });
        holder.option3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    questions.get(position).answer = holder.option3.getText().toString();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView question;
        RadioButton option1,option2,option3;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.question);
            option1 = itemView.findViewById(R.id.option1);
            option2 = itemView.findViewById(R.id.option2);
            option3 = itemView.findViewById(R.id.option3);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(v.getContext(), QuestionDetailsActivity.class);
//                    clickedQuestion = questions.get(getAdapterPosition());
//                    context.startActivity(intent);
//                }
//            });

        }
    }
}
