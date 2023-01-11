package com.example.kunmingsdkdemo;

import android.app.Application;

import common.utils.AppInit;


public class MyApplication extends Application {

    /**
     * isDebug 是否为测试环境
     * appkey 你的appKey
     */
    @Override
    public void onCreate() {
        super.onCreate();
        AppInit.init(this, false, "9111184");

    }
}