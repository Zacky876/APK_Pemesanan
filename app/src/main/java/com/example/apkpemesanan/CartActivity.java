package com.example.apkpemesanan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.apkpemesanan.Adapter.KeranjangAdapter;
import com.example.apkpemesanan.Domain.FoodDomain;
import com.example.apkpemesanan.Helper.ManagementCart;
import com.example.apkpemesanan.Interface.ChangeNumberItems;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private Spinner spinner;
    private ArrayList<String> nomorMejaList;
    private KeranjangAdapter adapter;
    private RecyclerView recyclerViewList;
    private ManagementCart managementCart;
    private TextView totalFeeTxt, pajakTxt, totalTxt, EmptyTxt, checkoutbtn;
    private double pajak;
    private ScrollView scrollView;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        managementCart = new ManagementCart(this);

        spinner = findViewById(R.id.spinner);
        nomorMejaList = new ArrayList<>();

        // Buat adapter tanpa data
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nomorMejaList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Atur listener untuk spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedTable = nomorMejaList.get(position);
                // Lakukan apa pun yang perlu dilakukan dengan item yang dipilih di sini
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle when nothing is selected (if needed)
            }
        });

        // Permintaan ke API PHP untuk mendapatkan data nomor meja yang diperlukan
        AndroidNetworking.get("http://192.168.122.252/ta/nomor_meja.php")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d("TESS", response.toString());
                        try {
                            // Bersihkan nomorMejaList sebelum menambahkan data baru
                            nomorMejaList.clear();

                            // Mengisi nomorMejaList dengan nomor meja dari response JSON
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = response.getJSONObject(i);
                                String nomorMeja = obj.getString("nomor");
                                nomorMejaList.add(nomorMeja);
                            }

                            // Memberitahu adapter bahwa data telah berubah
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        // Menangani kesalahan saat permintaan
                        Log.e("Error", "Error: " + anError.getErrorBody());
                    }
                });

        initView();
        initList();
        bottomNavigation();
        getbundle();
        calculateCard();
    }

    private void getbundle() {
        Intent intent = getIntent();
        Serializable serializable = intent.getSerializableExtra("fooddomain");

        checkoutbtn.setOnClickListener(view -> {
            int total_harga = 0;

            ArrayList<FoodDomain> cartList = managementCart.getListCart();

            for(int i=0; i<cartList.size(); i++)
                total_harga += (cartList.get(i).getFee() * cartList.get(i).getNumberInCart());

            AndroidNetworking.post("http://192.168.122.252/ta/checkout.php") // Ganti dengan URL API yang benar
                    .setPriority(Priority.MEDIUM)
                    .addBodyParameter("nomor_meja", String.valueOf(spinner.getSelectedItemPosition() + 1))
                    .addBodyParameter("total_pembayaran", String.valueOf(total_harga))
                    .addBodyParameter("pajak", String.valueOf(pajak))
                    .setTag("KIRIM CHECKOUT TRANSAKSI")
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            managementCart.getListCart().forEach(tes -> {
                                AndroidNetworking.get("http://192.168.122.252/ta/order.php?nama_menu="+tes+"&jumlah_menu="+tes.getNumberInCart()+"&subtotal="+(tes.getFee() * tes.getNumberInCart())+"&nomor_meja=" + (spinner.getSelectedItemPosition() + 1)) // Ganti dengan URL API yang benar
                                        .setPriority(Priority.MEDIUM)
                                        .setTag("CHECKOUT DETAIL")
                                        .build()
                                        .getAsJSONObject(new JSONObjectRequestListener() {
                                            @Override
                                            public void onResponse(JSONObject res) {

                                            }

                                            @Override
                                            public void onError(ANError anError) {
//                                                Log.d("ERROR", anError.getErrorBody());
                                            }
                                        });
                            });
//                            try {
//                                Intent checkout = new Intent(CartActivity.this, CheckoutActivity.class);
//                                startActivity(checkout);
//                                checkout.putExtra("id_transaksi", response.getString("id_transaksi"));
//                            } catch (JSONException e) {
//                                throw new RuntimeException(e);
//                            }

                            try {
                                Intent checkout = new Intent(CartActivity.this, CheckoutActivity.class);
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
//            Intent intent1 = new Intent(CartActivity.this, CheckoutActivity.class);
//            startActivity(intent1);




            TextView jumlah_menu = findViewById(R.id.numberItemCart);


            });

//            Log.d("tes", jumlah_menu.getText().toString());


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
        double percent = 0.5;

        pajak = Math.round((managementCart.getTotalFee() * percent) * 100.0) / 100.0;
        double total = Math.round((managementCart.getTotalFee() + pajak) * 100.0) / 100.0;
        double itemtotal = Math.round(managementCart.getTotalFee() * 100.0) / 100.0;

//        Log.d("TES", String.valueOf(itemtotal));
        totalFeeTxt.setText("Rp." + itemtotal);
        pajakTxt.setText("Rp" + pajak);
        totalTxt.setText("Rp" + total);
    }

    private void initView() {
        totalFeeTxt = findViewById(R.id.total_item);
        pajakTxt = findViewById(R.id.pajak);
        totalTxt = findViewById(R.id.total_pembayaran);
        recyclerViewList = findViewById(R.id.view);
        scrollView = findViewById(R.id.scrollView);
        EmptyTxt = findViewById(R.id.EmptyTxt);
        checkoutbtn = findViewById(R.id.checkoutbtn);
        spinner = findViewById(R.id.spinner);
        linearLayout = findViewById(R.id.linear1);
    }
}

//        checkoutbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
//                startActivity(intent);
//
//                JSONObject postData = new JSONObject();
//                try {
//                    postData.put("item", "Your item data");
//                    // Add other necessary data
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                // Send the data to the server
//                sendDataToServer(postData.toString());
//            }
//        });
//    }
//    private void sendDataToServer(String data) {
//        // Code to send data to server using POST method
//        // You can use libraries like Volley or Retrofit for this purpose
//    }
//}