package com.yebin.openapk.net;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by PVer on 2017/12/13.
 */

public class ResponseFunc<T> implements Function<BaseBean<T>,T>{

    @Override
    public T apply(@NonNull BaseBean<T> baseBean) throws Exception {
        if(baseBean.code!=ResponseException.CODE_SUCCESS){
            throw new ResponseException(baseBean.code,"");
        }
        return baseBean.data;
    }
}
