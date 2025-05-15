package com.dermatech.android.chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.dermatech.android.model.Chat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.CustomViewHolder> {

    Context context;
    ArrayList<Chat> chats;
    String userType;


    public AdapterChat(Context context, ArrayList<Chat> chats, String userType) {
        this.context = context;
        this.chats = chats;
        this.userType = userType;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chat,parent,false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        if(userType.equals("MedicalConsultant")) {
            if(chats.get(position).medicalConsultantCountUnRaed > 0){
                holder.countUnRaed.setText(chats.get(position).medicalConsultantCountUnRaed+"");
                holder.countUnRaed.setVisibility(View.VISIBLE);
            }else {
                holder.countUnRaed.setVisibility(View.GONE);
            }
            FirebaseDatabase.getInstance().getReference().child("users").child(chats.get(position).patientID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    User user = task.getResult().getValue(User.class);
                    holder.name.setText(user.username);
                    if(user.image != null) {
                        Bitmap bitmap = ImageUtils.convert(user.image);
                        holder.profileImage.setImageBitmap(bitmap);
                    }
                }
            });
        } else if(userType.equals("Patient") ) {
            if(chats.get(position).patientCountUnRaed > 0){
                holder.countUnRaed.setText(chats.get(position).patientCountUnRaed+"");
                holder.countUnRaed.setVisibility(View.VISIBLE);
            }else {
                holder.countUnRaed.setVisibility(View.GONE);
            }
            FirebaseDatabase.getInstance().getReference().child("users").child(chats.get(position).medicalConsultantID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    User user = task.getResult().getValue(User.class);
                    holder.name.setText(user.username);

                        Bitmap bitmap = ImageUtils.convert(user.consultantImage);
                        holder.profileImage.setImageBitmap(bitmap);

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder{
        ImageView profileImage;
        TextView name,countUnRaed;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profileImage);
            name = itemView.findViewById(R.id.name);
            countUnRaed = itemView.findViewById(R.id.countUnRaed);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ChatingActivity.class);
                    intent.putExtra("chatId",chats.get(getAdapterPosition()).id);
                    intent.putExtra("patientId",chats.get(getAdapterPosition()).patientID);
                    context.startActivity(intent);
                }
            });
        }
    }
}
