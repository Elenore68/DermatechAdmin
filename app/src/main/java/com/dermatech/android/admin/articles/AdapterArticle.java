package com.dermatech.android.admin.articles;

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
import com.dermatech.android.User;
import com.dermatech.android.admin.MedicalConsultantAcceptOrRejectActivity;
import com.dermatech.android.model.Article;

import java.util.ArrayList;

public class AdapterArticle extends RecyclerView.Adapter<AdapterArticle.MedicalConsultantViewHolder> {

    Context context;
    ArrayList<Article> articles;

    static Article clickedArticle;

    public AdapterArticle(Context context, ArrayList<Article> articles) {
        this.context = context;
        this.articles = articles;
    }

    @NonNull
    @Override
    public MedicalConsultantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_article,parent,false);

        return new MedicalConsultantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicalConsultantViewHolder holder, int position) {
        holder.title.setText(articles.get(position).title);
        holder.brief.setText(articles.get(position).brief);
        holder.image.setImageBitmap(ImageUtils.convert(articles.get(position).image));
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    class MedicalConsultantViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView title,brief;
        public MedicalConsultantViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            brief = itemView.findViewById(R.id.brief);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ArticleDetailsActivity.class);
                    clickedArticle = articles.get(getAdapterPosition());
                    context.startActivity(intent);
                }
            });

        }
    }
}
