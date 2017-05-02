package com.mph.okdroid;

import android.os.Handler;
import android.os.Looper;

import com.mph.okdroid.builder.DownloadBuilder;
import com.mph.okdroid.builder.GetBuilder;
import com.mph.okdroid.builder.PostBuilder;
import com.mph.okdroid.builder.UploadBuilder;

import okhttp3.Call;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;

/**
 * Created by：hcs on 2017/4/21 13:36
 * e_mail：aaron1539@163.com
 */
public class OkDroid {
    public static Handler mHandler = new Handler(Looper.getMainLooper());
    private static OkHttpClient mOkHttpClient;
    public static boolean isDebug = false;
    public static final String debugTag = "OkDroid";

    public OkDroid(){
        this(null);
    }

    public OkDroid(OkHttpClient okHttpClient){
        if(this.mOkHttpClient == null){
            synchronized (OkDroid.class){
                if(this.mOkHttpClient == null){
                    if(okHttpClient == null){
                        this.mOkHttpClient = new OkHttpClient();
                    }else{
                        this.mOkHttpClient = okHttpClient;
                    }
                }
            }
        }
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    /**
     * 是否开启log,默认不开启
     * @param debug
     */
    public void setDebug(boolean debug) {
        isDebug = debug;
    }

    public GetBuilder get(){
        return new GetBuilder(this);
    }

    public PostBuilder post(){
        return new PostBuilder(this);
    }

    public UploadBuilder upload(){
        return new UploadBuilder(this);
    }

    public DownloadBuilder download(){
        return new DownloadBuilder(this);
    }

    /**
     * 根据tag取消请求
     * @param tag
     */
    public void cancel(Object tag){
        Dispatcher dispatcher = mOkHttpClient.dispatcher();
        for (Call call : dispatcher.queuedCalls()) {
            if(tag.equals(call.request().tag())){
                call.cancel();
            }
        }

        for (Call call : dispatcher.runningCalls()){
            if(tag.equals(call.request().tag())){
                call.cancel();
            }
        }
    }
}
