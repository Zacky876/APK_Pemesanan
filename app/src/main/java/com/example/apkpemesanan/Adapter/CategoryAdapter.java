package com.example.apkpemesanan.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.apkpemesanan.Domain.CategoryDomain;
import com.example.apkpemesanan.R;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    ArrayList<CategoryDomain> categoryDomains;

    public CategoryAdapter(ArrayList<CategoryDomain> categoryDomains) {
        this.categoryDomains = categoryDomains;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_category,parent,false);

        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        holder.categoryName.setText(categoryDomains.get(position).getTitle());
        String picUrl = "";

        switch (position) {
            case 0: {
                picUrl="cat_1";
                break;
            }
            case 1: {
                picUrl="cat_2";
                break;
            }
//            case 2: {
//                picUrl="cat_3";
//                break;
//            }
//            case 3: {
//                picUrl="cat_4";
//                break;
//            }
//            case 4: {
//                picUrl="cat_5";
//                break;
//            }
        }

        @SuppressLint("DiscouragedApi") int drawableReourceId=holder.itemView.getContext().getResources().getIdentifier(picUrl,"drawable",holder.itemView.getContext().getPackageName());

        Glide.with(holder.itemView.getContext()).load(drawableReourceId).into(holder.categorypic);
    }

    @Override
    public int getItemCount() {
        return categoryDomains.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        ImageView categorypic;

        public ViewHolder(@NonNull View itemview) {
            super(itemview);

            categoryName = itemview.findViewById(R.id.categoryName);
            categorypic = itemview.findViewById(R.id.categoryPic);
        }
    }
}
