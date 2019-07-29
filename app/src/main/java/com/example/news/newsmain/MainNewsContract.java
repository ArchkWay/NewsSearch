package com.example.news.newsmain;



import com.example.news.apiservice.pojos.NewsWrapper;

import io.reactivex.Observable;

public interface MainNewsContract {
    interface view {
        void setNews(NewsWrapper news);
    }

    interface presenter extends MainNewsContractPresenter<view> {

    }

    interface model {
        Observable<NewsWrapper> getNews(String api, String theme);
    }
}
