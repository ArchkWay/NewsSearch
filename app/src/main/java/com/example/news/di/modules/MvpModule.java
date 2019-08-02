package com.example.news.di.modules;

import android.content.Context;


import com.example.news.apiservice.RetrofitProvider;
import com.example.news.newsmain.MainNewsContract;
import com.example.news.newsmain.MainNewsModel;
import com.example.news.newsmain.MainNewsPresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MvpModule {
    /*modules injectors*/
    private final Context context;
    public MvpModule(Context context){
        this.context = context;
    }

    @Provides
    @Singleton
    Context provideContext(){
        return context;
    }

    @Provides
    @Singleton
    RetrofitProvider provideRetrofit(){
        return new RetrofitProvider();
    }

    @Provides
    @Singleton
    MainNewsContract.Presenter provideMainNewsContractPresenter(Context context){
        return new MainNewsPresenter( context);
    }

    @Provides
    @Singleton
    MainNewsContract.Model provideMainNewsContractModel(Context context){
        return new MainNewsModel(context);
    }

}
