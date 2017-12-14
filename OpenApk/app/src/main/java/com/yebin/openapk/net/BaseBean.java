package com.yebin.openapk.net;

import java.io.Serializable;

/**
 * Created by PVer on 2017/12/13.
 */

public class BaseBean<T> implements Serializable{
    public int code;
    public T data;
}
