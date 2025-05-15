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
import com.dermatech.android.chat.ChatingActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class AdapterSugestedMedicalConsultant extends RecyclerView.Adapter<AdapterSugestedMedicalConsultant.MedicalConsultantViewHolder> {

    Context context;
    ArrayList<User> medicalConsultants;

    static User clickedMedicalConsultant;

    public AdapterSugestedMedicalConsultant(Context context, ArrayList<User> medicalConsultants) {
        this.context = context;
        this.medicalConsultants = medicalConsultants;
    }

    @NonNull
    @Override
    public MedicalConsultantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sugested_medical_consultant,parent,false);

        return new MedicalConsultantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicalConsultantViewHolder holder, int position) {
        holder.name.setText(medicalConsultants.get(position).username);
        holder.profileImage.setImageBitmap(ImageUtils.convert(medicalConsultants.get(position).consultantImage));
        holder.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ChatingActivity.class);
                intent.putExtra("startChat",true);
                intent.putExtra("patientId", FirebaseAuth.getInstance().getCurrentUser().getUid());
                intent.putExtra("medicalConsultantId",medicalConsultants.get(position).id);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return medicalConsultants.size();
    }

    class MedicalConsultantViewHolder extends RecyclerView.ViewHolder{
        ImageView profileImage,chat;
        TextView name,national_id;
        public MedicalConsultantViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profileImage);
            chat = itemView.findViewById(R.id.chat);
            name = itemView.findViewById(R.id.name);
            national_id = itemView.findViewById(R.id.national_id);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), MedicalConsultantDetailsActivity.class);
                    clickedMedicalConsultant = medicalConsultants.get(getAdapterPosition());
                    context.startActivity(intent);
                }
            });

        }
    }
}
