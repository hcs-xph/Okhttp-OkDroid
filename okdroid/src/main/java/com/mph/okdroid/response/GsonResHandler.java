package com.mph.okdroid.response;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;
import com.mph.okdroid.OkDroid;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by：hcs on 2017/4/24 17:19
 * e_mail：aaron1539@163.com
 */
public abstract class GsonResHandler<T> implements IResponseHandler {

    private Type mType;

    public GsonResHandler() {
        Type superclass = getClass().getGenericSuperclass();//反射获取带泛型的class
        if(superclass instanceof Class){
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterizedType = (ParameterizedType) superclass;//获取所有泛型
        mType = $Gson$Types.canonicalize(parameterizedType.getActualTypeArguments()[0]);//将泛型转为type
    }

    private Type getType() {
        return mType;
    }

    @Override
    public final void onSuccess(final Response response) {
        final ResponseBody body = response.body();
        String resBodyStr = "";
        try {
            resBodyStr = body.string();
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
            body.close();
        }

        final String finalBodyStr = resBodyStr;
        try {
            OkDroid.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Gson gson = new Gson();
                    onSuccess(response.code(),(T)gson.fromJson(finalBodyStr,getType()));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            OkDroid.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    onFailed(response.code(),"fail parse gson, body=" + finalBodyStr);
                }
            });
        }
    }

    public abstract void onSuccess(int statusCode,T response);

    @Override
    public void onProgress(long progress, long total) {

    }
}
