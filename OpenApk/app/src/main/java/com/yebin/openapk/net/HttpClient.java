package com.yebin.openapk.net;

import android.util.Log;

import com.yebin.openapk.App;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by PVer on 2017/12/13.
 */

public class HttpClient {
    private static final String TAG = HttpClient.class.getSimpleName();
    private static final int HTTP_CACHE_SIZE = 5242880; //5 * 1024 * 1024
    private static final String HTTP_CACHE_DIR = "HttpCache";
    private static final int HTTP_CONNECT_TIMEOUT = 20000;
    private static final int HTTP_WRITE_TIMEOUT = 20000;
    private static final int HTTP_READ_TIMEOUT = 20000;
    private static final String BASEUSL="http://58.30.9.142:31570/";
    private Retrofit mJsonRetrofit;
    private Retrofit mStringRetrofit;
    private static HttpAPI mServerJsonAPI;
    private static HttpAPI mServerStringAPI;

    private HttpClient(boolean isJson){
        File file = new File(App.app.getApplicationContext().getCacheDir().getAbsolutePath(), HTTP_CACHE_DIR);
        Cache cache = new Cache(file, HTTP_CACHE_SIZE);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(getLogInterceptor())
                .connectTimeout(HTTP_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(HTTP_WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(HTTP_READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .cache(cache).build();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASEUSL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        if(isJson){
            builder.addConverterFactory(GsonConverterFactory.create());
            mJsonRetrofit=builder.build();
        }else {
            builder.addConverterFactory(ScalarsConverterFactory.create());
            mStringRetrofit=builder.build();
        }
    }

    private HttpLoggingInterceptor getLogInterceptor() {
        return new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.i(TAG,message);
            }
        }).setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    static HttpAPI creatJsonApi(Class<HttpAPI> clazz){
        if(mServerJsonAPI==null){
            mServerJsonAPI=new HttpClient(true).mJsonRetrofit.create(clazz);
        }
        return mServerJsonAPI;
    }

    static HttpAPI creatStringApi(Class<HttpAPI> clazz){
        if(mServerStringAPI==null){
            mServerStringAPI=new HttpClient(false).mStringRetrofit.create(clazz);
        }
        return mServerStringAPI;
    }



}
