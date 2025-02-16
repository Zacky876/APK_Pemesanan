package com.example.apkpemesanan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.apkpemesanan.Adapter.RecommendAdapter;
import com.example.apkpemesanan.Domain.FoodDomain;

import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerViewMakanan, recyclerViewminuman, recyclerViewsnack, recyclerViewdesert;
    private RecyclerView  recyclerViewSearch;
    private RecommendAdapter recommendAdapterMakanan;
    private RecommendAdapter recommendAdapterMinuman;
    private RecommendAdapter recommendAdapterSnack;
    private RecommendAdapter recommendAdapterDesert;
    private RecommendAdapter searchAdapter;

    private ArrayList<FoodDomain> foodListMakanan = new ArrayList<>();
    private ArrayList<FoodDomain> foodListMinuman = new ArrayList<>();
    private ArrayList<FoodDomain> foodListSnack = new ArrayList<>();
    private ArrayList<FoodDomain> foodListDesert = new ArrayList<>();
    private TextView txtuser, txtmakanan, txtminuman, txtdesert, txtsnack;
    private SearchView searchView;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initview();
        bottomNavigation();
        search();

        searchView.setQueryHint("Search your favorite food");
        searchView.setIconifiedByDefault(false);
        
        recyclerViewPopular(1, recyclerViewMakanan, foodListMakanan);
        recyclerViewPopular(2, recyclerViewminuman, foodListMinuman);
        recyclerViewPopular(3, recyclerViewsnack, foodListSnack);
        recyclerViewPopular(4, recyclerViewdesert, foodListDesert);
    }

    private void bottomNavigation() {
        LinearLayout homebtn = findViewById(R.id.homeBtn);
        LinearLayout cartbtn = findViewById(R.id.cartbtn);
        LinearLayout Logoutbtn = findViewById(R.id.logout);

        homebtn.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, MainActivity.class)));
        cartbtn.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, CartActivity.class)));

        Logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.clear();
                editor.apply();

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }

        });

    }

    private void recyclerViewPopular(int idcategory, RecyclerView recyclerView,  ArrayList<FoodDomain> foodList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        RecommendAdapter adapter = new RecommendAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);

        if (idcategory == 1) {
            recommendAdapterMakanan = adapter;
        } else if (idcategory == 2) {
            recommendAdapterMinuman = adapter;
        } else if (idcategory == 3) {
            recommendAdapterSnack = adapter;
        } else if (idcategory == 4) {
            recommendAdapterDesert = adapter;
        }

        AndroidNetworking.get("http://10.0.2.2:8000/menu")
                .addQueryParameter("id_category", String.valueOf(idcategory))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        try {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject data = jsonArray.getJSONObject(i);

                                // Pastikan field sesuai dengan yang dikembalikan API
                                foodList.add(new FoodDomain(
                                        data.getString("nama_menu"),
                                        data.getString("id_gambar"),
                                        data.getInt("fee"),
                                        data.getInt("id_menu"),
                                        data.getString("description")
                                ));
                            }
                            adapter.updateData(foodList);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        // Handle error jika terjadi masalah saat mengakses API
                        anError.printStackTrace();
                    }
                });
    }

    private void search() {
        searchAdapter = new RecommendAdapter(new ArrayList<>(), this);
        recyclerViewSearch.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerViewSearch.setAdapter(searchAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                txtmakanan.setVisibility(!s.isEmpty() ? View.GONE : View.VISIBLE);
                txtminuman.setVisibility(!s.isEmpty() ? View.GONE : View.VISIBLE);
                txtsnack.setVisibility(!s.isEmpty() ? View.GONE : View.VISIBLE);
                txtdesert.setVisibility(!s.isEmpty() ? View.GONE : View.VISIBLE);

                if (!s.isEmpty()) {
                    List<FoodDomain> filteredList = new ArrayList<>();

                    // Ambil hasil pencarian dari masing-masing kategori
                    if (recommendAdapterMakanan != null) {
                        filteredList.addAll(recommendAdapterMakanan.getFilteredItems(s));
                    }
                    if (recommendAdapterMinuman != null) {
                        filteredList.addAll(recommendAdapterMinuman.getFilteredItems(s));
                    }
                    if (recommendAdapterSnack != null) {
                        filteredList.addAll(recommendAdapterSnack.getFilteredItems(s));
                    }
                    if (recommendAdapterDesert != null) {
                        filteredList.addAll(recommendAdapterDesert.getFilteredItems(s));
                    }

                    // Sembunyikan RecyclerView kategori dan tampilkan RecyclerView pencarian
                    recyclerViewMakanan.setVisibility(View.GONE);
                    recyclerViewminuman.setVisibility(View.GONE);
                    recyclerViewsnack.setVisibility(View.GONE);
                    recyclerViewdesert.setVisibility(View.GONE);
                    recyclerViewSearch.setVisibility(View.VISIBLE);

                    searchAdapter.updateList(filteredList);
                    searchAdapter.notifyDataSetChanged();
                } else {
                    // Jika input kosong, kembalikan tampilan awal
                    recyclerViewMakanan.setVisibility(View.VISIBLE);
                    recyclerViewminuman.setVisibility(View.VISIBLE);
                    recyclerViewsnack.setVisibility(View.VISIBLE);
                    recyclerViewdesert.setVisibility(View.VISIBLE);
                    recyclerViewSearch.setVisibility(View.GONE);
                }
                return true;
            }
        });
    }

    private void initview() {
        txtuser = findViewById(R.id.txtuser);
        txtmakanan = findViewById(R.id.txtmakanan);
        txtminuman = findViewById(R.id.txtminuman);
        txtdesert = findViewById(R.id.txtdesert);
        txtsnack = findViewById(R.id.txtsnack);
        searchView = findViewById(R.id.searchview);
        recyclerViewMakanan = findViewById(R.id.recyclerViewMakanan);
        recyclerViewminuman = findViewById(R.id.recyclerViewMinuman);
        recyclerViewsnack = findViewById(R.id.recyclerViewSnack);
        recyclerViewdesert = findViewById(R.id.recyclerViewDesert);
        recyclerViewSearch = findViewById(R.id.recyclerViewSearch);

        pref = getSharedPreferences("LOGIN", MODE_PRIVATE);
        editor = pref.edit();
    }
}
