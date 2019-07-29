package com.example.news.newsmain;


import android.annotation.SuppressLint;
import android.content.Context;
import com.example.news.BaseApp;

import javax.inject.Inject;


public class MainNewsPresenter implements MainNewsContract.presenter {
    MainNewsContract.view view;
    @Inject
    MainNewsContract.model model;
    @Inject
    public MainNewsPresenter(Context context) {
        BaseApp.get(context).getInjector().inject(this);
    }

    @SuppressLint("CheckResult")
    @Override
    public void attachView(MainNewsContract.view view, String keyApi, String theme) {
        this.view = view;
        model.getNews(keyApi, theme).subscribe(view::setNews);
    }

    @Override
    public void detachView() {
        this.view = null;
    }

}
