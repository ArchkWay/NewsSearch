package com.example.news.di.components;


import com.example.news.di.modules.MvpModule;
import com.example.news.newsmain.MainNewsModel;
import com.example.news.newsmain.MainNewsPresenter;
import com.example.news.newsmain.MainNewsVActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {MvpModule.class})
public interface AppComponent {
    void inject(MainNewsModel model);
    void inject(MainNewsPresenter presenter);
    void inject(MainNewsVActivity view);
}
