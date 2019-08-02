package com.example.news.newsmain;


import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;

import com.example.news.BaseApp;

import javax.inject.Inject;


public class MainNewsPresenter implements MainNewsContract.Presenter {
    MainNewsContract.View view;
    @Inject
    MainNewsContract.Model model;
    @Inject
    public MainNewsPresenter(Context context) {
        BaseApp.get(context).getInjector().inject(this);
    }

    @SuppressLint("CheckResult")
    @Override
    public void attachView(MainNewsContract.View view, String keyApi, String theme) {
        this.view = view;
        model.getNews(keyApi, theme).subscribe(view::setNews);
    }


    @Override
    public void attachDB(MainNewsContract.View mvpView, FragmentActivity fragmentActivity, @NonNull LifecycleOwner owner) {
        this.view = mvpView;
        view.setNewsFromDB(model.getDataFromDB2(fragmentActivity, owner));
    }

    @Override
    public void detachView() {
        this.view = null;
    }

}
