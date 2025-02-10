package com.example.appdrinkfruit.Model;

import java.io.Serializable;

public class CACLOAINUOC implements Serializable {
    public String maLoai,tenLoai,hinhLoai;

    public CACLOAINUOC(String tenLoai, String maLoai, String hinhLoai) {
        this.tenLoai = tenLoai;
        this.maLoai = maLoai;
        this.hinhLoai = hinhLoai;
    }
}
