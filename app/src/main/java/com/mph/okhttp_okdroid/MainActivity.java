package com.mph.okhttp_okdroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.get)
    Button mBtnGet;
    @BindView(R.id.post)
    Button mBtnPost;
    @BindView(R.id.upload)
    Button mBtnUpload;
    @BindView(R.id.download)
    Button mBtnDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mBtnGet.setOnClickListener(this);
        mBtnPost.setOnClickListener(this);
        mBtnUpload.setOnClickListener(this);
        mBtnDownload.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.get:
                intent.setClass(this,GetActivity.class);
                break;
            case R.id.post:
                intent.setClass(this,PostActivity.class);
                break;
            case R.id.upload:
                intent.setClass(this,UploadActivity.class);
                break;
            case R.id.download:
                intent.setClass(this,DownloadActivity.class);
                break;
        }
        startActivity(intent);
    }
}
