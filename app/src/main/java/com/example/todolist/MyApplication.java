package com.example.todolist;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    private static MyApplication instance;

    public Context getContext (){
        return this.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static MyApplication getInstance() {
        return instance;
    }
}
