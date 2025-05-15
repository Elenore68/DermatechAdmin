package com.dermatech.android.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dermatech.android.R;
import com.dermatech.android.medical_consultant.PatientHistoryActivity;
import com.dermatech.android.model.Chat;
import com.dermatech.android.model.Message;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatingActivity extends AppCompatActivity {
    ArrayList<Message> messages = new ArrayList<>();

    String chatId;
    MessageAdapter adapter;
    String userType;
    String patientId;

    Chat currentChat;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chating);
        getSupportActionBar().hide();


        TextView showHistory = findViewById(R.id.showHistory);


         userType = getSharedPreferences("com.dermatech.android", MODE_PRIVATE).getString("userType", "");

        if(userType.equals("MedicalConsultant") ){
            showHistory.setVisibility(View.VISIBLE);
        }
        if(getIntent().hasExtra("startChat")){
            String id = FirebaseDatabase.getInstance().getReference().child("chats").push().getKey();
            String patientID = getIntent().getStringExtra("patientId");
            String medicalConsultantId = getIntent().getStringExtra("medicalConsultantId");
            Chat chat = new Chat(id,patientID,medicalConsultantId,0,0);
            FirebaseDatabase.getInstance().getReference().child("chats").child(chat.id).setValue(chat).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    chatId = id;
                    currentChat = chat;
                    loadData();

                }
            });
        }else{
            Intent intent = getIntent();
            chatId = intent.getStringExtra("chatId");
            patientId = intent.getStringExtra("patientId");
            Log.e("patientId ----->",patientId);
            FirebaseDatabase.getInstance().getReference().child("chats").child(chatId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        currentChat = task.getResult().getValue(Chat.class);
                        loadData();
                    }
                }
            });

        }
        showHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PatientHistoryActivity.class);
                intent.putExtra("patientId",patientId);
                startActivity(intent);
            }
        });
        recyclerView = findViewById(R.id.recyclerView);
        EditText messageBox = findViewById(R.id.messageBox);
        ImageButton send = findViewById(R.id.send);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MessageAdapter(this,messages);
        recyclerView.setAdapter(adapter);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!messageBox.getText().toString().isEmpty()){
                    String idMessage = FirebaseDatabase.getInstance().getReference().child("messages").child(chatId).push().getKey();
                    Message message = new Message(idMessage,messageBox.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid());
                    FirebaseDatabase.getInstance().getReference().child("messages").child(chatId).child(idMessage).setValue(message)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        messageBox.setText("");
                                        if(userType.equals("Patient") ){
                                            currentChat.medicalConsultantCountUnRaed = currentChat.medicalConsultantCountUnRaed + 1;
                                            currentChat.patientCountUnRaed = 0;
                                            FirebaseDatabase.getInstance().getReference().child("chats").child(chatId).setValue(currentChat);
                                        }else if(userType.equals("MedicalConsultant") ){
                                            currentChat.medicalConsultantCountUnRaed = 0;
                                            currentChat.patientCountUnRaed = currentChat.patientCountUnRaed + 1;
                                            FirebaseDatabase.getInstance().getReference().child("chats").child(chatId).setValue(currentChat);
                                        }
                                    }
                                }
                            });

                }
            }
        });
    }

    void loadData(){
        FirebaseDatabase.getInstance().getReference().child("messages").child(chatId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Message message = dataSnapshot.getValue(Message.class);
                    messages.add(message);
                }
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(messages.size() - 1);

                if(userType.equals("Patient") ){

                    currentChat.patientCountUnRaed = 0;
                    FirebaseDatabase.getInstance().getReference().child("chats").child(chatId).setValue(currentChat);
                }else if(userType.equals("MedicalConsultant") ){
                    currentChat.medicalConsultantCountUnRaed = 0;

                    FirebaseDatabase.getInstance().getReference().child("chats").child(chatId).setValue(currentChat);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}