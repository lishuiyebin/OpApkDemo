package com.yebin.openapk.net;

import io.reactivex.Single;
import retrofit2.http.GET;

/**
 * Created by PVer on 2017/12/13.
 */

interface HttpAPI {
    @GET("yihee/up/getVersion.action")
    Single <BaseBean<VersionBean>> getVersion();
}
