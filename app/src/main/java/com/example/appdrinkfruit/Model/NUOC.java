package com.example.appdrinkfruit.Model;

import java.io.Serializable;

public class NUOC implements Serializable {

    public String title,img,maNuoc,maLoai,price,description,quantity;
    private boolean isFavorite;
    private String id;

    public NUOC(String title, String img, String maNuoc, String maLoai, String price, String description, String quantity) {
        this.title = title;
        this.img = img;
        this.maNuoc = maNuoc;
        this.maLoai = maLoai;
        this.price = price;
        this.description = description;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
