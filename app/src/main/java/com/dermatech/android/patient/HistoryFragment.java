package com.dermatech.android.patient;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dermatech.android.R;
import com.dermatech.android.User;
import com.dermatech.android.admin.AdapterMedicalConsultant;
import com.dermatech.android.model.Diagnosis;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class HistoryFragment extends Fragment {



    ArrayList<Diagnosis> diagnoses = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        AdapterHistory adapterHistory = new AdapterHistory(getActivity(),diagnoses);
        recyclerView.setAdapter(adapterHistory);

        FirebaseDatabase.getInstance().getReference().child("diagnosis").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                diagnoses.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Diagnosis diagnosisObj = dataSnapshot.getValue(Diagnosis.class);
                    diagnoses.add(diagnosisObj);

                }
                adapterHistory.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}