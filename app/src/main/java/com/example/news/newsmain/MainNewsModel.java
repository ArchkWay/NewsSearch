package com.example.news.newsmain;

import com.example.news.BaseApp;
import com.example.news.apiservice.pojos.Article;
import com.example.news.apiservice.pojos.NewsWrapper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;

import com.example.news.apiservice.Api;
import com.example.news.apiservice.RetrofitProvider;
import com.example.news.db.ArticleEntity;
import com.example.news.db.NoteViewModel;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class MainNewsModel implements MainNewsContract.Model {
    private final Api api;
    @Inject
    RetrofitProvider provider;
    private NoteViewModel noteViewModel;

    public MainNewsModel(Context context) {
        BaseApp.get(context).getInjector().inject(this);
        api = provider.getApi();
    }

    @Override
    public List <Article> getDataFromDB(FragmentActivity fragmentActivity, @NonNull LifecycleOwner owner) {
        List<Article> list = new ArrayList <>();
        if(noteViewModel == null) noteViewModel = ViewModelProviders.of(fragmentActivity).get(NoteViewModel.class);
        noteViewModel.getAllArticles().observe(owner, articles -> {
            for (int i = 0; i < articles.size(); i++) {
                list.add(new Article(articles.get(i).getTitle(), articles.get(i).getDescription()));
            }
        });
        return list;
    }

    @Override
    public Observable <NewsWrapper> getNewsFromNet(String apiKey, String theme) {
        NewsWrapper emptyWrapper = new NewsWrapper();
        List <Article> emptyList = new ArrayList <>();
        Article article = new Article("_No_", "data");
        emptyList.add(article);
        emptyWrapper.setArticles(emptyList);
        return api.getNews(apiKey, theme)
                .onErrorReturn(throwable -> emptyWrapper)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> Log.d("error", "Throwable " + throwable.getMessage()));
    }

    @Override
    public void deleteAll(FragmentActivity fragmentActivity) {
        if(noteViewModel == null) noteViewModel = ViewModelProviders.of(fragmentActivity).get(NoteViewModel.class);
        noteViewModel.deleteAll();
    }

    @Override
    public void addNewArcticle(ArticleEntity articleEntity) {
        noteViewModel.insert(articleEntity);
    }

}