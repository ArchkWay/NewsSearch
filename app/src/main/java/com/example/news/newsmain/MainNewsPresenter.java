package com.example.news.newsmain;


import android.annotation.SuppressLint;
import android.content.Context;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;

import com.example.news.BaseApp;
import com.example.news.apiservice.pojos.Article;
import com.example.news.db.ArticleEntity;

import java.util.List;

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
        model.getNewsFromNet(keyApi, theme).subscribe(view::setNews);
    }

    @Override
    public void attachDbItems(MainNewsContract.View mvpView,FragmentActivity fragmentActivity, LifecycleOwner owner) {
        view = mvpView;
        List <Article> list = model.getDataFromDB(fragmentActivity, owner);
        view.setNewsFromDB(list);
    }

    @Override
    public void deleteAll(FragmentActivity fragmentActivity) {
        model.deleteAll(fragmentActivity);
    }

    @Override
    public void addNewArcticle(ArticleEntity articleEntity) {
        model.addNewArcticle(articleEntity);
    }

}
