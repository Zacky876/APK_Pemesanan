package com.example.apkpemesanan.Helper;

import android.content.Context;
import android.widget.Toast;

import com.example.apkpemesanan.Domain.FoodDomain;
import com.example.apkpemesanan.Interface.ChangeNumberItems;

import java.util.ArrayList;

public class ManagementCart {
    private Context context;
    private TinyDB tinyDB;

    public ManagementCart(Context context) {
        this.context = context;
        this.tinyDB = new TinyDB(context);
    }

    public void clearfood() {
        tinyDB.clear();
    }

    public void insertfood(FoodDomain item) {
        ArrayList<FoodDomain> listfood = getListCart();
        boolean exxistAlready = false;
        int n = 0;
        for(int i = 0; i < listfood.size(); i++) {
            if (listfood.get(i).getTitle().equals(item.getTitle())) {
                exxistAlready = true;
                n = i;
                break;
            }
        }

        if (exxistAlready) {
            listfood.get(n).setNumberInCart(item.getNumberInCart());
        }else {
            listfood.add(item);
        }

        tinyDB.putListObject("CardList", listfood);
        Toast.makeText(context, "Added to your cart", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<FoodDomain> getListCart() {
    return tinyDB.getListObject("CardList");
    }

    public void minusNumberFood(ArrayList<FoodDomain> listfood, int position, ChangeNumberItems changeNumberItems) {
        if (listfood.get(position).getNumberInCart()==1) {
            listfood.remove(position);
        }else  {
            listfood.get(position).setNumberInCart(listfood.get(position).getNumberInCart()-1);
        }

        tinyDB.putListObject("CardList", listfood);
        changeNumberItems.changed();

    }
    public void plusNumberFood(ArrayList<FoodDomain> listfood, int position,ChangeNumberItems changeNumberItems ) {
         listfood.get(position).setNumberInCart(listfood.get(position).getNumberInCart() + 1);
         tinyDB.putListObject("CardList", listfood);
         changeNumberItems.changed();

    }

    public double getTotalFee() {
        ArrayList<FoodDomain> listfood2 =getListCart();
        double fee = 0;
        for(int i = 0; i < listfood2.size(); i++) {
            fee = fee + (listfood2.get(i).getFee() * listfood2.get(i).getNumberInCart());
        }
        return fee;
    }
}
