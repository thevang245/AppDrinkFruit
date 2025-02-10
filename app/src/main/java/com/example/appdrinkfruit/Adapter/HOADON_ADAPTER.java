package com.example.appdrinkfruit.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.example.appdrinkfruit.Model.HOADON;
import com.example.appdrinkfruit.Model.NUOC;
import com.example.appdrinkfruit.Nuoc_Detail;
import com.example.appdrinkfruit.R;
import com.example.appdrinkfruit.SEVER;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HOADON_ADAPTER extends RecyclerView.Adapter<HOADONVIEWHOLDER> {

    Context context;
    ArrayList<HOADON> hoadonArrayList;


    int totalPrice;

    public OnTotalPriceUpdateListener listener;



    public HOADON_ADAPTER(Context context, ArrayList<HOADON> hoadonArrayList, OnTotalPriceUpdateListener listener) {
        this.context = context;
        this.hoadonArrayList = hoadonArrayList;
        this.listener = listener;
    }


    @NonNull
    @Override
    public HOADONVIEWHOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hoadon, parent, false);
        return new HOADONVIEWHOLDER(view);
    }


    @Override
    public void onBindViewHolder(@NonNull HOADONVIEWHOLDER holder, int position) {
        HOADON hoadon = hoadonArrayList.get(position);
        // Hiển thị dữ liệu
        holder.tenHD.setText(hoadon.tenHD);

        double newPriceValue = Double.parseDouble(hoadon.giaHD);
        NumberFormat formatter = new DecimalFormat("#,###");
        String formattedNewPrice = formatter.format(newPriceValue);
        holder.giaHD.setText(formattedNewPrice+"đ");

        holder.soLuong.setText(String.valueOf(hoadon.soLuong));
        Picasso.get()
                .load(SEVER.hinhanh_url + hoadon.hinhHD)
                .error(R.drawable.rmnc)
                .into(holder.hinhHD);




        // Sự kiện khi nhấn vào nút Trừ
        holder.btnTru.setOnClickListener(v -> {
            int soLuong = Integer.parseInt(hoadon.soLuong);  // Lấy số lượng hiện tại
            if (soLuong > 1) {
                int soLuongCu = soLuong;  // Lưu lại số lượng cũ trước khi trừ
                hoadon.soLuong = String.valueOf(soLuong - 1);  // Giảm số lượng đi 1
                holder.soLuong.setText(hoadon.soLuong);  // Cập nhật hiển thị số lượng mới
                notifyItemChanged(position);  // Thông báo cập nhật dữ liệu
                capNhatSoLuong(hoadon.tenHD, hoadon.soLuong);  // Cập nhật số lượng trên server

                // Nếu checkbox đã chọn, cập nhật lại tổng tiền dựa trên số lượng mới
                if (hoadon.isChecked()) {
                    double itemPrice = Double.parseDouble(hoadon.giaHD);  // Lấy giá của sản phẩm
                    totalPrice -= itemPrice * soLuongCu;  // Trừ tổng giá trị cũ
                    totalPrice += itemPrice * Integer.parseInt(hoadon.soLuong);  // Cộng tổng giá trị mới sau khi giảm số lượng
                    listener.onTotalPriceUpdate(totalPrice);  // Cập nhật tổng tiền hiển thị
                }
            } else {
                // Nếu số lượng giảm xuống 1 và người dùng bấm Trừ, xóa sản phẩm khỏi giỏ hàng
                hoadonArrayList.remove(position);
                notifyItemRemoved(position);  // Thông báo sản phẩm đã bị xóa
                notifyItemRangeChanged(position, hoadonArrayList.size());  // Cập nhật lại danh sách

                // Nếu checkbox đã chọn, trừ giá trị của sản phẩm khỏi tổng tiền
                if (hoadon.isChecked()) {
                    double itemPrice = Double.parseDouble(hoadon.giaHD);
                    totalPrice -= itemPrice * soLuong;  // Trừ tổng giá trị của sản phẩm trước khi xóa
                    listener.onTotalPriceUpdate(totalPrice);  // Cập nhật tổng tiền
                }

                // Gọi hàm xóa sản phẩm khỏi giỏ hàng
                xoaSanPhamKhoiGioHang(hoadon);
                Toast.makeText(context, "Đã xóa sản phẩm khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
            }
        });


        holder.btnCong.setOnClickListener(v -> {
            int soLuong = Integer.parseInt(hoadon.soLuong);  // Số lượng hiện tại
            hoadon.soLuong = String.valueOf(soLuong + 1);   // Tăng số lượng lên 1
            holder.soLuong.setText(hoadon.soLuong);         // Cập nhật hiển thị số lượng mới
            notifyItemChanged(position);                    // Thông báo cập nhật dữ liệu
            capNhatSoLuong(hoadon.tenHD, hoadon.soLuong);   // Cập nhật số lượng trên server

            // Nếu checkbox đã chọn, cập nhật lại tổng tiền
            if (hoadon.isChecked()) {
                double itemPrice = Double.parseDouble(hoadon.giaHD);  // Lấy giá của sản phẩm
                totalPrice += itemPrice;  // Cộng thêm giá của một sản phẩm (vì số lượng vừa tăng thêm 1)
                listener.onTotalPriceUpdate(totalPrice);  // Cập nhật tổng tiền hiển thị
                hoadon.setChecked(true);
            }
        });



        // Xử lý sự kiện checkbox chọn/bỏ chọn
//        holder.checkBox.setOnCheckedChangeListener(null); // Tạm thời bỏ lắng nghe để tránh việc cập nhật lại

// Thiết lập trạng thái của checkbox theo trạng thái của hoadon
        holder.checkBox.setChecked(hoadon.isChecked());

// Đặt lại OnCheckedChangeListener sau khi đã thiết lập trạng thái checkbox
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            double itemPrice = Double.parseDouble(hoadon.giaHD);
            int itemQuantity = Integer.parseInt(hoadon.soLuong); // Số lượng hiện tại của sản phẩm

            // Cập nhật trạng thái checkbox trong đối tượng HOADON
            hoadon.setChecked(isChecked);

            if (isChecked) {
                // Nếu checkbox được tích chọn, cộng tổng tiền dựa trên số lượng hiện tại
                totalPrice += itemPrice * itemQuantity;
            } else {
                // Nếu checkbox bị bỏ chọn, trừ tổng tiền dựa trên số lượng hiện tại
                totalPrice -= itemPrice * itemQuantity;
            }

            // Cập nhật tổng tiền lên CartFragment
            if (listener != null) {
                listener.onTotalPriceUpdate(totalPrice);
            }

            // Cập nhật lại trạng thái của checkbox trong đối tượng hoadon
            hoadon.setChecked(isChecked);
        });



    }






    private void xoaSanPhamKhoiGioHang(HOADON hoadon) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.POST, SEVER.xoasanpham_url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String message = jsonResponse.getString("message");
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Lỗi khi xử lý phản hồi", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(context, "Có lỗi xảy ra. Vui lòng thử lại.", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("tenSanPham", hoadon.tenHD);
                return params;
            }
        };
        queue.add(postRequest);
    }

    public interface OnTotalPriceUpdateListener {
        void onTotalPriceUpdate(int totalPrice);
    }



    @Override
    public int getItemCount() {
        return hoadonArrayList.size();
    }

    private void capNhatSoLuong(String tenSanPham, String soLuong) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.POST, SEVER.capnhat_url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String message = jsonResponse.getString("message");
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Lỗi khi xử lý phản hồi", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(context, "Có lỗi xảy ra. Vui lòng thử lại.", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("tenSanPham", tenSanPham);
                params.put("soLuong", soLuong);
                return params;
            }
        };
        queue.add(postRequest);
    }
}

class HOADONVIEWHOLDER extends RecyclerView.ViewHolder {

    ImageView hinhHD;
    TextView tenHD, giaHD, soLuong;
    ImageButton btnTru, btnCong;
    CheckBox checkBox;

    private boolean isChecked;

    public HOADONVIEWHOLDER(@NonNull View itemView) {
        super(itemView);
        tenHD = itemView.findViewById(R.id.hd_tensanpham);
        giaHD = itemView.findViewById(R.id.hd_gia);
        hinhHD = itemView.findViewById(R.id.hd_hinh);
        soLuong = itemView.findViewById(R.id.soluong);
        btnTru = itemView.findViewById(R.id.btn_tru);
        btnCong = itemView.findViewById(R.id.btn_cong);
        checkBox = itemView.findViewById(R.id.btn_checkbox); // Thêm CheckBox
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
