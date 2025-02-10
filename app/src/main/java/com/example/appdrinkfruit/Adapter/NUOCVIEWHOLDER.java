package com.example.appdrinkfruit.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appdrinkfruit.R;

public class NUOCVIEWHOLDER extends RecyclerView.ViewHolder {
    ImageView imageView,imgFavorite;
    TextView title;
    TextView price;

    public NUOCVIEWHOLDER(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imgNuoc);
        imgFavorite = itemView.findViewById(R.id.imgFavorite);
        title = itemView.findViewById(R.id.tvtitle);
        price = itemView.findViewById(R.id.tvprice);
    }
}
