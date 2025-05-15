package com.dermatech.android.admin.questions;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dermatech.android.ImageUtils;
import com.dermatech.android.R;
import com.dermatech.android.admin.drugs.DrugDetailsActivity;
import com.dermatech.android.model.Drug;
import com.dermatech.android.model.Question;

import java.util.ArrayList;

public class AdapterQuestion extends RecyclerView.Adapter<AdapterQuestion.MyViewHolder> {

    Context context;
    ArrayList<Question> questions;

    static Question clickedQuestion;

    public AdapterQuestion(Context context, ArrayList<Question> questions) {
        this.context = context;
        this.questions = questions;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_question,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.question.setText(questions.get(position).question);
        holder.questionType.setText(questions.get(position).questionType);
        holder.type.setText(questions.get(position).skinDiseaseType);

    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView type,question,questionType;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.question);
            type = itemView.findViewById(R.id.type);
            questionType = itemView.findViewById(R.id.questionType);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), QuestionDetailsActivity.class);
                    clickedQuestion = questions.get(getAdapterPosition());
                    context.startActivity(intent);
                }
            });

        }
    }
}
