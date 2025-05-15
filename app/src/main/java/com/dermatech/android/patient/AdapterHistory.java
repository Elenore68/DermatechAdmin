package com.dermatech.android.patient;

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
import com.dermatech.android.User;
import com.dermatech.android.admin.MedicalConsultantAcceptOrRejectActivity;
import com.dermatech.android.model.Diagnosis;

import java.util.ArrayList;

public class AdapterHistory extends RecyclerView.Adapter<AdapterHistory.HistoryViewHolder> {

    Context context;
    ArrayList<Diagnosis> diagnoses;

    static Diagnosis clickedDiagnosis;

    public AdapterHistory(Context context, ArrayList<Diagnosis> diagnoses) {
        this.context = context;
        this.diagnoses = diagnoses;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hisorty,parent,false);

        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        holder.name.setText(diagnoses.get(position).skinDiseaseType);
        holder.dateTime.setText(diagnoses.get(position).dateTime);
        holder.img.setImageBitmap(ImageUtils.convert(diagnoses.get(position).skinDiseaseImage));
    }

    @Override
    public int getItemCount() {
        return diagnoses.size();
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView name,dateTime;
        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            name = itemView.findViewById(R.id.name);
            dateTime = itemView.findViewById(R.id.dateTime);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), DiagnosisDetailsActivity.class);
                    clickedDiagnosis = diagnoses.get(getAdapterPosition());
                    context.startActivity(intent);
                }
            });

        }
    }
}
