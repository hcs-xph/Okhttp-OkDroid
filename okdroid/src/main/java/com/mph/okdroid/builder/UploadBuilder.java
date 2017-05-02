package com.mph.okdroid.builder;

import android.text.TextUtils;

import com.mph.okdroid.OkDroid;
import com.mph.okdroid.body.ReqProgressBody;
import com.mph.okdroid.callback.MyCallback;
import com.mph.okdroid.response.IResponseHandler;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by：hcs on 2017/4/21 15:01
 * e_mail：aaron1539@163.com
 */
public class UploadBuilder extends BaseHasParamRequestBuilder<UploadBuilder>{

    private Map<String,File> mFiles;
    private List<MultipartBody.Part> mExtraParts;

    public UploadBuilder(OkDroid mOkDroid) {
        super(mOkDroid);
    }

    public UploadBuilder files(Map<String,File> files){
        this.mFiles = files;
        return this;
    }

    public UploadBuilder addFile(String key,File file){
        if(this.mFiles == null){
            mFiles = new HashMap<>();
        }
        mFiles.put(key,file);
        return this;
    }

    public UploadBuilder addFile(String key,String fileName,byte[] fileContent){
        if(this.mExtraParts == null){
            this.mExtraParts = new ArrayList<>();
        }
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"),fileContent);
        this.mExtraParts.add(MultipartBody.Part.create(Headers.of("Content-Disposition",
                "form-data; name=\""+key+"\"; filename=\""+fileName+"\""),
                fileBody));
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

            MultipartBody.Builder multipartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            appendParams(multipartBuilder,mParams);// add params
            appendFiles(multipartBuilder,mFiles);
            appendParts(multipartBuilder,mExtraParts);

            builder.post(new ReqProgressBody(multipartBuilder.build(),responseHandler));
            Request request = builder.build();
            mOkDroid.getOkHttpClient().newCall(request).enqueue(new MyCallback(responseHandler));
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

    private void appendParts(MultipartBody.Builder builder, List<MultipartBody.Part> parts) {
        if(parts!=null && parts.size()>0){
            for (int i = 0,size = parts.size(); i < size; i++) {
                builder.addPart(parts.get(i));
            }
        }
    }

    private void appendFiles(MultipartBody.Builder builder, Map<String, File> files) {
        if(files!=null && !files.isEmpty()){
            RequestBody fileBody;
            File file = null;
            String fileName = null;
            for (String key :
                    files.keySet()) {
                file = files.get(key);
                if(file!=null){
                    fileName = file.getName();
                    fileBody = RequestBody.create(MediaType.parse(getMimeType(fileName)),file);
                    builder.addPart(Headers.of("Content-Disposition",
                            "form-data; name=\""+key+"\"; filename=\""+fileName+"\""),
                            fileBody);
                }
            }
        }
    }

    private String getMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentType = fileNameMap.getContentTypeFor(path);
        if(contentType == null){
            contentType = "application/octet-stream";
        }
        return contentType;
    }

    private void appendParams(MultipartBody.Builder builder, Map<String, String> params) {
        if(params!=null && !params.isEmpty()){
            for (String key :
                    params.keySet()) {
                if (params.get(key)!=null) {
                    builder.addPart(Headers.of("Content-Disposition","form-data; name=\""+key+"\""),
                            RequestBody.create(null,params.get(key)));
                }
            }
        }
    }
}
