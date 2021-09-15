package com.example.gij.base;

import android.app.Application;
import android.content.Context;


/**
 * @author: 小杨同志
 * @date: 2021/8/13
 */
public class App extends Application {
    public static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        this.context=this;
    }
}
