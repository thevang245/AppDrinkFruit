package com.example.appdrinkfruit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.appdrinkfruit.Fragment.CartFragment;
import com.example.appdrinkfruit.Fragment.SPDCFragment;
import com.example.appdrinkfruit.Model.HOADON;

import java.util.ArrayList;

public class MuangayActivity extends AppCompatActivity  {

    // Khai báo các thành phần giao diện
    EditText ten;
    EditText soDt;
    EditText diaChi;
    EditText soNha;
    AppCompatButton btnDatHang;
    AppCompatButton btnback;
    TextView total;
    ArrayList<HOADON> hoadonArrayList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muangay_acitvity); // ánh xạ đến file XML của bạn


        ten = findViewById(R.id.full_name);
        soDt = findViewById(R.id.phone_number);
        diaChi = findViewById(R.id.diachi);
        soNha = findViewById(R.id.address_detail);
        btnDatHang = findViewById(R.id.btndathang);
        btnback = findViewById(R.id.btnback);
        total = findViewById(R.id.datngay_tongTien);

        soDt.setText(MainActivity.USER.sodienthoai);
        ten.setText(MainActivity.USER.tendangnhap);
        diaChi.setText(MainActivity.USER.diachi);

//        ArrayList<HOADON> selectedProducts = (ArrayList<HOADON>) getIntent().getSerializableExtra("spdc");
//        if (selectedProducts != null) {
//            SPDCFragment fragment = SPDCFragment.newInstance(selectedProducts);
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            FragmentTransaction transaction = fragmentManager.beginTransaction();
//            transaction.replace(R.id.framelayout_dathang, fragment);
//            transaction.commit();
//        } else {
//            // Xử lý khi selectedProducts là null, như hiển thị thông báo lỗi hoặc tắt Activity
//            Toast.makeText(this, "Không có sản phẩm được chọn", Toast.LENGTH_SHORT).show();
//            finish();
//        }


        String tongtien = getIntent().getStringExtra("tongtien");
        if(tongtien != null) {
            total.setText(tongtien);
        }





        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btnDatHang.setOnClickListener(view -> {
            // Lấy dữ liệu từ các trường
            String fullName = ten.getText().toString().trim();
            String phoneNumber = soDt.getText().toString().trim();
            String address = diaChi.getText().toString().trim();
            String addressDetail = soNha.getText().toString().trim();
            // Kiểm tra thông tin có hợp lệ không
            if (fullName.isEmpty() || phoneNumber.isEmpty() || addressDetail.isEmpty() || address.isEmpty()) {
                // Hiển thị thông báo nếu còn thiếu thông tin
                Toast.makeText(MuangayActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                // Đặt hàng thành công, xóa các sản phẩm đã chọn trong giỏ hàng
                FragmentManager fragmentManager = getSupportFragmentManager();
                CartFragment cartFragment = (CartFragment) fragmentManager.findFragmentByTag("CartFragment");
                // Điều hướng sang trang thành công
                Intent intent = new Intent(MuangayActivity.this, DatsuccessActivity.class);
                startActivity(intent);
                finish();
            }
        });




    }

}