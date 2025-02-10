package com.example.appdrinkfruit.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appdrinkfruit.Model.NUOC;
import com.example.appdrinkfruit.Model.NUOCGIAMGIA;
import com.example.appdrinkfruit.R;
import com.example.appdrinkfruit.SEVER;
import com.example.appdrinkfruit.Nuoc_Detail;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class GIAMGIA_ADAPTER extends RecyclerView.Adapter<NUOCGIAMGIAVIEWHOLDER> {

    Context context;
    ArrayList<NUOCGIAMGIA> nuocArrayList;

    public GIAMGIA_ADAPTER(Context context, ArrayList<NUOCGIAMGIA> nuocArrayList) {
        this.context = context;
        this.nuocArrayList = nuocArrayList;
    }

    @NonNull
    @Override
    public NUOCGIAMGIAVIEWHOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_nuocgiamgia,null);
        return new NUOCGIAMGIAVIEWHOLDER(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NUOCGIAMGIAVIEWHOLDER holder, int position) {
        NUOCGIAMGIA n = nuocArrayList.get(position);
        holder.title.setText(n.title);


        holder.favorite.setImageResource(n.isFavorite() ?
                R.drawable.baseline_favorite_24 : R.drawable.baseline_favorite_border_24);

        holder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thay đổi trạng thái yêu thích
                n.setFavorite(!n.isFavorite());

                // Cập nhật lại hình ảnh trái tim dựa trên trạng thái mới
                holder.favorite.setImageResource(n.isFavorite() ?
                        R.drawable.baseline_favorite_24 : R.drawable.baseline_favorite_border_24);
            }
        });


        double newprice = Double.parseDouble(n.newprice);
        NumberFormat formatter = new DecimalFormat("#,###");
        String formattedNewPrice = formatter.format(newprice);
        holder.newprice.setText(formattedNewPrice+"đ");

        double price = Double.parseDouble(n.price);
        NumberFormat formatterr = new DecimalFormat("#,###");
        String formattedPrice = formatterr.format(price);
        holder.price.setPaintFlags(holder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.price.setText(formattedPrice+"đ");

        String tennuoc = "";
        if(n.title.length() >15) {
            tennuoc = n.title.substring(0,15);
            holder.title.setText(tennuoc + "...");
        }


        String donggia = NumberFormat.getInstance(Locale.getDefault()).format(Double.parseDouble(n.price+""));
        //// Hinh se load tu Sever bang Picasso
        Picasso.get()
                .load(SEVER.hinhanh_url + n.img)
                .error(R.drawable.rmnc)  // Hình ảnh mặc định khi xảy ra lỗi
                .into(holder.imageView);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, Nuoc_Detail.class);
            intent.putExtra("nuocgiamgia", n);
            intent.putExtra("tennuoc",n.title);
            context.startActivity(intent);
        });

//        holder.itemView.startAnimation(AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.rotate));
    }


    @Override
    public int getItemCount() {
        return nuocArrayList.size();
    }
}

class NUOCGIAMGIAVIEWHOLDER extends RecyclerView.ViewHolder {
    ImageView imageView,favorite;
    TextView title;
    TextView price;
    TextView newprice;

    public NUOCGIAMGIAVIEWHOLDER(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.gg_img);
        title = itemView.findViewById(R.id.gg_title);
        price = itemView.findViewById(R.id.tvgiagoc);
        newprice = itemView.findViewById(R.id.tvgiamoi);
        favorite = itemView.findViewById(R.id.img_Favorite);
    }
}

