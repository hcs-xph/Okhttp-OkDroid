package com.mph.okdroid.builder;

import android.text.TextUtils;

import com.mph.okdroid.OkDroid;
import com.mph.okdroid.body.ResProgressBody;
import com.mph.okdroid.callback.MyDownloadCallback;
import com.mph.okdroid.response.IResponseDownloadHandler;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by：hcs on 2017/4/21 15:00
 * e_mail：aaron1539@163.com
 */
public class DownloadBuilder {

    private String mUrl;//请求地址
    private Object mTag;
    private Map<String,String> mHeaders;//请求头
    private OkDroid mOkDroid;

    private String mFileDir;
    private String mFileName;
    private String mFilePath;//文件路径(如果设置该字段，以上两个就无需设置)

    private long mCompleteBytes = 0l;//已经下载完成的字节数，用于断点续传

    public DownloadBuilder(OkDroid mOkDroid) {
        this.mOkDroid = mOkDroid;
    }

    public DownloadBuilder fileDir(String fileDir){
        this.mFileDir = fileDir;
        return this;
    }

    public DownloadBuilder fileName(String fileName){
        this.mFileName = fileName;
        return this;
    }

    public DownloadBuilder filePath(String filePath){
        this.mFilePath = filePath;
        return this;
    }

    public DownloadBuilder completeBytes(long completeBytes){
        if(completeBytes>0l){
            this.mCompleteBytes = completeBytes;
            addHeader("RANGE","bytes="+completeBytes+"-");
        }
        return this;
    }

    /**
     * 设置url
     * @param url
     * @return
     */
    public DownloadBuilder url(String url){
        this.mUrl = url;
        return this;
    }

    /**
     * 设置tag
     * @param tag
     * @return
     */
    public DownloadBuilder tag(Object tag){
        this.mTag = tag;
        return this;
    }

    /**
     * 添加头
     * @param headers
     * @return
     */
    public DownloadBuilder headers(Map<String,String> headers){
        this.mHeaders = headers;
        return this;
    }

    /**
     * 添加单个头
     * @param key
     * @param value
     * @return
     */
    public DownloadBuilder addHeader(String key,String value){
        if(this.mHeaders == null){
            this.mHeaders = new HashMap<>();
        }
        this.mHeaders.put(key,value);
        return this;
    }

    public Call enqueue(final IResponseDownloadHandler downloadHandler) {
        try {
            if(TextUtils.isEmpty(mUrl)){
                throw new IllegalArgumentException("url can not be null");
            }
            if(TextUtils.isEmpty(mFilePath)){
                if(TextUtils.isEmpty(mFileDir) || TextUtils.isEmpty(mFileName)){
                    throw new IllegalArgumentException("filepath can not be null");
                }else{
                    mFilePath = mFileDir+mFileName;
                }
            }
            checkFilePath(mFilePath,mCompleteBytes);
            Request.Builder builder = new Request.Builder().url(mUrl);
            appendHeaders(builder,mHeaders);
            if(mTag!=null){
                builder.tag(mTag);
            }
            Request request = builder.build();
            Call call = mOkDroid.getOkHttpClient().newBuilder()
                    .addNetworkInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Response response = chain.proceed(chain.request());
                            return response.newBuilder()
                                    .body(new ResProgressBody(response.body(), downloadHandler)).build();
                        }
                    }).build().newCall(request);
            call.enqueue(new MyDownloadCallback(downloadHandler,mFilePath,mCompleteBytes));
            return call;
        } catch (final Exception e) {
            e.printStackTrace();
            OkDroid.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    downloadHandler.onFailed(e.getMessage());
                }
            });
            return null;
        }
    }

    protected void appendHeaders(Request.Builder builder,Map<String,String> headers){
        if(headers == null || headers.isEmpty()){
            return ;
        }
        Headers.Builder headerBuilder = new Headers.Builder();
        for (String key :
                headers.keySet()) {
            headerBuilder.add(key,headers.get(key));
        }
        builder.headers(headerBuilder.build());
    }

    /**
     * 检测filepath是否有效
     * @param filePath
     * @param completeBytes
     */
    private void checkFilePath(String filePath, long completeBytes) throws Exception {
        File file = new File(filePath);
        if(file.exists()){
            return ;
        }
        if(completeBytes>0l){
            throw new Exception("断点续传文件："+filePath+"不存在");
        }
        if(filePath.endsWith(File.separator)){
            throw new Exception("创建文件："+filePath+"失败，目标文件不能为空目录");
        }
        if(!file.getParentFile().exists()){
            if(!file.getParentFile().mkdirs()){
                throw new Exception("创建目标文件所在目录失败");
            }
        }
    }
}
