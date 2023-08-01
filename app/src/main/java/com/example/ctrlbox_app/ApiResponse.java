package com.example.ctrlbox_app;

import android.graphics.Picture;

import java.util.ArrayList;
import java.util.List;

public class ApiResponse {

    private String item;
    private PictureModel picture;

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public PictureModel getPicture() {
        return picture;
    }

    public void setPicture(PictureModel picture) {
        this.picture = picture;
    }
}

