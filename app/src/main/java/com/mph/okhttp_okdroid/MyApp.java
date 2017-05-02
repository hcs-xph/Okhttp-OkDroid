package com.mph.okhttp_okdroid;

import android.app.Application;

import com.mph.okdroid.OkDroid;

/**
 * Created by：hcs on 2017/5/2 10:10
 * e_mail：aaron1539@163.com
 */
public class MyApp extends Application {

    private static MyApp instance;
    private OkDroid okDroid;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        okDroid = new OkDroid();
        okDroid.setDebug(true);
    }

    public static synchronized MyApp getInstance(){
        return instance;
    }

    public OkDroid getOkDroid(){
        return okDroid;
    }
}
