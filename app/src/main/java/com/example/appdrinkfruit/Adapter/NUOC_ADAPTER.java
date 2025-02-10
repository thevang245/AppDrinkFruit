package com.example.appdrinkfruit.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appdrinkfruit.Model.NUOC;
import com.example.appdrinkfruit.R;
import com.example.appdrinkfruit.SEVER;
import com.example.appdrinkfruit.Nuoc_Detail;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class NUOC_ADAPTER extends RecyclerView.Adapter<NUOCVIEWHOLDER> {

    Context context;
    ArrayList<NUOC> nuocArrayList;

    public NUOC_ADAPTER(Context context, ArrayList<NUOC> sachArrayList) {
        this.context = context;
        this.nuocArrayList = sachArrayList;
    }

    @NonNull
    @Override
    public NUOCVIEWHOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_nuoc_layout,null);
        return new NUOCVIEWHOLDER(view);
    }



    @Override
    public void onBindViewHolder(@NonNull NUOCVIEWHOLDER holder, int position) {
        NUOC n = nuocArrayList.get(position);
        holder.title.setText(n.title);



        String tennuoc = "";
        if(n.title.length() >15) {
            tennuoc = n.title.substring(0,15);
            holder.title.setText(tennuoc + "...");
        }


        String donggia = NumberFormat.getInstance(Locale.getDefault()).format(Double.parseDouble(n.price+""));
        holder.price.setText(n.price);
        double newprice = Double.parseDouble(n.price);
        NumberFormat formatter = new DecimalFormat("#,###");
        String formattedPrice = formatter.format(newprice);
        holder.price.setText(formattedPrice+" đ");


        // Hiển thị trạng thái yêu thích ban đầu
        holder.imgFavorite.setImageResource(n.isFavorite() ?
                R.drawable.baseline_favorite_24 : R.drawable.baseline_favorite_border_24);

        holder.imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                n.setFavorite(!n.isFavorite());

                // Cập nhật lại hình ảnh trái tim
                holder.imgFavorite.setImageResource(n.isFavorite() ?
                        R.drawable.baseline_favorite_24 : R.drawable.baseline_favorite_border_24);

                // Lưu trạng thái yêu thích vào SharedPreferences
               themsanphamyeuthich(n);
            }
        });





        //// Hinh se load tu Sever bang Picasso
        Picasso.get()
                .load(SEVER.hinhanh_url + n.img)
                .error(R.drawable.rmnc)  // Hình ảnh mặc định khi xảy ra lỗi
                .into(holder.imageView);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, Nuoc_Detail.class);
            intent.putExtra("nuoc", n);
            intent.putExtra("tennuoc",n.title);
            context.startActivity(intent);
        });

//        holder.itemView.startAnimation(AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.rotate));
    }

    private void themsanphamyeuthich(NUOC n) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Favorites", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(n.getId(), n.isFavorite()); // `getId` là mã định danh duy nhất của sản phẩm
        editor.apply();
    }


        @Override
    public int getItemCount() {
        return nuocArrayList.size();
    }
}
