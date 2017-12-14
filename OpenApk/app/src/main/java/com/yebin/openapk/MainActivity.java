package com.yebin.openapk;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.yebin.openapk.dialog.BaseDialog;
import com.yebin.openapk.dialog.CommonDialog;
import com.yebin.openapk.net.ApkDownloadManager;
import com.yebin.openapk.net.HttpRequst;
import com.yebin.openapk.net.ResponseSubscriber;
import com.yebin.openapk.net.VersionBean;
import com.yebin.openapk.utils.PermissionUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

public class MainActivity extends AppCompatActivity {
    private String visionHost="58.30.9.142:31570";
    private String mUpdateUrl="http://58.30.9.142:31570/yihee/up/getVersion.action";
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PermissionUtil.requestExternalStoragePermission(this);
    }

    public void open(View view) {

        //要调用另一个APP的activity所在的包名
        String packageName = "com.visionvera.jiang";
        //String packageName = "com.visionvera.zong";
        //要调用另一个APP的activity名字
        if(isAppInstalled(this,packageName)){
            String activity = "com.visionvera.zong.activity.SplashActivity";
            ComponentName component = new ComponentName(packageName, activity);
            Intent intent = new Intent();
            intent.setComponent(component);
            //intent.setFlags(101);
            //intent.putExtra("data", "call");
            startActivity(intent);
        }else {
            //Toast.makeText(this, packageName+"未安装", Toast.LENGTH_SHORT).show();
           /* new CommonDialog(this)
                    .setTitle("视联网"+"未安装")
                    .setMsg("需要下载吗？")
                    .setOnConfirmClickListener(new BaseDialog.OnConfirmClickListener() {
                        @Override
                        public void onConfirm() {
                            getVersion();
                        }
                    })
                    .show();*/
           getVersion();
        }
    }

    private void getVersion(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("加载中。。。");
        progressDialog.show();
        HttpRequst.getVersion(new ResponseSubscriber<VersionBean>() {
            @Override
            public void onSuccess(@NonNull final VersionBean bean) {
                if(bean!=null){
                    ApkDownloadManager.checkDownloadState(bean.andversion, new ApkDownloadManager.DownloadStateListener() {
                        @Override
                        public void downloadState(int state, final String apkPath) {
                            if (state == ApkDownloadManager.DOWNLOAD_STATE_TO_DOWNLOAD) {
                                new CommonDialog(MainActivity.this)
                                        .setTitle("视联网: " + bean.andversion)
                                        .setMsg("视联网未安装,需要下载吗？")
                                        .setConfirmText("下载")
                                        .setCanceledOnTouchOutside(false)
                                        .setOnConfirmClickListener(new BaseDialog.OnConfirmClickListener() {
                                            @Override
                                            public void onConfirm() {
                                                ApkDownloadManager.downloadApk(getApplicationContext(), bean.andversion, bean.andurl, null);
                                            }
                                        })
                                        .show();
                            } else if (state == ApkDownloadManager.DOWNLOAD_STATE_DOWNLOADED) {
                                new CommonDialog(MainActivity.this)
                                        .setTitle("视联网: " + bean.andversion)
                                        .setMsg("视联网未安装,需要安装吗？")
                                        .setConfirmText("安装")
                                        .setCanceledOnTouchOutside(false)
                                        .setOnConfirmClickListener(new BaseDialog.OnConfirmClickListener() {
                                            @Override
                                            public void onConfirm() {
                                                ApkDownloadManager.installApk(getApplication(), apkPath);
                                            }
                                        })
                                        .show();
                            } else {
                                ApkDownloadManager.downloadApk(getApplicationContext(), bean.andversion, bean.andurl, null);
                            }
                        }
                    });
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(String msg) {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }


    public boolean isAppInstalled(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> infos = packageManager.getInstalledPackages(0);
        ArrayList<String> pName = new ArrayList<>();
        if(infos!=null){
            for (int i = 0; i < infos.size(); i++) {
                String name = infos.get(i).packageName;
                pName.add(name);

            }
        }
        return pName.contains(packageName);
    }


}
