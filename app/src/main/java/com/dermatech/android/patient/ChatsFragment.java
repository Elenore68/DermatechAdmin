package com.dermatech.android.patient;

import static android.content.Context.MODE_PRIVATE;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dermatech.android.R;
import com.dermatech.android.chat.AdapterChat;
import com.dermatech.android.model.Chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ChatsFragment extends Fragment {

    ArrayList<Chat> chats = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chats, container, false);
        String userType = getActivity().getSharedPreferences("com.dermatech.android", MODE_PRIVATE).getString("userType", "");


        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        AdapterChat adapterChat = new AdapterChat(getActivity(),chats,userType);
        recyclerView.setAdapter(adapterChat);

        if(userType.equals("Patient")) {
            FirebaseDatabase.getInstance().getReference().child("chats").orderByChild("patientID").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    chats.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        chats.add(dataSnapshot.getValue(Chat.class));
                    }
                    adapterChat.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else if(userType.equals("MedicalConsultant")){
            FirebaseDatabase.getInstance().getReference().child("chats").orderByChild("medicalConsultantID").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    chats.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        chats.add(dataSnapshot.getValue(Chat.class));
                    }
                    adapterChat.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        return view;
    }


}