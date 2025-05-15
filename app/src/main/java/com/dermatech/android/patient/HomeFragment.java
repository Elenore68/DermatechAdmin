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
import android.widget.Button;
import android.widget.TextView;

import com.dermatech.android.R;
import com.dermatech.android.admin.articles.AdapterArticle;
import com.dermatech.android.admin.tips.AdapterTip;
import com.dermatech.android.model.Article;
import com.dermatech.android.model.Tip;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
    ArrayList<Article> articles = new ArrayList<>();
    ArrayList<Tip> tips = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        TextView name = view.findViewById(R.id.name);
        Button analyze = view.findViewById(R.id.analyze);
        String username = getActivity().getSharedPreferences("com.dermatech.android", MODE_PRIVATE).getString("username", "Mohammed");
        name.setText(username);
        analyze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomePatientActivity activity = (HomePatientActivity) getActivity();
                if (activity != null) {
                    BottomNavigationView bottomNavigationView = activity.findViewById(R.id.bottomNavigationView);
                    bottomNavigationView.setSelectedItemId(R.id.scan);
                }
            }
        });

        RecyclerView recyclerViewMedicalArticles = view.findViewById(R.id.recyclerViewMedicalArticles);
        recyclerViewMedicalArticles.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        AdapterForPatientArticle adapterArticle = new AdapterForPatientArticle(getActivity(),articles);
        recyclerViewMedicalArticles.setAdapter(adapterArticle);

        FirebaseDatabase.getInstance().getReference().child("articles").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                articles.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Article article = dataSnapshot.getValue(Article.class);
                    articles.add(article);
                }
                adapterArticle.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        RecyclerView recyclerViewCareTips = view.findViewById(R.id.recyclerViewCareTips);
        recyclerViewCareTips.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        AdapterTip adapterTip = new AdapterTip(getActivity(),tips);
        recyclerViewCareTips.setAdapter(adapterTip);

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