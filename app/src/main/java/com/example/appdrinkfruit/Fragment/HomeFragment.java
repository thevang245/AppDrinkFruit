package com.example.appdrinkfruit.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appdrinkfruit.Adapter.CACLOAINUOC_ADAPTER;
import com.example.appdrinkfruit.Adapter.NUOC_ADAPTER;
import com.example.appdrinkfruit.Model.CACLOAINUOC;
import com.example.appdrinkfruit.Model.NUOC;
import com.example.appdrinkfruit.R;
import com.example.appdrinkfruit.SEVER;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    ViewFlipper viewFlipper;
    RecyclerView rvLoainuoc;
    ArrayList<CACLOAINUOC> cacloainuocArrayList = new ArrayList<>();
    CACLOAINUOC_ADAPTER clnAdapter;

    ArrayList<NUOC> nuocArrayList = new ArrayList<>();
    NUOC_ADAPTER nuocAdapter;
    RecyclerView rvNuoc;

    List<NUOC> mangphu;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_layout_home,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        viewFlipper = view.findViewById(R.id.vFlipper);
        rvLoainuoc = view.findViewById(R.id.rvLoainuoc);
        rvNuoc = view.findViewById(R.id.rvNuoc);



        clnAdapter = new CACLOAINUOC_ADAPTER(getActivity(),cacloainuocArrayList);
        rvLoainuoc.setAdapter(clnAdapter);
        rvLoainuoc.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,true));

        nuocAdapter = new NUOC_ADAPTER(getActivity(),nuocArrayList);
        rvNuoc.setAdapter(nuocAdapter);
        rvNuoc.setLayoutManager(new GridLayoutManager(getContext(),2));

        loadLoainuoc();

        loadNuoc();

        loadViewFlipper();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // nap du  lieu len chudearraylist
//        sachArrayList.clear();
//        chudesachArrayList.clear();



    }


//    public void search(String query) {
//
//        List<NUOC> filteredList = new ArrayList<>();
//        if (query.isEmpty()) {
//            filteredList.addAll(mangphu);  // Nếu từ khóa trống, hiển thị lại toàn bộ danh sách
//        } else {
//            for (NUOC sach : mangphu) {
//                if (sach.getTitle().toLowerCase().contains(query.toLowerCase())) {
//                    filteredList.add(sach);  // Chỉ thêm sản phẩm nào chứa từ khóa vào danh sách
//                }
//            }
//        }
//        nuocArrayList.clear();
//        nuocArrayList.addAll(filteredList);
//        nuocAdapter.notifyDataSetChanged();  // Cập nhật lại giao diện hiển thị
//    }


    void loadLoainuoc() {
        cacloainuocArrayList.clear();
        Response.Listener<JSONArray> thanhcong = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i= 0 ;i< response.length();i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        cacloainuocArrayList.add(0,new CACLOAINUOC(
                                jsonObject.getString("Tenloai"),
                                jsonObject.getString("Maloai"),
                                jsonObject.getString("Hinhloai")
                        ));
                    } catch (JSONException e) {
                        Toast.makeText(getContext(), "Loi lay du lieu" + e.getMessage(), Toast.LENGTH_LONG).show();
                        throw new RuntimeException(e);
                    }
                }
                clnAdapter.notifyDataSetChanged();
            }
        };
        Response.ErrorListener thatbai = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"That bai"+error.getMessage(),Toast.LENGTH_LONG).show();
                Log.i("TAG", "onErrorResponse: " + error.getMessage());
            }
        };
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(SEVER.layloainuoc_url,thanhcong,thatbai);
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);
    }



    void loadNuoc() {
        Response.Listener<JSONArray> thanhcong = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                nuocArrayList.clear();
                for (int i=0;i< response.length();i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        nuocArrayList.add(new NUOC(
                                jsonObject.getString("Tennuoc"),
                                jsonObject.getString("Hinhminhhoa"),
                                jsonObject.getString("Manuoc"),
                                jsonObject.getString("Maloai"),
                                jsonObject.getString("Dongia"),
                                jsonObject.getString("Mota"),
                                jsonObject.getString("Soluongban")

                        ));
                    } catch (JSONException e) {
                        Toast.makeText(getContext(), "Loi lay du lieu trang homegragment" + e.getMessage(), Toast.LENGTH_LONG).show();
                        throw new RuntimeException(e);
                    }
                }
                mangphu = new ArrayList<>(nuocArrayList);
                nuocAdapter.notifyDataSetChanged();
            }
        };
        Response.ErrorListener thatbai = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"That bai"+error.getMessage(),Toast.LENGTH_LONG).show();
                Log.i("TAG", "onErrorResponse: " + error.getMessage());
            }
        };
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(SEVER.laynuoc_url,thanhcong,thatbai);
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);
    }

    void loadViewFlipper() {

        Response.Listener thanhcong = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String[] arrayfile = response.split("-");
                for ( String filename : arrayfile) {
                    ImageView imageView = new ImageView(getContext());
                    Picasso.get().load(SEVER.banner_url + filename).into(imageView);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    viewFlipper.addView(imageView);

                }
            }
        };
        Response.ErrorListener thatbai = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"That bai"+error.getMessage(),Toast.LENGTH_LONG).show();
                Log.e("TAG", "onErrorResponse: " + error.getMessage());
            }
        };

        StringRequest stringRequest = new StringRequest(SEVER.laybanner_url,thanhcong,thatbai);
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

        viewFlipper.setFlipInterval(2000);  // Thời gian lật ảnh (3 giây)
        viewFlipper.setAutoStart(true);


    }


}
