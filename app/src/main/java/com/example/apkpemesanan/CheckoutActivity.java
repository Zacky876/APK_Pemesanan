package com.example.apkpemesanan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.apkpemesanan.Adapter.CheckoutAdapter;
import com.example.apkpemesanan.Adapter.KeranjangAdapter;
import com.example.apkpemesanan.Domain.FoodDomain;
import com.example.apkpemesanan.Helper.ManagementCart;
import com.example.apkpemesanan.Interface.ChangeNumberItems;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CheckoutActivity extends AppCompatActivity {

    private Spinner spinner;
    private CheckoutAdapter adapter;
    private RecyclerView recyclerViewList;
    private ManagementCart managementCart;
    private TextView total_invoice, pjkInvoice, total, invoice, tanggal;
    private double pajak;
    private ScrollView scrollView;
    private LinearLayout linearLayout;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        managementCart = new ManagementCart(this);

//        bottomNavigation();
//        getbundle();
        initList();
        initView();
        recyclerViewPopular();
    }





    private void initList() {


    }

    private void calculateCard() {
        double percent = 0.5;

        pajak = Math.round((managementCart.getTotalFee() * percent) * 100.0) / 100.0;
        double totall = Math.round((managementCart.getTotalFee() + pajak) * 100.0) / 100.0;
        double itemtotal = Math.round(managementCart.getTotalFee() * 100.0) / 100.0;

        total_invoice.setText("Rp." + itemtotal);
        pjkInvoice.setText("Rp" + pajak);
        total.setText("Rp" + totall);
    }

//    private void bottomNavigation() {
//        LinearLayout homebtn = findViewById(R.id.homeBtn);
//        LinearLayout cartbtn = findViewById(R.id.cartbtn);
//
//        homebtn.setOnClickListener(view -> startActivity(new Intent(CheckoutActivity.this, MainActivity.class)));
//
//        cartbtn.setOnClickListener(view -> startActivity(new Intent(CheckoutActivity.this, CartActivity.class)));
//    }

    private void recyclerViewPopular() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewList = findViewById(R.id.view1);
        recyclerViewList.setLayoutManager(linearLayoutManager);
        adapter = new CheckoutAdapter(managementCart.getListCart(), this, new ChangeNumberItems() {
            @Override
            public void changed() {
                calculateCard();
            }
        });

        recyclerViewList.setAdapter(adapter);


        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading ...");
        progressDialog.show();
        progressDialog.dismiss();
        CheckoutActivity activity = this;

        Intent intent = getIntent();
        String id_transaksi = intent.getStringExtra("id_transaksi");
//            Log.d("TES", id_transaksi);

        Log.d("AAAAAAAAAAAA", id_transaksi);

        AndroidNetworking.get("https://192.168.1.12/ta/invoice.php?id_transaksi"+ id_transaksi)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray responsea) {
                        // do anything with response
                            try {
                                for (int i = 0; i < responsea.length(); i++) {
                                    JSONObject data = responsea.getJSONObject(i);
//                                    Log.d("satu", data.getString("item_total"));
                                }
                            }catch(Exception e) {

                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        error.printStackTrace();
                            Log.d("TESSS", error.getMessage());
                    }
                });
            // Handle jika intent null
        }




    private void initView() {
        tanggal = findViewById(R.id.tanggal);
        invoice = findViewById(R.id.invoice);
        recyclerViewList = findViewById(R.id.view1);
        total_invoice = findViewById(R.id.total_invoice);
        pjkInvoice = findViewById(R.id.pjkInvoice);
        total = findViewById(R.id.totall);
    }
}
