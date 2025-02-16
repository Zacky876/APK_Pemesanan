package com.example.apkpemesanan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
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

public class RegisterActivity extends AppCompatActivity {

    private TextView txtlogin;
    private EditText reguser, regpw, regemail;
    private AppCompatButton btnregis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initview();

        txtlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnregis.setOnClickListener(view -> register());
    }

    private void register() {
        String username = reguser.getText().toString().trim();
        String email = regemail.getText().toString().trim();
        String password = regpw.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "tidak boleh ada kososng",Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
            jsonObject.put("email", email);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post("http://10.0.2.2:8000/register")
                .addJSONObjectBody(jsonObject)
                .setTag("register")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean success = response.getString("status").equals("success");
                            if (success) {

                                Toast.makeText(RegisterActivity.this, "Register Berhasil", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Register Gagal", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RegisterActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        //            Log.d("LOGIN", anError.getErrorDetail());
                        System.out.println("Error Detail: " + anError.getErrorDetail());
                        System.out.println("Error Body: " + anError.getErrorBody());
                        System.out.println("Error Code: " + anError.getErrorCode());

                        anError.printStackTrace();

                        Toast.makeText(RegisterActivity.this, "Login gagal: " + anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initview() {
        regemail = findViewById(R.id.regemail);
        reguser = findViewById(R.id.reguser);
        regpw = findViewById(R.id.regpw);
        txtlogin = findViewById(R.id.txtlogin);
        btnregis = findViewById(R.id.btnregister);
    }
}