package com.example.ncgamelands.util;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.ncgamelands.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by KP on 1/20/2018.
 */

public class AppUtil {

    public static boolean isOnLine() {
        ConnectivityManager connectivityManager = (ConnectivityManager) CommonsCore.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager != null && connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    /**
     * Method checks if the app is in background or not
     */
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    public static String getValue(String text) {
        return (text != null && !text.isEmpty() && !text.equalsIgnoreCase("null")) ? text : "";
    }

    public static Integer getValue(Integer text) {
        return (text != null && text != 0) ? text : 0;
    }

    public static Float getValue(Float text) {
        return (text != null && text != 0) ? text : Float.valueOf(0);
    }

    public static Double getValue(Double text) {
        return (text != null && text != 0) ? text : Double.valueOf(0);
    }

    public static void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) CommonsCore.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    // copy file from Assets to private folder
    public static void copyFromAssetsToDevice(Context context, ArrayList<String> srcList) {
        try {
            String dstPath = Environment.getExternalStorageDirectory().toString() + context.getResources().getString(R.string.download_file_path);
            AssetManager assetManager = context.getAssets();

            // create output directory if it doesn't exist
            File dir = new File(dstPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            for (String srcFilename : srcList) {

                InputStream in = assetManager.open(srcFilename);
                OutputStream out = new FileOutputStream(dstPath + "/" + srcFilename);

                byte[] buffer = new byte[1024];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                in.close();

                // write the output file
                out.flush();
                out.close();

                // re-scan media
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(dstPath + "/" + srcFilename))));
            }

            ToastUtil.showToast("File(s) downloaded to " + Environment.getExternalStorageDirectory().toString() + context.getResources().getString(R.string.download_file_path) + "/");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}