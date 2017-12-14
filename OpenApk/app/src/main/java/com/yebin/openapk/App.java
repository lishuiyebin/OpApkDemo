package com.yebin.openapk;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.yebin.openapk.utils.ResUtil;

/**
 * Created by PVer on 2017/12/13.
 */

public class App extends Application {
    public static App app;
    private Handler mHandler = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();
        app=this;
        ResUtil.init(getApplicationContext());
    }

    public static void post(Runnable r) {
        getInstance().mHandler.post(r);
    }

    public static App getInstance() {
        return app;
    }

    public static Context getContext() {
        return app.getApplicationContext();
    }

}
