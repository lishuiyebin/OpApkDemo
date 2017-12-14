package com.yebin.openapk.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

/**
 * Created by Qiao on 2016/12/16.
 */

public class ResUtil {
    public static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    public static Resources getResources() {
        return getContext().getResources();
    }

    public static String getString(int resId) {
        return getResources().getString(resId);
    }

    public static int getColor(int resId) {
        return getResources().getColor(resId);
    }

    public static ColorStateList getColorStateList(int resId) {
        return getResources().getColorStateList(resId);
    }

    public static float getDimen(int resId) {
        return getResources().getDimension(resId);
    }

    public static Drawable getDrawable(int resId) {
        return getResources().getDrawable(resId);
    }

    public static Context getContext() {
        return mContext.getApplicationContext();
    }
}
