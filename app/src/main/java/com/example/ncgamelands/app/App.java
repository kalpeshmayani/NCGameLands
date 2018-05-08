package com.example.ncgamelands.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.example.ncgamelands.BuildConfig;
import com.example.ncgamelands.util.CommonsCore;
import com.example.ncgamelands.util.KLog;
import com.example.ncgamelands.util.RuntimeData;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            // do something for a debug build
            KLog.setEnabled(true);
        }
        CommonsCore.init(this);
        RuntimeData.init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}