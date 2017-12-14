package com.yebin.openapk.net;

/**
 * Created by PVer on 2017/12/13.
 */

public class ResponseException extends RuntimeException {
    public static final int CODE_SUCCESS = 0;
    public int code;
    public ResponseException(int code,String message){
        super(message);
        this.code=code;
    }
}
