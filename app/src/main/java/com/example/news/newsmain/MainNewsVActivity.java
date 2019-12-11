package com.example.news.newsmain;

import android.os.Handler;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;

import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.news.BaseApp;
import com.example.news.R;
import com.example.news.apiservice.pojos.Article;
import com.example.news.apiservice.pojos.NewsWrapper;
import com.example.news.db.ArticleEntity;
import com.example.news.db.NoteViewModel;
import com.example.news.newsmain.func.ListWithOutEmpties;
import com.example.news.utils.ConnectivityHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class MainNewsVActivity extends AppCompatActivity implements MainNewsContract.View {
    String theme = "russia";//default theme
    RecyclerView rvNews;
    SearchView svTheme;
    MainNewsAdapter adapter = new MainNewsAdapter();
    SwipeRefreshLayout srlContainer;

    boolean errorHandlerNoConnect;
    @Inject
    MainNewsContract.Presenter presenter;
    ListWithOutEmpties cleaner;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BaseApp.get(this).getInjector().inject(this);
        cleaner = new ListWithOutEmpties();
        setViews();
        setRecyclerView();
        loadNews();
        refreshListener();
        editSearchThemeListener();
    }

    private void setViews() {
        svTheme = findViewById(R.id.svTheme);
        srlContainer = findViewById(R.id.srlContainer);
        rvNews = findViewById(R.id.rvNews);
    }

    private void setRecyclerView() {
        rvNews.setLayoutManager(new LinearLayoutManager(MainNewsVActivity.this));
        rvNews.setAdapter(adapter);
    }

    private void editSearchThemeListener() {
        svTheme.setQueryHint(getString(R.string.enter_theme));
        svTheme.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                theme = query;
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                theme = newText;
                if (handler != null) handler.removeCallbacksAndMessages(null);
                handler = new Handler();
                handler.postDelayed(() -> {
                    if (!newText.equals("")) {
                        loadNews();//loading news after two seconds after input theme
                    }
                }, 1000);
                return false;
            }
        });
    }

    private void loadNews() {
        if (ConnectivityHelper.isConnectedToNetwork(this)) {
            presenter.attachView(this, getResources().getString(R.string.apiKey), theme); //if connection has interrupted - loading from DB
        } else {
            presenter.attachDbItems(this, this, this);
            Toast.makeText(this, this.getString(R.string.error), Toast.LENGTH_SHORT).show();
        }
    }

    private void refreshListener() {
        srlContainer.setOnRefreshListener(() -> {
            if (ConnectivityHelper.isConnectedToNetwork(this)) {
                presenter.attachView(this, getResources().getString(R.string.apiKey), theme);
            } else {
                if (!errorHandlerNoConnect) {
                    presenter.attachDbItems(this, this, this);
                } else Toast.makeText(getApplicationContext(), this.getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
            new Handler().postDelayed(() -> {
                if (srlContainer != null) {
                    srlContainer.setRefreshing(false);
                    presenter.attachView(this, getResources().getString(R.string.apiKey), theme);
                }
            }, 500);
        });
    }

    @Override
    public void setNews(NewsWrapper news) {
        ArticleEntity articleEntity;
        if ((news.getArticles()) == null || cleaner.cleanList(news.getArticles()).isEmpty()) { return;
        } else {
            adapter.submitList(cleaner.cleanList(news.getArticles()));
            presenter.deleteAll(this);
            for (int i = 0; i < news.getArticles().size(); i++) {
                articleEntity = new ArticleEntity(news.getArticles().get(i).getTitle(), news.getArticles().get(i).getDescription());
                presenter.addNewArcticle(articleEntity);
            }
        }
    }

    @Override
    public void setNewsFromDB(List <Article> listArticle) {
        if (listArticle != null) adapter.submitList(listArticle);
    }
}
