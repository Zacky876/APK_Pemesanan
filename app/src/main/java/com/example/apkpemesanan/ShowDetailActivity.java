package com.example.apkpemesanan;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.apkpemesanan.Domain.FoodDomain;
import com.example.apkpemesanan.Helper.ManagementCart;
import com.squareup.picasso.Picasso;

import java.io.Serializable;

public class ShowDetailActivity extends AppCompatActivity {
private TextView addTocart, title, fee, description, numOrder, totalprice, star, time, calory;
private ImageView plusbtn, minusbtn, picfood;
private FoodDomain foodDomain;
private int numberOrder = 1;
private ManagementCart managementCart;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail);

        managementCart = new ManagementCart(this);
        
        initView();
        getbundle();
        bottomNavigation();
    }

    private void bottomNavigation() {
        LinearLayout homebtn = findViewById(R.id.homeBtn);
        LinearLayout cartbtn = findViewById(R.id.cartbtn);

        homebtn.setOnClickListener(view -> startActivity(new Intent(ShowDetailActivity.this, MainActivity.class)));

        cartbtn.setOnClickListener(view -> startActivity(new Intent(ShowDetailActivity.this, CartActivity.class)));
    }


    private void getbundle() {
        Intent intent = getIntent();
        Serializable serializable = intent.getSerializableExtra("fooddomain");

//        Log.d("TES",serializable);
        if (serializable instanceof FoodDomain) {
            foodDomain = (FoodDomain) serializable;

//            int drawableResourceId = this.getResources().getIdentifier(foodDomain.getPic(), "drawable", this.getPackageName());
//            Glide.with(this)
//                    .load(drawableResourceId)
//                    .into(picfood);
            Picasso.get().load(foodDomain.getPic()).into(picfood);


            title.setText(foodDomain.getTitle());
            fee.setText("Rp." + foodDomain.getFee());
            description.setText(foodDomain.getDescription());
            numOrder.setText(String.valueOf(numberOrder));
//            calory.setText(foodDomain.getCalories() + "calories");
//            star.setText(foodDomain.getStar() + "");
//            time.setText(foodDomain.getTime() + " menit");
            totalprice.setText("Rp." + (numberOrder * foodDomain.getFee()));
        }



        plusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberOrder = numberOrder + 1;
                numOrder.setText(String.valueOf(numberOrder));
                totalprice.setText("Rp." + (numberOrder * foodDomain.getFee()));
            }
        });

       minusbtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if (numberOrder > 1) {
                   numberOrder = numberOrder - 1;
                   numOrder.setText(String.valueOf(numberOrder));
               }
               numOrder.setText(String.valueOf(numberOrder));
               totalprice.setText("Rp." + (numberOrder * foodDomain.getFee()));
           }
       });

        addTocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               foodDomain.setNumberInCart(numberOrder);
               managementCart.insertfood(foodDomain);
            }
        });
    }


    private void initView() {

        addTocart = findViewById(R.id.AddToCartbtn);
        title = findViewById(R.id.textView12);
        fee = findViewById(R.id.price);
        description = findViewById(R.id.description);
        numOrder = findViewById(R.id.numberItemCart);
        plusbtn = findViewById(R.id.PlusCartbtn);
        minusbtn = findViewById(R.id.minusCartbtn);
        picfood = findViewById(R.id.foodpic);
        totalprice = findViewById(R.id.totalPrice);
//        star = findViewById(R.id.star);
//        time = findViewById(R.id.time);
//        calory = findViewById(R.id.calories);
    }
}