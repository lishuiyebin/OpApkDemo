package com.yebin.openapk.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;


/**
 * Created by Qiao on 2016/12/21.
 */

public class PermissionUtil {
    public static final int REQUEST_CODE_EXTERNAL_STORAGE = 100;

    public static boolean hasLocationPermission() {
        return ActivityCompat.checkSelfPermission(ResUtil.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(ResUtil.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean hasCameraPermission() {
        return ActivityCompat.checkSelfPermission(ResUtil.getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean hasExternalStoragePermission() {
        return ActivityCompat.checkSelfPermission(ResUtil.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestLocationPermission(Activity activity, int requestCode) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, requestCode);
    }

    public static void requestCameraPermission(Activity activity, int requestCode) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, requestCode);
    }

    public static void requestExternalStoragePermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_EXTERNAL_STORAGE);
    }

    public static void requestRecordAudioPermission(Activity activity, int requestCode) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.RECORD_AUDIO}, requestCode);
    }
}
