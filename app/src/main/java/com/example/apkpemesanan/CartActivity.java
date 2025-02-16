package com.example.apkpemesanan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.apkpemesanan.Adapter.KeranjangAdapter;
import com.example.apkpemesanan.Domain.FoodDomain;
import com.example.apkpemesanan.Helper.ManagementCart;
import com.example.apkpemesanan.Interface.ChangeNumberItems;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    private ArrayList<String> nomorMejaList;
    private KeranjangAdapter adapter;
    private RecyclerView recyclerViewList;
    private ManagementCart managementCart;
    private TextView totalFeeTxt, pajakTxt, totalTxt, EmptyTxt, checkoutbtn;
    private double pajak;
    private ScrollView scrollView;
    private LinearLayout linearLayout;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        managementCart = new ManagementCart(this);

        initView();
        initList();
        bottomNavigation();
        getbundle();
        calculateCard();

    }

    private void getbundle() {
        Intent intent = getIntent();
        Serializable serializable = intent.getSerializableExtra("fooddomain");

        pref = getSharedPreferences("LOGIN", MODE_PRIVATE);
        int id_user = pref.getInt("id_user", 0);

        checkoutbtn.setOnClickListener(view -> {
            int total_harga = 0;

            ArrayList<FoodDomain> cartList = managementCart.getListCart();

            for(int i=0; i<cartList.size(); i++)
                total_harga += (cartList.get(i).getFee() * cartList.get(i).getNumberInCart());

            JSONObject checkoutdata = new JSONObject();
            try {
                checkoutdata.put("id_user", id_user);
                checkoutdata.put("total_pembayaran", total_harga);
                checkoutdata.put("pajak", pajak);
            }catch (JSONException e) {
                e.printStackTrace();
            }

            AndroidNetworking.post("http://10.0.2.2:8000/checkout")
                    .setPriority(Priority.MEDIUM)
                    .addJSONObjectBody(checkoutdata)
                    .setTag("KIRIM CHECKOUT TRANSAKSI")
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            for (FoodDomain food : managementCart.getListCart() ) {
                                JSONObject orderdata = new JSONObject();

                                try {
                                    orderdata.put("id_menu", food.getId_menu());
                                    orderdata.put("jumlah_menu", food.getNumberInCart());
                                    orderdata.put("subtotal", food.getFee() * food.getNumberInCart());
                                    orderdata.put("pajak", pajak);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                AndroidNetworking.post("http://10.0.2.2:8000/order")
                                        .setPriority(Priority.MEDIUM)
                                        .addJSONObjectBody(orderdata)
                                        .setTag("CHECKOUT DETAIL")
                                        .build()
                                        .getAsJSONObject(new JSONObjectRequestListener() {
                                            @Override
                                            public void onResponse(JSONObject res) {
                                                Log.d("SUCCES", "data berhasil terkirim");

                                            }

                                            @Override
                                            public void onError(ANError anError) {
                                                Log.d("ERROR", "body" + anError.getErrorBody());
                                            }
                                        });
                            }

                            try {
                                Intent checkout = new Intent(CartActivity.this, InvoiceActivity.class);
                                checkout.putExtra("id_transaksi", response.getString("id_transaksi"));
                                startActivity(checkout);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        @Override
                        public void onError(ANError anError) {
                            Log.d("ERROR", anError.getErrorBody());
                        }
                    });
            TextView jumlah_menu = findViewById(R.id.numberItemCart);
        });
    }

    private void cleardata() {
        managementCart.clearfood();
        initList();
    }

    private void bottomNavigation() {
        LinearLayout homebtn = findViewById(R.id.homeBtn);
        LinearLayout cartbtn = findViewById(R.id.cartbtn);

        homebtn.setOnClickListener(view -> startActivity(new Intent(CartActivity.this, MainActivity.class)));

        cartbtn.setOnClickListener(view -> startActivity(new Intent(CartActivity.this, CartActivity.class)));
    }

    private void initList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewList.setLayoutManager(linearLayoutManager);
        adapter = new KeranjangAdapter(managementCart.getListCart(), this, new ChangeNumberItems() {
            @Override
            public void changed() {
                calculateCard();
            }
        });

        recyclerViewList.setAdapter(adapter);
        if (managementCart.getListCart().isEmpty()) {
            EmptyTxt.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        } else {
            EmptyTxt.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("SetTextI18n")
    private void calculateCard() {
        double percent = 0.05;

        pajak = Math.round((managementCart.getTotalFee() * percent) * 100.0) / 100.0;
        double total = Math.round((managementCart.getTotalFee() + pajak) * 100.0) / 100.0;
        double itemtotal = Math.round(managementCart.getTotalFee() * 100.0) / 100.0;

//        Log.d("TES", String.valueOf(itemtotal));
        totalFeeTxt.setText("Rp." + itemtotal);
        pajakTxt.setText("Rp." + pajak);
        totalTxt.setText("Rp." + total);
    }

    private void initView() {
        totalFeeTxt = findViewById(R.id.total_item);
        pajakTxt = findViewById(R.id.pajak);
        totalTxt = findViewById(R.id.total_pembayaran);
        recyclerViewList = findViewById(R.id.view);
        scrollView = findViewById(R.id.scrollView);
        EmptyTxt = findViewById(R.id.EmptyTxt);
        checkoutbtn = findViewById(R.id.checkoutbtn);
        linearLayout = findViewById(R.id.linear1);
    }
}
