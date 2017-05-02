package com.mph.okhttp_okdroid;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mph.okdroid.OkDroid;
import com.mph.okdroid.response.IResponseDownloadHandler;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DownloadActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.tv_progress)
    TextView tvProgress;
    @BindView(R.id.progressbar)
    ProgressBar progressbar;
    @BindView(R.id.cancel)
    Button cancel;

    private OkDroid okDroid = MyApp.getInstance().getOkDroid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        ButterKnife.bind(this);

        okDroid.download().url("http://ivy.pconline.com.cn/click?adid=434690&id=pc.xz.android.zd.tl1.&__uuid=10220796")
                .tag(this)
                .filePath(Environment.getExternalStorageDirectory()+"/okdroid/kyw.apk")
                .enqueue(new IResponseDownloadHandler() {
                    @Override
                    public void onFinish(File downloadFile) {
                        Toast.makeText(DownloadActivity.this,"下载成功",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onProgress(long progress, long total) {
                        int rate = (int)(((float)progress/total)*100);
                        tvProgress.setText(rate+"%");
                        progressbar.setProgress(rate);
                    }

                    @Override
                    public void onFailed(String errMsg) {

                    }

                    @Override
                    public void onCancel() {
                        progressbar.setProgress(0);
                        tvProgress.setText("");
                        name.setText("正在下载");
                    }
                });

        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        okDroid.cancel(this);
    }
}
