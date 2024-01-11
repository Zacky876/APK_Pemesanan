package com.example.apkpemesanan.Adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.apkpemesanan.Domain.FoodDomain;
import com.example.apkpemesanan.MainActivity;
import com.example.apkpemesanan.R;
import com.example.apkpemesanan.ShowDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.ViewHolder> {
    ArrayList<FoodDomain> RecommendDomains;
    MainActivity activity;

    public RecommendAdapter(ArrayList<FoodDomain> categoryDomains, MainActivity activity) {
        this.RecommendDomains = categoryDomains;
        this.activity = activity;
    }

    @NonNull
    @Override
    public RecommendAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_recommend, parent, false);

        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.title.setText(RecommendDomains.get(position).getTitle());
        holder.fee.setText(String.valueOf(RecommendDomains.get(position).getFee()));


        Picasso.get().load(RecommendDomains.get(position).getPic()).into(holder.pic);
//
//        int drawableReourceId = holder.itemView.getContext().getResources()
//                .getIdentifier(RecommendDomains.get(position).getPic(), "drawable",
//                        holder.itemView.getContext().getPackageName());

//        Glide.with(holder.itemView.getContext()).load(drawableReourceId).into(holder.pic);

        holder.addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(activity, ShowDetailActivity.class);
//            Log.d("TES", RecommendDomains.get(position).toString());
                intent.putExtra("fooddomain", RecommendDomains.get(position));
//                activity.getApplication().startActivity(intent);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return RecommendDomains.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, fee;
        ImageView pic;
        ImageView addbtn;

        public ViewHolder(@NonNull View itemview) {
            super(itemview);

            title = itemview.findViewById(R.id.tittle);
            pic = itemview.findViewById(R.id.pic);
            fee = itemview.findViewById(R.id.fee);
            addbtn = itemview.findViewById(R.id.addbtn);
        }
    }
}
