package com.dermatech.android.admin.drugs;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dermatech.android.R;
import com.dermatech.android.admin.articles.AdapterArticle;
import com.dermatech.android.admin.articles.AddArticlesActivity;
import com.dermatech.android.model.Article;
import com.dermatech.android.model.Drug;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ManageDrugsFragment extends Fragment {
    ArrayList<Drug> drugs = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manage_drugs, container, false);

        FloatingActionButton floatingActionButton = view.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), AddDrugActivity.class));
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        AdapterDrug adapterDrug = new AdapterDrug(getActivity(),drugs);
        recyclerView.setAdapter(adapterDrug);

        FirebaseDatabase.getInstance().getReference().child("drugs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                drugs.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Drug drug = dataSnapshot.getValue(Drug.class);
                    drugs.add(drug);
                }
                adapterDrug.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }
}