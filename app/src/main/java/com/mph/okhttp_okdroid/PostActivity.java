package com.mph.okhttp_okdroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.TextView;

import com.mph.okdroid.OkDroid;
import com.mph.okdroid.response.JsonResHandler;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostActivity extends AppCompatActivity {

    @BindView(R.id.content)
    TextView mTvContent;
    private OkDroid okDroid = MyApp.getInstance().getOkDroid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ButterKnife.bind(this);


        //http://192.168.1.221:9190/api/agency/newGetAgency
        okDroid.post().url("请求地址")
                .tag(this)
                .addParam("type","1")
                .enqueue(new JsonResHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        if(response!=null){
                            if(!TextUtils.isEmpty(response.toString())){
                                mTvContent.setText(response.toString());
                            }
                        }
                    }

                    @Override
                    public void onFailed(int statusCode, String errMsg) {

                    }
                });


    }
}
