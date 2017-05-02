package com.mph.okdroid.callback;

import android.util.Log;

import com.mph.okdroid.OkDroid;
import com.mph.okdroid.response.IResponseHandler;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by：hcs on 2017/4/21 15:31
 * e_mail：aaron1539@163.com
 */
public class MyCallback implements Callback {

    private IResponseHandler mResponseHandler;

    public MyCallback(IResponseHandler mResponseHandler) {
        this.mResponseHandler = mResponseHandler;
    }

    @Override
    public void onFailure(final Call call, final IOException e) {
        if(OkDroid.isDebug){
            Log.e(OkDroid.debugTag,e.getMessage());
            e.printStackTrace();
        }
        OkDroid.mHandler.post(new Runnable() {
            @Override
            public void run() {
                mResponseHandler.onFailed(0,e.toString());
            }
        });
    }

    @Override
    public void onResponse(Call call, final Response response) throws IOException {
        if(OkDroid.isDebug){
            Log.i(OkDroid.debugTag,"response status:"+response.code()+",msg:"+response.message()+",response body:"+response.body());
        }
        if(response.isSuccessful()){
            mResponseHandler.onSuccess(response);
        }else{
            OkDroid.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mResponseHandler.onFailed(response.code(),"response msg"+response.message());
                }
            });
        }
    }
}
