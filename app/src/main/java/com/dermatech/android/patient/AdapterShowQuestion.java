package com.dermatech.android.patient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dermatech.android.R;
import com.dermatech.android.model.Question;

import java.util.ArrayList;

public class AdapterShowQuestion extends RecyclerView.Adapter<AdapterShowQuestion.MyViewHolder> {

    Context context;
    ArrayList<Question> questions;

    static Question clickedQuestion;

    public AdapterShowQuestion(Context context, ArrayList<Question> questions) {
        this.context = context;
        this.questions = questions;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_question_show,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.question.setText(questions.get(position).question);
        holder.answer.setText(questions.get(position).answer);

    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView question,answer;
        RadioButton option1,option2,option3;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.question);
            answer = itemView.findViewById(R.id.answer);




        }
    }
}
