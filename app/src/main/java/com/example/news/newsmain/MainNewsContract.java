package com.example.news.newsmain;




import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;

import com.example.news.apiservice.pojos.Article;
import com.example.news.apiservice.pojos.NewsWrapper;

import java.util.List;

import io.reactivex.Observable;

public interface MainNewsContract {
    interface View {
        void setNews(NewsWrapper news);
        void setNewsFromDB(List <Article> list);
    }

    interface Presenter extends MainNewsContractPresenter<View> {}

    interface Model {
        List<Article> getDataFromDB2(FragmentActivity activity, @NonNull LifecycleOwner owner);
        Observable<NewsWrapper> getNews(String api, String theme);
    }
}
