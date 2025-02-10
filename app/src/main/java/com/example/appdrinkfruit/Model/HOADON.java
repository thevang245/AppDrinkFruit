package com.example.appdrinkfruit.Model;

import java.io.Serializable;

public class HOADON implements Serializable {
    public String tenHD;
    public String hinhHD;
    public String giaHD;
    public String soLuong;
    private boolean isSelected; // Thêm thuộc tính để đánh dấu sản phẩm đã chọn


    private boolean isChecked;

    // Constructor


    public HOADON(String tenHD, String hinhHD, String giaHD, String soLuong) {
        this.tenHD = tenHD;
        this.hinhHD = hinhHD;
        this.giaHD = giaHD;
        this.soLuong = soLuong;
        this.isChecked = false;
    }

    // Getters và setters cho các thuộc tính
    public String getTenHD() {
        return tenHD;
    }

    public void setTenHD(String tenHD) {
        this.tenHD = tenHD;
    }

    public String getHinhHD() {
        return hinhHD;
    }

    public void setHinhHD(String hinhHD) {
        this.hinhHD = hinhHD;
    }

    public String getGiaHD() {
        return giaHD;
    }

    public void setGiaHD(String giaHD) {
        this.giaHD = giaHD;
    }

    public String getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(String soLuong) {
        this.soLuong = soLuong;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
