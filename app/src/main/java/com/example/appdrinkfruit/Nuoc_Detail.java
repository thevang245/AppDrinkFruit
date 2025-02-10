package com.example.appdrinkfruit;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appdrinkfruit.Model.NUOC;
import com.example.appdrinkfruit.Model.NUOCGIAMGIA;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

public class Nuoc_Detail extends AppCompatActivity {
    ImageView imgdetail;
    TextView tendetail, giadetail, giagiamdetail, motadetail, soluongbandetail,titleTennuoc;
    AppCompatButton themvaoGH, muaNgay, btnback;
    TextView btnTru, btnCong;
    TextView soLuong;
    int quantity = 1;

    public static String Tongtien;
    double priceValue = 0; // Giá của sản phẩm
    double totalAmount = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuoc_detail);

        btnCong = findViewById(R.id.detail_cong);
        btnTru = findViewById(R.id.detail_tru);
        soLuong = findViewById(R.id.detail_soluong);
        titleTennuoc = findViewById(R.id.titletennuoc);
        String tennuoc = getIntent().getStringExtra("tennuoc");
        titleTennuoc.setText(tennuoc);

        btnback = findViewById(R.id.detail_backchitiet);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });





        btnCong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity++;
                soLuong.setText(String.valueOf(quantity));
            }
        });

        btnTru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity > 1) {
                    quantity--;
                    soLuong.setText(String.valueOf(quantity));
                } else {
                    quantity = 1; // Giữ giá trị tối thiểu là 1
                }
            }
        });

        imgdetail = findViewById(R.id.img_detail);
        tendetail = findViewById(R.id.tennuoc_detail);
        giadetail = findViewById(R.id.gia_detail);
        giagiamdetail = findViewById(R.id.giagiam_detail);
        motadetail = findViewById(R.id.mota_detail);
        soluongbandetail = findViewById(R.id.soluongban_detail);
        themvaoGH = findViewById(R.id.buttonAdd);
        muaNgay = findViewById(R.id.mua_detail);

        if(Tongtien != null) {

        }




        muaNgay.setOnClickListener(v -> {
            Intent intent = new Intent(Nuoc_Detail.this, MuangayActivity.class);
            startActivity(intent);
        });

        // Nhận thông tin sản phẩm từ Intent
        Intent intentngg = getIntent();
        NUOCGIAMGIA ngg = (NUOCGIAMGIA) intentngg.getSerializableExtra("nuocgiamgia");

        if (ngg != null) {
            loadProductDetails(ngg.title, ngg.img, ngg.price, ngg.newprice, ngg.description, ngg.quantity);
        }

        Intent intent = getIntent();
        NUOC n = (NUOC) intent.getSerializableExtra("nuoc");
        if (n != null) {
            loadProductDiscountDetails(n.title, n.img, n.price,n.price, n.description, n.quantity); // Giả sử giá mới giống giá cũ
        }

        themvaoGH.setOnClickListener(v -> {
            if (n != null) {
                addToCart(n.title, n.img, n.price, String.valueOf(quantity));
            } else if (ngg != null) {
                addToCart(ngg.title, ngg.img, ngg.newprice, String.valueOf(quantity));
            }
        });


    }

    private void loadProductDetails(String title, String img, String price, String newPrice, String description, String quantitySold) {
        double priceValue = Double.parseDouble(price);
        NumberFormat formatter = new DecimalFormat("#,###");
        String formattedPrice = formatter.format(priceValue);

        double newPriceValue = Double.parseDouble(newPrice);
        NumberFormat formatterr = new DecimalFormat("#,###");
        String formattedNewPrice = formatterr.format(newPriceValue);
        giagiamdetail.setText(formattedNewPrice+"đ");

        tendetail.setText(title);
        motadetail.setText(description);
        soluongbandetail.setText("Đã bán: " + quantitySold);

        giadetail.setText(formattedPrice + "đ");
        giadetail.setPaintFlags(giadetail.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        Picasso.get()
                .load(SEVER.hinhanh_url + img)
                .placeholder(R.drawable.rmnc)
                .error(R.drawable.rmnc)
                .into(imgdetail);
    }

    private void loadProductDiscountDetails(String title, String img, String price, String newPrice, String description, String quantitySold) {
        double priceValue = Double.parseDouble(price);
        NumberFormat formatter = new DecimalFormat("#,###");
        String formattedPrice = formatter.format(priceValue);

        double newPriceValue = Double.parseDouble(newPrice);
        NumberFormat formatterr = new DecimalFormat("#,###");
        String formattedNewPrice = formatterr.format(newPriceValue);


        tendetail.setText(title);
        motadetail.setText(description);
        soluongbandetail.setText("Đã bán: " + quantitySold);

        giagiamdetail.setText(formattedNewPrice + "đ");
        giadetail.setPaintFlags(giadetail.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        Picasso.get()
                .load(SEVER.hinhanh_url + img)
                .placeholder(R.drawable.rmnc)
                .error(R.drawable.rmnc)
                .into(imgdetail);
    }

    private void addToCart(String tenNuoc, String hinhNuoc, String donGia, String soLuong) {
        String currentUsername = getCurrentUsername(); // Lấy tên đăng nhập hiện tại
        if (currentUsername == null) {
            Toast.makeText(Nuoc_Detail.this, "Bạn chưa đăng nhập!", Toast.LENGTH_SHORT).show();
            return; // Ngừng thực hiện nếu tên đăng nhập là null
        }

        RequestQueue queue = Volley.newRequestQueue(Nuoc_Detail.this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, SEVER.themsanpham_url,
                response -> {
                    try {
                        Log.d(TAG, "API Response: " + response);
                        JSONObject jsonResponse = new JSONObject(response);
                        String message = jsonResponse.getString("message");
                        if ("Thêm vào giỏ hàng thành công".equals(message)) {
                            Toast.makeText(Nuoc_Detail.this, "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Nuoc_Detail.this, "" + message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(Nuoc_Detail.this, "Lỗi phân tích dữ liệu phản hồi", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.d(TAG, "Error: " + error.toString());
                    Toast.makeText(Nuoc_Detail.this, "Lỗi khi thêm vào giỏ hàng: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("Tennuoc", tenNuoc);         // Tên sản phẩm
                params.put("Hinhnuoc", hinhNuoc);       // Hình sản phẩm
                params.put("Dongia", donGia);           // Giá sản phẩm
                params.put("Soluong", soLuong);         // Số lượng sản phẩm
                params.put("Tendangnhap", currentUsername); // Thêm tên đăng nhập vào params
                return params;
            }
        };

        // Thêm yêu cầu vào hàng đợi
        queue.add(postRequest);
    }



    private String getCurrentUsername() {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null); // Đảm bảo dùng cùng khóa với trong LoginActivity
        if (username == null) {
            Log.d(TAG, "Tên đăng nhập chưa được lưu.");
        }
        return username;
    }


}
