package com.dermatech.android.admin.tips;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dermatech.android.R;
import com.dermatech.android.admin.articles.AdapterArticle;
import com.dermatech.android.admin.articles.AddArticlesActivity;
import com.dermatech.android.model.Article;
import com.dermatech.android.model.Tip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class  ManageTipsFragment extends Fragment {

    ArrayList<Tip> tips = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manage_tips, container, false);
        FloatingActionButton floatingActionButton = view.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), AddTipActivity.class));
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        AdapterTip adapterTip = new AdapterTip(getActivity(),tips);
        recyclerView.setAdapter(adapterTip);

        FirebaseDatabase.getInstance().getReference().child("tips").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tips.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Tip tip  = dataSnapshot.getValue(Tip.class);
                    tips.add(tip);
                }
                adapterTip.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
}