package com.example.appdrinkfruit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appdrinkfruit.Adapter.NUOC_ADAPTER;
import com.example.appdrinkfruit.Model.NUOC;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Nuoctheoloai extends AppCompatActivity {
    RecyclerView recyclerView;
    NUOC_ADAPTER nuocAdapter ;
    TextView loainuoc;

    AppCompatButton btnBack;

    ArrayList<NUOC> nuocArrayList = new ArrayList<>();
    String MALOAI = "";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_nuoc_theo_loai);
        recyclerView = findViewById(R.id.rvchude);
        loainuoc = findViewById(R.id.loainuoc);
        nuocAdapter = new NUOC_ADAPTER(this, nuocArrayList);
        recyclerView.setAdapter(nuocAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2, RecyclerView.VERTICAL,false));
        btnBack = findViewById(R.id.btnbackloainuoc);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        Intent intent = getIntent();
        String tl = getIntent().getStringExtra("tenloai");
        loainuoc.setText("Các Loại "+tl);
        MALOAI = intent.getStringExtra("maloai");
        if(MALOAI.length() > 0) {
            loadNuoc();
        }
    }


    void loadNuoc() {
        Response.Listener<String> thanhcong = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        nuocArrayList.add(new NUOC(
                                jsonObject.getString("Tennuoc"),
                                jsonObject.getString("Hinhminhhoa"),
                                jsonObject.getString("Manuoc"),
                                jsonObject.getString("Maloai"),
                                jsonObject.getString("Dongia"),
                                jsonObject.getString("Mota"),
                                jsonObject.getString("Soluongban")

                        ));
                    }
                    nuocAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Toast.makeText(Nuoctheoloai.this,
                            "Lỗi lấy dữ liệu trang sachtheochude: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("TAG", "JSON Parsing Error: " + e.getMessage(), e);
                }
            }
        };

        Response.ErrorListener thatbai = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMsg = error.getMessage() != null ? error.getMessage() : "Lỗi mạng";
                Toast.makeText(Nuoctheoloai.this,
                        "Thất bại: " + errorMsg, Toast.LENGTH_LONG).show();
                Log.e("TAG", "onErrorResponse: " + errorMsg, error);
            }
        };

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                SEVER.laynuoctheoloai_url,
                thanhcong,
                thatbai) {
            @Nullable
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("maloai", MALOAI);
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data, "UTF-8");
                    return Response.success(jsonString, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }
            }
        };

        // Add the request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


}