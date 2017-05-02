package com.mph.okdroid.response;

import com.mph.okdroid.OkDroid;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by：hcs on 2017/4/24 16:45
 * e_mail：aaron1539@163.com
 */
public abstract class JsonResHandler implements IResponseHandler {
    @Override
    public final void onSuccess(final Response response) {
        ResponseBody responseBody = response.body();
        String responseBodyStr = "" ;
        try {
            responseBodyStr = responseBody.string();
        } catch (IOException e) {
            e.printStackTrace();
            OkDroid.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    onFailed(response.code(),"read response body failed");
                }
            });
            return ;
        }finally {
            responseBody.close();
        }

        final String finalBodyStr = responseBodyStr;
        try {
            final Object result = new JSONTokener(finalBodyStr).nextValue();
            if(result instanceof JSONObject){
                OkDroid.mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        onSuccess(response.code(),(JSONObject) result);
                    }
                });
            }else if(result instanceof JSONArray){
                OkDroid.mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        onSuccess(response.code(),(JSONArray) result);
                    }
                });
            }else{
                OkDroid.mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        onFailed(response.code(),"failed parse jsonobject,body="+finalBodyStr);
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
            OkDroid.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    onFailed(response.code(),"failed parse jsonobject,body="+finalBodyStr);
                }
            });
        }


    }

    public void onSuccess(int statusCode, JSONObject response){

    }

    public void onSuccess(int statusCode, JSONArray response){

    }

    @Override
    public void onProgress(long progress, long total) {

    }
}
