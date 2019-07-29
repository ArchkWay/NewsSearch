package com.example.news;

import android.app.Application;
import android.content.Context;

import com.example.news.di.components.AppComponent;
import com.example.news.di.components.DaggerAppComponent;
import com.example.news.di.modules.MvpModule;


public class BaseApp extends Application {
    final static public String INTENT_TITLE = "intent_title";
    final static public String INTENT_DESC = "intent_desc";
    /*baseDI*/
    private AppComponent appComponent;

    public AppComponent getInjector() {
        if(appComponent == null){
            appComponent = DaggerAppComponent
                    .builder()
                    .mvpModule(new MvpModule(this))
                    .build();
        }
        return appComponent;
    }

    public static BaseApp get(Context ctx){
            return (BaseApp)ctx.getApplicationContext();
    }
}
