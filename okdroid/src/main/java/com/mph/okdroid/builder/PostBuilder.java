package com.mph.okdroid.builder;

import android.text.TextUtils;

import com.mph.okdroid.OkDroid;
import com.mph.okdroid.callback.MyCallback;
import com.mph.okdroid.response.IResponseHandler;

import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by：hcs on 2017/4/21 15:01
 * e_mail：aaron1539@163.com
 */
public class PostBuilder extends BaseHasParamRequestBuilder<PostBuilder> {

    private String mJsonParams;

    public PostBuilder(OkDroid mOkDroid) {
        super(mOkDroid);
    }

    public PostBuilder jsonParams(String json){
        this.mJsonParams = json;
        return this;
    }

    @Override
    public void enqueue(final IResponseHandler responseHandler) {
        try {
            if(TextUtils.isEmpty(mUrl)){
                throw new IllegalArgumentException("url can not be null");
            }

            Request.Builder builder = new Request.Builder().url(mUrl);
            appendHeaders(builder,mHeaders);

            if(mTag!=null){
                builder.tag(mTag);
            }

            if(!TextUtils.isEmpty(mJsonParams) && mParams!=null){
                throw new RuntimeException("json params and form params cannot be set at the same time");
            }

            if(!TextUtils.isEmpty(mJsonParams)){
                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),mJsonParams);
                builder.post(body);
            }else if(mParams!=null){
                FormBody.Builder encodingBuilder = new FormBody.Builder();
                appendParams(encodingBuilder, mParams);
                builder.post(encodingBuilder.build());
            }else {
                //default is string request body
                RequestBody body = RequestBody.create(MediaType.parse("text/plain; charset=utf-8"),"");
                builder.post(body);
            }

            Request request = builder.build();
            mOkDroid.getOkHttpClient()
                    .newCall(request)
                    .enqueue(new MyCallback(responseHandler));
        } catch (final Exception e) {
            e.printStackTrace();
            OkDroid.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    responseHandler.onFailed(0, e.getMessage());
                }
            });
        }
    }

    private void appendParams(FormBody.Builder builder, Map<String, String> params) {
        if(mParams!=null && !params.isEmpty()){
            for (String key :
                    params.keySet()) {
                builder.add(key, params.get(key));
            }
        }
    }
}
