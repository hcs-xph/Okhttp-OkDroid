package com.mph.okhttp_okdroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mph.okdroid.OkDroid;
import com.mph.okdroid.response.JsonResHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UploadActivity extends AppCompatActivity {


    @BindView(R.id.progressbar)
    ProgressBar mProgressbar;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.tv_progress)
    TextView tvProgress;
    @BindView(R.id.cancel)
    Button mBtnCancel;
    private OkDroid okDroid = MyApp.getInstance().getOkDroid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        ButterKnife.bind(this);

        mProgressbar.setIndeterminate(false);
        mProgressbar.setMax(100);

        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                okDroid.cancel(UploadActivity.this);
                mProgressbar.setProgress(0);
                tvProgress.setText("");
                name.setText("上传已经取消");
            }
        });

        //http://192.168.1.221:9190/api/casuserroleapi/editUserInfo
        Map<String, String> map = new HashMap<>();
        map.put("address", "");
        map.put("gender", "");
        map.put("height", "42");
        map.put("weight", "21");
        map.put("realname", "fsd");
        map.put("waist", "43");
        map.put("userid", "285");
        File file = new File("/sdcard/images/20170308_131947.jpg");
        name.setText("开始上传");
        okDroid.upload().url("http://192.168.1.221:9190/api/casuserroleapi/editUserInfo")
                .tag(UploadActivity.this)
                .params(map)
                .addFile("avatarByte", file)
                .enqueue(new JsonResHandler() {

                    @Override
                    public void onProgress(long progress, long total) {
                        Log.d(UploadActivity.class.getSimpleName(), "当前进度-->" + progress + ",总大小-->" + total);
                        int rate = (int) (((float) progress / total) * 100);
                        tvProgress.setText(rate + "%");
                        mProgressbar.setProgress(rate);
                    }

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        try {
                            if (response.getInt("status") == 0) {
                                name.setText("上传完成");
                                Toast.makeText(UploadActivity.this, "上传完成", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d(UploadActivity.class.getSimpleName(), response.toString());
                    }

                    @Override
                    public void onFailed(int statusCode, String errMsg) {

                    }
                });
    }
}
