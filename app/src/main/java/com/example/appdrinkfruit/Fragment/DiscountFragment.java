package com.example.appdrinkfruit.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.appdrinkfruit.Adapter.CACLOAINUOC_ADAPTER;
import com.example.appdrinkfruit.Adapter.GIAMGIA_ADAPTER;
import com.example.appdrinkfruit.Adapter.NUOC_ADAPTER;
import com.example.appdrinkfruit.Model.CACLOAINUOC;
import com.example.appdrinkfruit.Model.NUOC;
import com.example.appdrinkfruit.Model.NUOCGIAMGIA;
import com.example.appdrinkfruit.R;
import com.example.appdrinkfruit.SEVER;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DiscountFragment extends Fragment {

    ArrayList<NUOCGIAMGIA> nuocgiamgiaArrayList = new ArrayList<>();
    GIAMGIA_ADAPTER giamgiaAdapter;
    RecyclerView rvNuocgiamgia;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Sử dụng container thay vì null
        return inflater.inflate(R.layout.activity_discount_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rvNuocgiamgia = view.findViewById(R.id.rvnuocGG);

        // Kiểm tra RecyclerView có null không
        if (rvNuocgiamgia == null) {
            Log.e("DiscountFragment", "RecyclerView rvnuocGG không tồn tại trong giao diện.");
            return;
        }

        giamgiaAdapter = new GIAMGIA_ADAPTER(getActivity(), nuocgiamgiaArrayList);
        rvNuocgiamgia.setAdapter(giamgiaAdapter);
        rvNuocgiamgia.setLayoutManager(new GridLayoutManager(getContext(), 2));

        loadNuoc();
    }

    void loadNuoc() {
        Response.Listener<JSONArray> thanhcong = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                nuocgiamgiaArrayList.clear();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        nuocgiamgiaArrayList.add(new NUOCGIAMGIA(
                                jsonObject.optString("Tennuoc", ""),
                                jsonObject.optString("Hinhminhhoa", ""),
                                jsonObject.optString("Manuoc", ""),
                                jsonObject.optString("Maloai", ""),
                                jsonObject.optString("Dongia", ""),
                                jsonObject.optString("Mota", ""),
                                jsonObject.optString("Soluongban", ""),
                                jsonObject.optString("Dongiamoi", "")
                        ));
                    } catch (JSONException e) {
                        Log.e("DiscountFragment", "Lỗi đọc dữ liệu JSON: " + e.getMessage());
                    }
                }
                giamgiaAdapter.notifyDataSetChanged();
            }
        };
        Response.ErrorListener thatbai = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("DiscountFragment", "Lỗi kết nối: " + error.getMessage());
                Toast.makeText(getContext(), "Không thể tải dữ liệu: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        };
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(SEVER.laynuocgiamgia, thanhcong, thatbai);
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);
    }
}
