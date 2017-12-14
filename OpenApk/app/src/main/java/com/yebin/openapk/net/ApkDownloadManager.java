package com.yebin.openapk.net;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


import com.yebin.openapk.App;
import com.yebin.openapk.utils.PermissionUtil;
import com.yebin.openapk.utils.ResUtil;
import com.yebin.openapk.utils.SharedPrefUtil;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Qiao on 2017/3/21.
 */

public class ApkDownloadManager {
    public static final int DOWNLOAD_STATE_TO_DOWNLOAD = 101;
    public static final int DOWNLOAD_STATE_DOWNLOADING = 102;
    public static final int DOWNLOAD_STATE_DOWNLOADED = 103;
    private static final String DOWNLOAD_ID = "DOWNLOAD_ID";
    private static final String DOWNLOAD_VERSION = "DOWNLOAD_VERSION";
    private static final long DEFAULT_DOWNLOAD_ID = -1;
    public static final String APK_MIME_TYPE = "application/vnd.android.package-archive";

    public static void checkDownloadState(String versionName, DownloadStateListener listener) {
        if (!PermissionUtil.hasExternalStoragePermission()) return;
        if (listener == null) return;
        long downloadId = SharedPrefUtil.getLong(DOWNLOAD_ID, DEFAULT_DOWNLOAD_ID);
        String oldVersion = SharedPrefUtil.getString(DOWNLOAD_VERSION);
        if (downloadId == DEFAULT_DOWNLOAD_ID) {
            listener.downloadState(DOWNLOAD_STATE_TO_DOWNLOAD, null);
            return;
        }
        DownloadManager downloadManager = (DownloadManager) App.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Query query = new DownloadManager.Query();
        Cursor cursor = downloadManager.query(query.setFilterById(downloadId));
        if (cursor != null && cursor.moveToNext()) {
            int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            if (status == DownloadManager.STATUS_SUCCESSFUL) {
                String uri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                String path = Uri.parse(uri).getPath();
                File file = new File(path);
                if (file.exists()) {
                    if (TextUtils.equals(versionName, oldVersion)) {
                        listener.downloadState(DOWNLOAD_STATE_DOWNLOADED, path);
                    } else {
                        file.delete();
                        listener.downloadState(DOWNLOAD_STATE_TO_DOWNLOAD, null);
                    }
                } else {
                    listener.downloadState(DOWNLOAD_STATE_TO_DOWNLOAD, null);
                }
            } else if (status == DownloadManager.STATUS_RUNNING) {
                listener.downloadState(DOWNLOAD_STATE_DOWNLOADING, null);
            } else {
                listener.downloadState(DOWNLOAD_STATE_TO_DOWNLOAD, null);
            }
        } else {
            listener.downloadState(DOWNLOAD_STATE_TO_DOWNLOAD, null);
        }
    }

    public static void downloadApk(final Context context, String versionName, String apkUrl, final DownloadListener listener) {
        if (!PermissionUtil.hasExternalStoragePermission()) return;
        if (TextUtils.isEmpty(apkUrl)) return;
        if (!apkUrl.startsWith("http")) {
            Toast.makeText(context, "下载链接错误", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(context, "后台下载中...", Toast.LENGTH_SHORT).show();
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdir();
        final DownloadManager downloadManager = (DownloadManager) App.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(apkUrl));
        final String name = "视联网(疆)" + versionName + ".apk";
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), name);
        if (file.exists()) file.delete();
        final String apkPath = file.getAbsolutePath();
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, name);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle("视联网(疆)" + versionName);
        request.setDescription("正在下载...");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setMimeType(APK_MIME_TYPE);
        final long id = downloadManager.enqueue(request);
        if (listener == null) {
            SharedPrefUtil.putLong(DOWNLOAD_ID, id);
            SharedPrefUtil.putString(DOWNLOAD_VERSION, versionName);
            IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
            context.registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Log.d("onReceive: ", intent.getAction());
                    installApk(context, apkPath);
                    context.unregisterReceiver(this);
                }
            }, intentFilter);
        } else {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    Log.d("DownloadUtil", "timer running...");
                    DownloadManager.Query query = new DownloadManager.Query();
                    Cursor cursor = downloadManager.query(query.setFilterById(id));
                    if (cursor != null && cursor.moveToNext()) {
                        final int progress = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                        final int total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                        final boolean done = progress == total;
                        App.post(new Runnable() {
                            @Override
                            public void run() {
                                listener.onProgress(progress, total, done);
                            }
                        });
                        if (done) {
                            cancel();
                            Log.d("DownloadUtil", "timer done...");
                            installApk(context, apkPath);
                        }
                    }
                }
            }, 0, 1000);
        }
    }

    public static void installApk(Context context, String apkPath) {
        if (context == null) return;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            uri = FileProvider.getUriForFile(context, context.getPackageName(), new File(apkPath));//通过FileProvider创建一个content类型的Uri
        } else {
            uri = Uri.parse("file://" + apkPath);
        }
        intent.setDataAndType(uri, APK_MIME_TYPE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public interface DownloadStateListener {
        void downloadState(int state, String apkPath);
    }

    public interface DownloadListener {
        void onProgress(int progress, int total, boolean done);
    }
}
