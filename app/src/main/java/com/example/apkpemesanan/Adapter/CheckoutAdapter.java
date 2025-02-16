package com.example.apkpemesanan.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apkpemesanan.Domain.FoodDomain;
import com.example.apkpemesanan.Helper.ManagementCart;
import com.example.apkpemesanan.Interface.ChangeNumberItems;
import com.example.apkpemesanan.R;

import java.util.ArrayList;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.ViewHolder> {
    private ManagementCart managementCart;
    public ArrayList<FoodDomain> listFood;
    ChangeNumberItems changeNumberItems;

    public CheckoutAdapter(ArrayList<FoodDomain> listFood, Context context, ChangeNumberItems changeNumberItems) {
        this.listFood = listFood;
        managementCart = new ManagementCart(context);
        this.changeNumberItems = changeNumberItems;
    }

    public ArrayList<FoodDomain> getFoodList() {
        return this.listFood;
    }

    @NonNull
    @Override
    public CheckoutAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_checkout, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckoutAdapter.ViewHolder holder, int position) {
        FoodDomain food = listFood.get(position);

        holder.title2.setText(food.getTitle());
        holder.feeInvoice.setText("Rp. " + food.getFee());
        holder.stokInvoice.setText(String.valueOf(food.getNumberInCart()));

        int total = (int) (food.getNumberInCart() * food.getFee());
        holder.total_Invoice.setText("Rp. " + total);
    }


    @Override
    public int getItemCount() {
        return listFood.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title2, feeInvoice, stokInvoice, total_Invoice;

        public ViewHolder(@NonNull View itemview) {
            super(itemview);
            title2 = itemview.findViewById(R.id.title2);
            feeInvoice = itemview.findViewById(R.id.feeInvoice);
            stokInvoice = itemview.findViewById(R.id.stokInvoice);
            total_Invoice = itemview.findViewById(R.id.total_invoice);
        }
    }
}
