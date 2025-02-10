package com.example.appdrinkfruit.Adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appdrinkfruit.Model.CACLOAINUOC;
import com.example.appdrinkfruit.Model.TENLOAI;
import com.example.appdrinkfruit.R;
import com.example.appdrinkfruit.SEVER;
import com.example.appdrinkfruit.Nuoctheoloai;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CACLOAINUOC_ADAPTER extends RecyclerView.Adapter<CACLOAINUOCVIEWHOLDER> {

    Context context;
    ArrayList<CACLOAINUOC> cacloainuocArrayList;

    ArrayList<TENLOAI> tenloaiArrayList;

    public CACLOAINUOC_ADAPTER(Context context, ArrayList<CACLOAINUOC> chudesachArrayList) {
        this.context = context;
        this.cacloainuocArrayList = chudesachArrayList;
    }

    @NonNull
    @Override
    public CACLOAINUOCVIEWHOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cacloai_layout,null);
        return new CACLOAINUOCVIEWHOLDER(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CACLOAINUOCVIEWHOLDER holder, int position) {
        CACLOAINUOC cln = cacloainuocArrayList.get(position);
        holder.textView.setText(cln.tenLoai);
        //// Hinh se load tu Sever bang Picasso
        Picasso.get()
                .load(SEVER.hinhanh_url+ cln.hinhLoai)
                .error(R.drawable.rmnc)  // Hình ảnh mặc định khi xảy ra lỗi
                .into(holder.imageView);
        holder.itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                         Intent intent = new Intent(context, Nuoctheoloai.class);
                         intent.putExtra("maloai",cln.maLoai);
                         intent.putExtra("tenloai",cln.tenLoai);
                         context.startActivity(intent);

                    }
                }
        );




}

    @Override
    public int getItemCount() {
        return cacloainuocArrayList.size();
    }
}

class CACLOAINUOCVIEWHOLDER extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView textView;
    public CACLOAINUOCVIEWHOLDER(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imghinhLoai);
        textView = itemView.findViewById(R.id.tvtenLoai);
    }
}
