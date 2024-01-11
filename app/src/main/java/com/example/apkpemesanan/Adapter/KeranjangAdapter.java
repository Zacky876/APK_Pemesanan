package com.example.apkpemesanan.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apkpemesanan.Domain.FoodDomain;
import com.example.apkpemesanan.Helper.ManagementCart;
import com.example.apkpemesanan.Interface.ChangeNumberItems;
import com.example.apkpemesanan.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class KeranjangAdapter extends RecyclerView.Adapter<KeranjangAdapter.ViewHolder> {
    public ArrayList<FoodDomain> listFood;
    private ManagementCart managementCart;
    ChangeNumberItems changeNumberItems;

    public KeranjangAdapter(ArrayList<FoodDomain> listFood, Context context, ChangeNumberItems changeNumberItems) {
        this.listFood = listFood;
        managementCart = new ManagementCart(context);
        this.changeNumberItems = changeNumberItems;
    }

    public ArrayList<FoodDomain> getFoodList() {
        return this.listFood;
    }

    @NonNull
    @Override
    public KeranjangAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cart,parent,false);

        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(listFood.get(position).getTitle());
        holder.feeEachItem.setText("Rp." + listFood.get(position).getFee());
        holder.totalEachItem.setText("Rp." + Math.round((listFood.get(position).getNumberInCart() * listFood.get(position).getFee())) );
        holder.totalEachItem.setText("Rp." + Math.round((listFood.get(position).getNumberInCart() * listFood.get(position).getFee())) );
        holder.num.setText(String.valueOf(listFood.get(position).getNumberInCart()));


//        int drawableReourceId=holder.itemView.getContext().getResources().getIdentifier(listFood.get(position).getPic(),"drawable",holder.itemView.getContext().getPackageName());
//
//        Glide.with(holder.itemView.getContext()).load(drawableReourceId).into(holder.pic);
        Picasso.get().load(listFood.get(position).getPic()).into(holder.pic);

        holder.plusItem.setOnClickListener(view -> managementCart.plusNumberFood(listFood, position, () -> {
            notifyDataSetChanged();
            changeNumberItems.changed();
        }));

        holder.minusItem.setOnClickListener(view -> managementCart.minusNumberFood(listFood, position, () -> {
            notifyDataSetChanged();
            changeNumberItems.changed();
        }));
    }

    @Override
    public int getItemCount() {
        return listFood.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, feeEachItem;
        ImageView pic,plusItem,minusItem;
        TextView totalEachItem, num;

        public ViewHolder(@NonNull View itemview) {
            super(itemview);

            title = itemview.findViewById(R.id.title);
            pic = itemview.findViewById(R.id.pic);
            feeEachItem = itemview.findViewById(R.id.feeEachItem);
            totalEachItem = itemview.findViewById(R.id.totalEachItem);
            plusItem = itemview.findViewById(R.id.PlusCartbtn);
            minusItem = itemview.findViewById(R.id.minusCartbtn);
            num = itemview.findViewById(R.id.numberItemCart);
        }
    }
}
