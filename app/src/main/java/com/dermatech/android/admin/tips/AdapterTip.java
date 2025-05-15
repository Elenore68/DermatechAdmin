package com.dermatech.android.admin.tips;

import static android.content.Context.MODE_PRIVATE;

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
import com.dermatech.android.admin.articles.ArticleDetailsActivity;
import com.dermatech.android.model.Article;
import com.dermatech.android.model.Tip;

import java.util.ArrayList;

public class AdapterTip extends RecyclerView.Adapter<AdapterTip.MedicalConsultantViewHolder> {

    Context context;
    ArrayList<Tip> tips;

    static Tip clickedTip;

    String userType;
    public AdapterTip(Context context, ArrayList<Tip> tips) {
        this.context = context;
        this.tips = tips;
         userType = context.getSharedPreferences("com.dermatech.android", MODE_PRIVATE).getString("userType", "");

    }

    @NonNull
    @Override
    public MedicalConsultantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tip,parent,false);

        return new MedicalConsultantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicalConsultantViewHolder holder, int position) {
        holder.tip.setText(tips.get(position).tip);
        holder.type.setText(tips.get(position).type);
        holder.image.setImageBitmap(ImageUtils.convert(tips.get(position).image));
    }

    @Override
    public int getItemCount() {
        return tips.size();
    }

    class MedicalConsultantViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView tip,type;
        public MedicalConsultantViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            tip = itemView.findViewById(R.id.tip);
            type = itemView.findViewById(R.id.type);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(userType.equals("Admin")) {
                        Intent intent = new Intent(v.getContext(), TipDetailsActivity.class);
                        clickedTip = tips.get(getAdapterPosition());
                        context.startActivity(intent);
                    }
                }
            });

        }
    }
}
