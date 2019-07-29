package com.example.news.newsmain;


import android.content.Context;
import android.util.Log;

import com.example.news.BaseApp;
import com.example.news.apiservice.Api;
import com.example.news.apiservice.RetrofitProvider;
import com.example.news.apiservice.pojos.Article;
import com.example.news.apiservice.pojos.NewsWrapper;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class MainNewsModel implements MainNewsContract.model{
    private final Api api;
    @Inject
    RetrofitProvider provider;

    public MainNewsModel(Context context){
        BaseApp.get(context).getInjector().inject(this);
        api = provider.getApi();
    }

    @Override
    public Observable<NewsWrapper> getNews(String apiKey, String theme) {
        NewsWrapper emptyWrapper = new NewsWrapper();
        List<Article> emptyList = new ArrayList<>();
        Article article = new Article("No ", "data");
        emptyList.add(article);
        emptyWrapper.setArticles(emptyList);
        return api.getNews(apiKey, theme)
                .onErrorReturn(throwable -> emptyWrapper)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                    .doOnError(throwable -> Log.d("error", "Throwable " + throwable.getMessage()));
    }

}
