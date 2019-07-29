package com.example.news.apiservice;

import com.example.news.apiservice.pojos.NewsWrapper;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {
    @GET("everything")
    Observable<NewsWrapper> getNews(@Query("apiKey") String apiKey, @Query("q") String q);

}
