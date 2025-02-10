package com.example.appdrinkfruit.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appdrinkfruit.Adapter.HOADON_ADAPTER;
import com.example.appdrinkfruit.Model.HOADON;
import com.example.appdrinkfruit.MuangayActivity;
import com.example.appdrinkfruit.R;
import com.example.appdrinkfruit.SEVER;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartFragment extends Fragment implements HOADON_ADAPTER.OnTotalPriceUpdateListener {

    RecyclerView rvCart;
    ArrayList<HOADON> hoadonArrayList = new ArrayList<>();
    HOADON_ADAPTER hoadonAdapter;
    AppCompatButton dathang;
    TextView tongTien;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_layout_cart, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rvCart = view.findViewById(R.id.rv_cart);
        hoadonAdapter = new HOADON_ADAPTER(getActivity(), hoadonArrayList, this);
        rvCart.setAdapter(hoadonAdapter);
        rvCart.setLayoutManager(new GridLayoutManager(getContext(), 1));
        dathang = view.findViewById(R.id.btndathang);

        tongTien = view.findViewById(R.id.sum);



        dathang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOrderButtonClick();
            }
        });

        loadsanpham(); // Gọi hàm để load sản phẩm

    }

    // Xử lý nút Đặt hàng
    private void handleOrderButtonClick() {
        // Lấy giá trị từ TextView tổng tiền
        String tongTienText = tongTien.getText().toString().trim();

        // Kiểm tra xem tổng tiền có rỗng hoặc bằng "0" không
        if (tongTienText.isEmpty() || tongTienText.equals("0 VND")) {
            Toast.makeText(getContext(), "Bạn chưa chọn sản phẩm nào để mua", Toast.LENGTH_LONG).show();
        } else {
            // Lặp qua danh sách hoadonArrayList để tìm các sản phẩm đã được tích chọn
            ArrayList<HOADON> selectedItems = new ArrayList<>();
            for (HOADON hoadon : hoadonArrayList) {
                if (hoadon.isChecked()) {
                    selectedItems.add(hoadon);  // Thêm sản phẩm đã chọn vào danh sách
                }
            }

            // Kiểm tra xem có sản phẩm nào được tích chọn không
            if (selectedItems.isEmpty()) {
                Toast.makeText(getContext(), "Bạn chưa chọn sản phẩm nào để thanh toán.", Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(getContext(),MuangayActivity.class);
                intent.putExtra("tongtien",tongTienText);
                startActivity(intent);
                xoaSanPhamDaChon(selectedItems);
            }
        }
    }


    private void xoaSanPhamDaChon(ArrayList<HOADON> selectedItems) {
        for (HOADON hoadon : selectedItems) {
            RequestQueue queue = Volley.newRequestQueue(getContext());
            StringRequest postRequest = new StringRequest(Request.Method.POST, SEVER.xoasanpham_url,
                    response -> {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String message = jsonResponse.getString("message");
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                            // Xóa sản phẩm khỏi danh sách và cập nhật giao diện
                            hoadonArrayList.remove(hoadon);
                            hoadonAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> Toast.makeText(getContext(), "Có lỗi xảy ra. Vui lòng thử lại.", Toast.LENGTH_SHORT).show()
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("tenSanPham", hoadon.tenHD);  // Truyền tên sản phẩm vào để xóa trên server
                    return params;
                }
            };
            queue.add(postRequest);
        }
    }


    // Hàm load sản phẩm từ giỏ hàng
    void loadsanpham() {
        hoadonArrayList.clear();
        String currentUsername = getCurrentUsername(); // Lấy tên đăng nhập từ SharedPreferences

        if (currentUsername == null) {
            Toast.makeText(getContext(), "Bạn cần đăng nhập để xem giỏ hàng.", Toast.LENGTH_SHORT).show();
            return; // Nếu không có tên đăng nhập, không tiếp tục
        }

        // Cập nhật URL với tên đăng nhập
        String urlWithUsername = SEVER.layhoadon_url + "?tendangnhap=" + currentUsername;

        Log.d("CartFragment", "Request URL: " + urlWithUsername); // Log URL để kiểm tra

        // Xử lý phản hồi thành công từ server
        Response.Listener<JSONArray> thanhcong = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("CartFragment", "Response received: " + response.toString()); // Log phản hồi JSON

                if (response.length() == 0) {
                    Toast.makeText(getContext(), "Giỏ hàng của bạn đang trống.", Toast.LENGTH_LONG).show();
                }
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Log.d("CartFragment", "Processing item: " + jsonObject.toString()); // Log từng mục

                        hoadonArrayList.add(0, new HOADON(
                                jsonObject.getString("Tennuoc"),
                                jsonObject.getString("Hinhnuoc"),
                                jsonObject.getString("Dongia"),
                                jsonObject.getString("Soluong")
                        ));
                    } catch (JSONException e) {
                        Log.e("CartFragment", "JSON parsing error: " + e.getMessage()); // Log lỗi JSON
                        Toast.makeText(getContext(), "Lỗi lấy dữ liệu: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                hoadonAdapter.notifyDataSetChanged(); // Cập nhật adapter
            }
        };

        // Xử lý khi có lỗi
        Response.ErrorListener thatbai = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("CartFragment", "Volley error: " + error.getMessage()); // Log lỗi mạng
                Toast.makeText(getContext(), "Thất bại: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        };

        // Gửi request với Volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(urlWithUsername, thanhcong, thatbai);
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest); // Thêm request vào hàng đợi
    }




    // Lấy tên đăng nhập hiện tại từ SharedPreferences
    private String getCurrentUsername() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null);
        Log.d("CartFragment", "Current username: " + username); // Log tên đăng nhập
        return username;
    }

    // Cập nhật tổng tiền
    @Override
    public void onTotalPriceUpdate(int totalPrice) {
        Log.d("CartFragment", "Total price updated: " + totalPrice + " VND"); // Log cập nhật tổng tiền
        // Sử dụng DecimalFormat để định dạng số với dấu chấm phần ngàn
        NumberFormat formatter = new DecimalFormat("#,###");
        String formattedPrice = formatter.format(totalPrice);

        tongTien.setText(formattedPrice + " VND"); // Hiển thị giá đã định dạng
    }




}