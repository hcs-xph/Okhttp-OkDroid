package com.mph.okdroid.body;

import com.mph.okdroid.response.IResponseDownloadHandler;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by：hcs on 2017/4/24 16:05
 * e_mail：aaron1539@163.com
 */
public class ResProgressBody extends ResponseBody {

    private ResponseBody mResponseBody;
    private IResponseDownloadHandler mResponseDownloadHandler;
    private BufferedSource bufferedSource;

    public ResProgressBody(ResponseBody mResponseBody, IResponseDownloadHandler mResponseDownloadHandler) {
        this.mResponseBody = mResponseBody;
        this.mResponseDownloadHandler = mResponseDownloadHandler;
    }

    @Override
    public MediaType contentType() {
        return mResponseBody.contentType();
    }

    @Override
    public long contentLength() {
        return mResponseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if(bufferedSource == null){
            bufferedSource = Okio.buffer(source(mResponseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(BufferedSource source) {

        return new ForwardingSource(source) {

            long totalBytesRead;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                //这个进度是读取response每次内容的进度，在写文件之前，所以读取进度以写完文件的进度为准
                long bytesRead = super.read(sink,byteCount);
                totalBytesRead += (bytesRead!=-1)?bytesRead:0;
//                if(mResponseDownloadHandler!=null){
//                    mResponseDownloadHandler.onProgress(totalBytesRead,mResponseBody.contentLength());
//                }
                return totalBytesRead;
            }
        };
    }
}
