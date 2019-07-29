package com.example.news.apiservice;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

final public class RetrofitProvider {

    private static final String BASE_URL = "https://newsapi.org/v2/";
    private final Retrofit retrofit;

    public RetrofitProvider(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public Api getApi(){
        return retrofit.create(Api.class);
    }

}
