package com.mph.okdroid.response;

import okhttp3.Response;

/**
 * Created by：hcs on 2017/4/21 13:49
 * e_mail：aaron1539@163.com
 */
public interface IResponseHandler {

    void onSuccess(Response response);

    void onFailed(int statusCode,String errMsg);

    void onProgress(long progress,long total);
}
