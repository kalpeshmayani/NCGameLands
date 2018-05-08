package com.example.ncgamelands.util;

import android.content.Context;

import com.example.ncgamelands.model.CustomInfo;

public class RuntimeData {

    private Context context;
    private static RuntimeData instance;

    private CustomInfo item;

    private RuntimeData(Context context) {
        this.context = context;
    }

    public static RuntimeData getInstance() {
        return instance;
    }

    public static void init(Context context) {
        instance = new RuntimeData(context.getApplicationContext());
    }

    public CustomInfo getItem() {
        return item;
    }

    public void setItem(CustomInfo item) {
        this.item = item;
    }

}