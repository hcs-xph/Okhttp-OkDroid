package com.mph.okdroid.response;

import java.io.File;

/**
 * Created by：hcs on 2017/4/21 15:54
 * e_mail：aaron1539@163.com
 */
public abstract class IResponseDownloadHandler {
    public abstract void onFinish(File downloadFile);
    public abstract void onProgress(long progress,long total);
    public abstract void onFailed(String errMsg);

    public void onStart(long total){

    }
    public void onCancel(){

    }
}
