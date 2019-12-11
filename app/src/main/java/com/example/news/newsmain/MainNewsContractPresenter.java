package com.example.news.newsmain;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import com.example.news.db.ArticleEntity;

public interface MainNewsContractPresenter<V> {
    void attachView(V mvpView, String keyApi, String theme);
    void attachDbItems(V mvpView,FragmentActivity fragmentActivity, LifecycleOwner owner);
    void deleteAll(FragmentActivity fragmentActivity);
    void addNewArcticle(ArticleEntity articleEntity);
}
