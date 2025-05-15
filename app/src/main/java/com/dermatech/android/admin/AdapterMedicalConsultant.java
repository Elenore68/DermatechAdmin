package com.dermatech.android.admin;

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

import java.util.ArrayList;

public class AdapterMedicalConsultant extends RecyclerView.Adapter<AdapterMedicalConsultant.MedicalConsultantViewHolder> {

    Context context;
    ArrayList<User> medicalConsultants;

    static User clickedMedicalConsultant;

    public AdapterMedicalConsultant(Context context, ArrayList<User> medicalConsultants) {
        this.context = context;
        this.medicalConsultants = medicalConsultants;
    }

    @NonNull
    @Override
    public MedicalConsultantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_medical_consultant,parent,false);

        return new MedicalConsultantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicalConsultantViewHolder holder, int position) {
        holder.name.setText(medicalConsultants.get(position).username);
        holder.national_id.setText(medicalConsultants.get(position).national_id);
        holder.profileImage.setImageBitmap(ImageUtils.convert(medicalConsultants.get(position).consultantImage));
    }

    @Override
    public int getItemCount() {
        return medicalConsultants.size();
    }

    class MedicalConsultantViewHolder extends RecyclerView.ViewHolder{
        ImageView profileImage;
        TextView name,national_id;
        public MedicalConsultantViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profileImage);
            name = itemView.findViewById(R.id.name);
            national_id = itemView.findViewById(R.id.national_id);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), MedicalConsultantAcceptOrRejectActivity.class);
                    clickedMedicalConsultant = medicalConsultants.get(getAdapterPosition());
                    context.startActivity(intent);
                }
            });

        }
    }
}
