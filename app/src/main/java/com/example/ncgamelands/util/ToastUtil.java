package com.example.ncgamelands.util;


import android.widget.Toast;

public class ToastUtil {

    public static void showToast(String message) {
        Toast.makeText(CommonsCore.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(int messageResId) {
        Toast.makeText(CommonsCore.getContext(), messageResId, Toast.LENGTH_SHORT).show();
    }

    public static void shotLongToast(String message) {
        Toast.makeText(CommonsCore.getContext(), message, Toast.LENGTH_LONG).show();
    }

    public static void showLongToast(int messageResId) {
        Toast.makeText(CommonsCore.getContext(), messageResId, Toast.LENGTH_LONG).show();
    }

}