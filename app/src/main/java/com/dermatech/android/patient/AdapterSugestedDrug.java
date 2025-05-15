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
import com.dermatech.android.admin.drugs.DrugDetailsActivity;
import com.dermatech.android.model.Drug;

import java.util.ArrayList;

public class AdapterSugestedDrug extends RecyclerView.Adapter<AdapterSugestedDrug.MyViewHolder> {

    Context context;
    ArrayList<Drug> drugs;

    static Drug clickedDrug;

    public AdapterSugestedDrug(Context context, ArrayList<Drug> drugs) {
        this.context = context;
        this.drugs = drugs;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sugested_drug,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.genericName.setText(drugs.get(position).genericName);
        holder.tradeName.setText(drugs.get(position).tradeName);
        holder.image.setImageBitmap(ImageUtils.convert(drugs.get(position).image));
    }

    @Override
    public int getItemCount() {
        return drugs.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView genericName,tradeName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            genericName = itemView.findViewById(R.id.genericName);
            tradeName = itemView.findViewById(R.id.tradeName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), DrugDetailsForPatientActivity.class);
                    clickedDrug = drugs.get(getAdapterPosition());
                    context.startActivity(intent);
                }
            });

        }
    }
}
