package com.dermatech.android.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dermatech.android.R;
import com.dermatech.android.model.Message;
import com.google.firebase.auth.FirebaseAuth;


import java.util.ArrayList;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    Context context;
    ArrayList<Message> messages;

    public MessageAdapter(Context context, ArrayList<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_message,parent,false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.get(position).senderId))
        {
                holder.s_message.setText(messages.get(position).message);
                holder.s_message.setVisibility(View.VISIBLE);
                holder.r_message.setVisibility(View.GONE);
        }   else {
                holder.r_message.setText(messages.get(position).message);
                holder.r_message.setVisibility(View.VISIBLE);
                holder.s_message.setVisibility(View.GONE);
            }

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder{

        TextView s_message,r_message;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            s_message = itemView.findViewById(R.id.s_message);
            r_message = itemView.findViewById(R.id.r_message);


        }
    }
}
