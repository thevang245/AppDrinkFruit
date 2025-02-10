package com.example.appdrinkfruit.Model;

import java.io.Serializable;

public class NUOCGIAMGIA implements Serializable {

    public String title,img,maSach,maChude,price,description,quantity,newprice;
    private boolean isFavorite;
    public NUOCGIAMGIA(String title, String img, String maSach, String maChude, String price, String description, String quantity, String newprice) {
        this.title = title;
        this.img = img;
        this.maSach = maSach;
        this.maChude = maChude;
        this.price = price;
        this.description = description;
        this.quantity = quantity;
        this.newprice = newprice;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}