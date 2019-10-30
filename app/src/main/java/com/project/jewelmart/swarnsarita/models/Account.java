package com.project.jewelmart.swarnsarita.models;

import android.graphics.drawable.Drawable;

public class Account {

    public int image;
    public Drawable imageDrw;
    public String name;
    public String email;
    public boolean section = false;

    public Account() {
    }

    public Account(String name, boolean section) {
        this.name = name;
        this.section = section;
    }

}
