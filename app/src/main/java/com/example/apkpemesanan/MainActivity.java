package com.example.apkpemesanan;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.apkpemesanan.Adapter.CategoryAdapter;
import com.example.apkpemesanan.Adapter.RecommendAdapter;
import com.example.apkpemesanan.Domain.CategoryDomain;
import com.example.apkpemesanan.Domain.FoodDomain;
import com.example.apkpemesanan.R;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapter, adapter2;
    private RecyclerView recyclerViewPopularList;
    private ArrayList<FoodDomain> foodList;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        recyclerViewCategory();
        recyclerViewPopular();
        bottomNavigation();
//        fetchDataFromServer();


//        double nilaiMataUang = 20000.0;
//
//        DecimalFormat decimalFormat = new DecimalFormat("#.000");
//
//        String tampilanMataUang = decimalFormat.format(nilaiMataUang);
//
//        View recommendLayout = getLayoutInflater().inflate(R.layout.viewholder_recommend, null);
//
//        TextView textViewMataUang = recommendLayout.findViewById(R.id.fee);
//        textViewMataUang.setText(tampilanMataUang);

//        EditText search = findViewById(R.id.search);
//        search.addTextChangedListener(new TextWatcher() {
//
//            public void afterTextChanged(Editable s) {
//
//                // you can call or do what you want with your EditText here
//
//                // yourEditText...
//            }
//
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            public void onTextChanged(CharSequence s, int start, int before, int count) {}
//        });
    }
    private void bottomNavigation() {
        LinearLayout homebtn = findViewById(R.id.homeBtn);
        LinearLayout cartbtn = findViewById(R.id.cartbtn);

        homebtn.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, MainActivity.class)));

        cartbtn.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, CartActivity.class)));
    }


    private void recyclerViewPopular() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewPopularList = findViewById(R.id.view2);
        recyclerViewPopularList.setLayoutManager(linearLayoutManager);
        ArrayList<FoodDomain> foodList = new ArrayList<>();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading ...");
        progressDialog.show();
        MainActivity activity = this;

        AndroidNetworking.get("http://192.168.101.122/ta/tampil.php")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        // do anything with response
                        try {
                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject data = jsonArray.getJSONObject(i);
                                FoodDomain item = new FoodDomain(
                                        data.getString("nama_menu"),
                                        data.getString("link_gambar"),
                                        data.getInt("fee"),
                                        data.getInt("id_menu"),
                                        data.getString("description")
                                );

                                foodList.add(item);
                                progressDialog.dismiss();


                            }

                            adapter2 = new RecommendAdapter(foodList, activity);
                            recyclerViewPopularList.setAdapter(adapter2);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                        adapter2.notifyDataSetChanged();
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Toast.makeText(getApplicationContext(),"Kesalahan kode" + error, Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                });
    }

//    private void recyclerViewCategory() {
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        recyclerViewCategoryList = findViewById(R.id.view1);
//        recyclerViewCategoryList.setLayoutManager(linearLayoutManager);
//
//        ArrayList<CategoryDomain> categoryList = new ArrayList<>();
//        categoryList.add(new CategoryDomain("steak", "cat_1"));
//        categoryList.add(new CategoryDomain("drink", "cat_2"));
////        categoryList.add(new CategoryDomain("Hotdog", "cat_3"));
////        categoryList.add(new CategoryDomain("drink", "cat_4"));
////        categoryList.add(new CategoryDomain("donut", "cat_5"));
//
//
//        adapter = new CategoryAdapter(categoryList);
//        recyclerViewCategoryList.setAdapter(adapter);
//    }
}
