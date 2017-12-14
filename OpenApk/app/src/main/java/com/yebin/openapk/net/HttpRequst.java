package com.yebin.openapk.net;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by PVer on 2017/12/13.
 */

public class HttpRequst {

    private static HttpAPI getJsonInstance(){
        return HttpClient.creatJsonApi(HttpAPI.class);
    }

    private static HttpAPI getStringInstance(){
        return HttpClient.creatStringApi(HttpAPI.class);
    }

    private static <T> void subscribe(Single<BaseBean<T>> single,ResponseSubscriber<T> subscriber ){
        if(single==null){
            return;
        }

        single.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new ResponseFunc<T>())
                .subscribe(subscriber);
    }

    private static <T> void subscribeString(Single<T> single,ResponseSubscriber<T> subscriber ){
        if(single==null){
            return;
        }

        single.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public static void getVersion(ResponseSubscriber<VersionBean> subscriber){
        subscribe(getJsonInstance().getVersion(), subscriber);
    }
}
