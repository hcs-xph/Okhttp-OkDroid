package com.mph.okhttp_okdroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.mph.okdroid.OkDroid;
import com.mph.okdroid.response.RawResHandler;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GetActivity extends AppCompatActivity {

    @BindView(R.id.content)
    TextView mTvContent;

    private OkDroid okDroid = MyApp.getInstance().getOkDroid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get);
        ButterKnife.bind(this);

        //http://192.168.1.109:8080/day10/servlet/ListBookServlet

        okDroid.get().url("请求地址")
                .tag(this)
                .enqueue(new RawResHandler() {
                    @Override
                    public void onSuccess(int statusCode, String response) {
                        Log.d(GetActivity.class.getSimpleName(),response);
                        if(!TextUtils.isEmpty(response)){
                            mTvContent.setText(response);
                        }
                    }

                    @Override
                    public void onFailed(int statusCode, String errMsg) {

                    }
                });
    }
}
