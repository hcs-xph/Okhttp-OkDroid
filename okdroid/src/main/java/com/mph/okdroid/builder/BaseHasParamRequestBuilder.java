package com.mph.okdroid.builder;

import com.mph.okdroid.OkDroid;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by：hcs on 2017/4/21 14:07
 * e_mail：aaron1539@163.com
 */
public abstract class BaseHasParamRequestBuilder<T extends BaseHasParamRequestBuilder> extends BaseRequestBuilder<T> {

    protected Map<String,String> mParams;

    public BaseHasParamRequestBuilder(OkDroid mOkDroid) {
        super(mOkDroid);
    }

    /**
     * 设置请求参数
     * @param params
     * @return
     */
    public T params(Map<String,String> params){
        this.mParams = params;
        return (T) this;
    }

    /**
     * 添加单个参数
     * @param key
     * @param value
     * @return
     */
    public T addParam(String key,String value){
        if(this.mParams==null){
            this.mParams = new HashMap<>();
        }
        this.mParams.put(key,value);
        return (T) this;
    }
}
