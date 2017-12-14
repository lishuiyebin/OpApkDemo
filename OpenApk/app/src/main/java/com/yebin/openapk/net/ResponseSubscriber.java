package com.yebin.openapk.net;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;

/**
 * Created by PVer on 2017/12/13.
 */

public abstract class ResponseSubscriber<T> extends DisposableSingleObserver<T>{

    @Override
    public void onError(@NonNull Throwable e) {
        if(e instanceof ConnectException){
            onFailure("无法连接服务器");
        }else if(e instanceof SocketTimeoutException){
            onFailure("连接超时，请稍后重试");
        }else if(e instanceof ResponseException){
            onFailure(e.getMessage());
        }else {
            onFailure(e.getMessage());
        }
    }

    @Override
    public abstract void onSuccess(@NonNull T t);

    public abstract void onFailure(String msg);
}
