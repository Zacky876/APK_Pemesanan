package com.example.apkpemesanan.Helper;

import android.content.Context;
import android.util.Log;
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

    public void updateCartCount() {
        int cartCount = getCartCount();
        android.content.SharedPreferences cartPrefs = context.getSharedPreferences("CART", android.content.Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = cartPrefs.edit();
        editor.putInt("cartCount", cartCount);
        editor.apply();
    }

    public int getCartCount() {
        ArrayList<FoodDomain> listFood = getListCart();
        int count = 0;
        for (FoodDomain item : listFood) {
            count += item.getNumberInCart();
        }
        return count;
    }

    public int getTotalItem() {
        return getCartCount();
    }

    public void clearfood() {
        tinyDB.clear();
        updateCartCount(); // Add this line to reset count when cart is cleared
    }

    public void insertfood(FoodDomain item) {
        // Your existing code
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

        tinyDB.putListObject("CartList", listfood);
        updateCartCount(); // Add this line
        Log.d("LIST CART", "Isi list: " + getListCart().toString());
    }

    public ArrayList<FoodDomain> getListCart() {
    return tinyDB.getListObject("CartList");
    }

    public void minusNumberFood(ArrayList<FoodDomain> listfood, int position, ChangeNumberItems changeNumberItems) {
        // Your existing code
        if (listfood.get(position).getNumberInCart()==1) {
            listfood.remove(position);
        }else  {
            listfood.get(position).setNumberInCart(listfood.get(position).getNumberInCart()-1);
        }

        tinyDB.putListObject("CartList", listfood);
        updateCartCount(); // Add this line
        changeNumberItems.changed();
    }
    public void plusNumberFood(ArrayList<FoodDomain> listfood, int position,ChangeNumberItems changeNumberItems ) {
        // Your existing code
        listfood.get(position).setNumberInCart(listfood.get(position).getNumberInCart() + 1);
        tinyDB.putListObject("CartList", listfood);
        updateCartCount(); // Add this line
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
