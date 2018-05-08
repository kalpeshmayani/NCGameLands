package com.example.ncgamelands.util;

import android.content.Context;
import android.content.Intent;

import com.example.ncgamelands.ui.activity.GPSTrackingActivity;
import com.example.ncgamelands.ui.activity.MapActivity;
import com.example.ncgamelands.ui.activity.RegulationActivity;

public class IntentUtil {

    public static final int CHOOSE_IMAGE_REQUEST_CODE = 101;

    public static void startMapActivity(Context context) {
        Intent intent = new Intent(context, MapActivity.class);
        context.startActivity(intent);
    }

    public static void startRegulationActivity(Context context) {
        Intent intent = new Intent(context, RegulationActivity.class);
        context.startActivity(intent);
    }

    public static void startGPSTrackingActivity(Context context) {
        Intent intent = new Intent(context, GPSTrackingActivity.class);
        context.startActivity(intent);
    }

}