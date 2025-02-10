package com.example.appdrinkfruit;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appdrinkfruit.Model.USER_MODEL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText edtUser, edtPass;
    AppCompatButton btnLogin, btnRegister;
    CheckBox checkBoxRemember;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Ánh xạ
        edtUser = findViewById(R.id.et1);
        edtPass = findViewById(R.id.et2);
        btnLogin = findViewById(R.id.bt1);
        btnRegister = findViewById(R.id.moMH2);
        checkBoxRemember = findViewById(R.id.checkboxRemember);
        checkBoxRemember.setChecked(true);



        sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        loadLoginInfo(); // Tự động điền nếu đã lưu trước đó

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String password = intent.getStringExtra("password");

        // Nếu có username và password, tự động điền vào các trường
        if (username != null) {
            edtUser.setText(username);
        }
        if (password != null) {
            edtPass.setText(password);
        }


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, Register.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUser.getText().toString().trim();
                String password = edtPass.getText().toString().trim();

                Response.Listener<String> thanhCong = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.length() > 0 && !response.equals("fail")) {
                            try {
                                JSONObject userJson = new JSONObject(response);
                                MainActivity.USER = new USER_MODEL(
                                        userJson.getString("tendangnhap"),
                                        userJson.getString("matkhau"),
                                        userJson.getString("diachi"),
                                        userJson.getString("sodienthoai")
                                );
                                if (checkBoxRemember.isChecked()) {
                                    saveLoginInfo(username, password); // Lưu thông tin nếu chọn checkbox
                                }
                                Intent intent = new Intent(LoginActivity.this, IntroduceActivity.class);
                                startActivity(intent);
                            } catch (JSONException e) {
                                Log.e("JSON Error", e.getMessage());
                            }
                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Tên đăng nhập hoặc mật khẩu không đúng", Toast.LENGTH_LONG).show();
                        }
                    }
                };

                Response.ErrorListener thatBai = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_LONG).show();
                        Log.e("*LOGIN*", volleyError.getMessage());
                    }
                };

                StringRequest stringRequest = new StringRequest(Request.Method.POST, SEVER.login_url, thanhCong, thatBai) {
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> params = new HashMap<>();
                        params.put("username", username);
                        params.put("password", password);
                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                requestQueue.add(stringRequest);
            }
        });
    }

    // Hàm lưu thông tin đăng nhập
    private void saveLoginInfo(String username, String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.putBoolean("remember", true);
        editor.apply();
    }

    // Hàm tự động điền thông tin đăng nhập nếu đã lưu trước đó
    private void loadLoginInfo() {
        boolean isRemembered = sharedPreferences.getBoolean("remember", false);
        if (isRemembered) {
            String username = sharedPreferences.getString("username", "");
            String password = sharedPreferences.getString("password", "");
            edtUser.setText(username);
            edtPass.setText(password);
            checkBoxRemember.setChecked(true);
        }
    }
}
