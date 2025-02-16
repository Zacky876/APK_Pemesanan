package com.example.apkpemesanan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private TextView tvSignUp;
    private EditText edtusername, edtPw;
    private AppCompatButton btnLogin;

    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initview();

        int id_user = pref.getInt("id_user", 0);
        if (id_user != 0) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


        btnLogin.setOnClickListener(view -> loginuser());
    }

    private void loginuser() {

        String identifier = edtusername.getText().toString().trim();
        String password = edtPw.getText().toString().trim();

        if (identifier.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Username atau Password tidak boleh kososng",Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("identifier", identifier);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post("http://10.0.2.2:8000/login")
                .addJSONObjectBody(jsonObject)
                .setTag("login")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean success = response.getString("status").equals("success");
                            if (success) {
                                SharedPreferences.Editor edit = pref.edit();
                                edit.putInt("id_user", response.getJSONObject("user").getInt("id_user"));
                                edit.putString("username", response.getJSONObject("user").getString("username"));
                                edit.apply();

                                Toast.makeText(LoginActivity.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Login Gagal", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    }
            @Override
            public void onError(ANError anError) {
    //            Log.d("LOGIN", anError.getErrorDetail());
                System.out.println("Error Detail: " + anError.getErrorDetail());
                System.out.println("Error Body: " + anError.getErrorBody());
                System.out.println("Error Code: " + anError.getErrorCode());

                anError.printStackTrace();

                Toast.makeText(LoginActivity.this, "Login gagal: " + anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initview() {
        edtusername = findViewById(R.id.edtuser);
        edtPw = findViewById(R.id.edtpw);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignUp = findViewById(R.id.tvSignUp);

        pref = getSharedPreferences("LOGIN", MODE_PRIVATE);

    }
}