package com.example.news.newsmain;


import com.example.news.db.ArticleEntity;

import java.util.List;

public interface MainNewsContractPresenter<V> {
    void attachView(V mvpView, String keyApi, String theme);
    void detachView();
}
