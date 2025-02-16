package com.example.apkpemesanan.Adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.apkpemesanan.Domain.FoodDomain;
import com.example.apkpemesanan.MainActivity;
import com.example.apkpemesanan.R;
import com.example.apkpemesanan.ShowDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.ViewHolder> implements Filterable {
    ArrayList<FoodDomain> RecommendDomains;
    ArrayList<FoodDomain> RecommendDomainsFull;
    MainActivity activity;

    public RecommendAdapter(ArrayList<FoodDomain> categoryDomains, MainActivity activity) {
        this.RecommendDomains = categoryDomains;
        this.RecommendDomainsFull = new ArrayList<>(categoryDomains);
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


        Picasso.get().load("http://10.0.2.2:8000/gambar?id_gambar=" + RecommendDomains.get(position).getPic()).into(holder.pic);

        holder.addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(activity, ShowDetailActivity.class);
                intent.putExtra("fooddomain", RecommendDomains.get(position));
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
    public void updateData(ArrayList<FoodDomain> newData) {
        this.RecommendDomains.clear();
        this.RecommendDomains.addAll(newData);
        this.RecommendDomainsFull.clear();
        this.RecommendDomainsFull.addAll(newData);
        notifyDataSetChanged();
    }

    public List<FoodDomain> getFilteredItems(String query) {
        List<FoodDomain> filteredList = new ArrayList<>();
        for (FoodDomain item : RecommendDomainsFull) {
            if (item.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(item);
            }
        }
        return filteredList;
    }

    public void updateList(List<FoodDomain> newList) {
        Log.d("SearchDebug", "Updating List with: " + newList.size() + " items");
        RecommendDomains.clear();
        RecommendDomains.addAll(newList);
        notifyDataSetChanged();
    }
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<FoodDomain> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(RecommendDomainsFull);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (FoodDomain item : RecommendDomainsFull) {
                        if (item.getTitle().toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                RecommendDomains.clear();
                if (results.values != null && results.values instanceof List) {
                    RecommendDomains.addAll((List<FoodDomain>) results.values);
                }
                notifyDataSetChanged();
            }
        };
    }
}
