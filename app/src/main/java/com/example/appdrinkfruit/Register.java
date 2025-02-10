package com.example.appdrinkfruit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    EditText edt1, edt2,edt3,edt4;
    AppCompatButton registerButton;
    TextView tvlogin;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edt1 = findViewById(R.id.user);
        edt2 = findViewById(R.id.pass);
        edt3 = findViewById(R.id.address);
        edt4 = findViewById(R.id.soDT);

        tvlogin = findViewById(R.id.tvlogin);
        registerButton = findViewById(R.id.moMH2);

        tvlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, LoginActivity.class); // Chuyển đến RegisterActivity
                startActivity(intent);
            }
        });

        // Sự kiện khi người dùng nhấn nút "Tạo tài khoản"
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edt1.getText().toString().trim();
                String password = edt2.getText().toString().trim();
                String address = edt3.getText().toString().trim();
                String sodienthoai = edt4.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty() || address.isEmpty() || sodienthoai.isEmpty() ) {
                    Toast.makeText(Register.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                } else {
                    // Gọi phương thức đăng ký
                    registerUser(username, password,address,sodienthoai);
                    Intent intent = new Intent(Register.this, LoginActivity.class); // Chuyển đến RegisterActivity
                    intent.putExtra("username", username);
                    intent.putExtra("password", password);
                    startActivity(intent);
                }
            }
        });
    }

    private void registerUser(String username, String password, String address,  String sodienthoai) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, SEVER.register_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Xử lý phản hồi từ server
                        Toast.makeText(Register.this, response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Xử lý lỗi
                        Toast.makeText(Register.this, "Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Gửi dữ liệu qua POST
                Map<String, String> params = new HashMap<>();
                params.put("tendangnhap", username);
                params.put("matkhau", password);
                params.put("diachi", address);
                params.put("sodienthoai", sodienthoai);
                return params;
            }
        };

        // Thêm yêu cầu vào hàng đợi
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}