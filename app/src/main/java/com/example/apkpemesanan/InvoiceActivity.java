package com.example.apkpemesanan;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.apkpemesanan.Domain.FoodDomain;
import com.example.apkpemesanan.Helper.ManagementCart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InvoiceActivity extends AppCompatActivity {
    private ManagementCart managementCart;
    private TextView total_invoice, pjkInvoice, total, username, tanggal;
    private double pajak;
    private ScrollView scrollView;
    private LinearLayout orderItemsContainer;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private List<FoodDomain> foodList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        managementCart = new ManagementCart(this);
        initView();
        fetchInvoiceData();

        username.setText(pref.getString("username", ""));
    }

    private void calculateCard() {
        double percent = 0.1;
        double totalFee = calculateTotalFee();
        pajak = Math.round((totalFee * percent) * 100.0) / 100.0;
        double totall = Math.round((totalFee + pajak) * 100.0) / 100.0;
        double itemtotal = Math.round(totalFee * 100.0) / 100.0;

        total_invoice.setText("Rp." + itemtotal);
        pjkInvoice.setText("Rp." + pajak);
        total.setText("Rp." + totall);
    }

    private double calculateTotalFee() {
        double total = 0;
        for (FoodDomain food : foodList) {
            total += food.getFee() * food.getNumberInCart();
        }
        return total;
    }

    private void fetchInvoiceData() {
        Intent intent = getIntent();
        String id_transaksi = intent.getStringExtra("id_transaksi");

        if (id_transaksi == null) {
            Log.e("ERROR", "id_transaksi tidak ditemukan");
            return;
        }

        AndroidNetworking.get("http://10.0.2.2:8000/invoice/{id_transaksi}")
                .addPathParameter("id_transaksi", id_transaksi)
                .setTag("invoice")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equals("success")) {
                                JSONArray invoice = response.getJSONArray("invoice");
                                foodList.clear();

                                // Ambil data transaksi pertama untuk total dan pajak
                                if (invoice.length() > 0) {
                                    JSONObject firstItem = invoice.getJSONObject(0);
                                    total_invoice.setText("Rp." + firstItem.getString("item_total"));
                                    pjkInvoice.setText("Rp." + firstItem.getString("pajak"));
                                    total.setText("Rp." + firstItem.getString("total_pembayaran"));
                                    tanggal.setText(firstItem.getString("tanggal_pembayaran"));
                                }

                                // Proses setiap item pesanan
                                for (int i = 0; i < invoice.length(); i++) {
                                    JSONObject item = invoice.getJSONObject(i);

                                    FoodDomain food = new FoodDomain(
                                            item.getString("nama_menu"),
                                            "",
                                            item.getDouble("harga_menu"),
                                            item.getInt("id_menu"),
                                            ""
                                    );
                                    food.setNumberInCart(item.getInt("jumlah_menu"));
                                    foodList.add(food);
                                }

                                runOnUiThread(() -> {
                                    displayOrderItems();
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("JSON Error", "Error parsing JSON: " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        error.printStackTrace();
                        Log.e("Network Error", "Error: " + error.getMessage());
                    }
                });
    }

    private void displayOrderItems() {
        orderItemsContainer.removeAllViews();

        for (FoodDomain food : foodList) {
            LinearLayout itemRow = new LinearLayout(this);
            itemRow.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            itemRow.setOrientation(LinearLayout.HORIZONTAL);
            itemRow.setPadding(0, 8, 0, 8);

            // Food name
            TextView nameView = createItemTextView(food.getTitle(), 2f, Gravity.START);
            itemRow.addView(nameView);

            // Price per item
            TextView priceView = createItemTextView("Rp." + food.getFee(), 1f, Gravity.END);
            itemRow.addView(priceView);

            // Quantity
            TextView quantityView = createItemTextView(String.valueOf(food.getNumberInCart()), 1f, Gravity.CENTER);
            itemRow.addView(quantityView);

            // Total for this item
            double itemTotal = food.getFee() * food.getNumberInCart();
            TextView totalView = createItemTextView("Rp." + itemTotal, 1f, Gravity.END);
            itemRow.addView(totalView);

            orderItemsContainer.addView(itemRow);
        }
    }

    private TextView createItemTextView(String text, float weight, int gravity) {
        TextView textView = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, weight);
        params.setMargins(8, 0, 8, 0);
        textView.setLayoutParams(params);
        textView.setText(text);
        textView.setGravity(gravity);
        textView.setTextSize(14);
        return textView;
    }

    private void initView() {
        tanggal = findViewById(R.id.tanggal);
        username = findViewById(R.id.username);
        orderItemsContainer = findViewById(R.id.orderItemsContainer);
        total_invoice = findViewById(R.id.total_invoice);
        pjkInvoice = findViewById(R.id.pjkInvoice);
        total = findViewById(R.id.totall);

        pref = getSharedPreferences("LOGIN", MODE_PRIVATE);
        editor = pref.edit();
    }
}