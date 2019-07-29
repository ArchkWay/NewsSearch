package com.example.news.newsmain;

import android.os.Handler;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.news.BaseApp;
import com.example.news.R;
import com.example.news.apiservice.pojos.Article;
import com.example.news.apiservice.pojos.NewsWrapper;
import com.example.news.db.ArticleEntity;
import com.example.news.db.ArticleRepository;
import com.example.news.utils.ConnectivityHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class MainNewsVActivity extends AppCompatActivity implements MainNewsContract.view {
    List <Article> newsList = new ArrayList <>();
    RecyclerView rvNews;
    EditText etTheme;
    MainNewsAdapter adapter = new MainNewsAdapter();
    SwipeRefreshLayout srlContainer;
    ArticleEntity articleEntity;
    NoteViewModel noteViewModel;
    @Inject
    MainNewsContract.presenter presenter;
    String theme = "russia";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BaseApp.get(this).getInjector().inject(this);
        setViews();
        loadNews();
        refreshListener();
        editThemeListener();
    }

    private void getDataFromDB() {
        noteViewModel.getAllArticles().observe(this, articles -> {
            Article article;
            List <Article> list = new ArrayList <>();
            for (int i = 0; i < articles.size(); i++) {
                article = new Article(articles.get(i).getTitle(), articles.get(i).getDescription());
                list.add(article);
            }
            setRecyclerAdapter(list);
        });
    }



    private void editThemeListener() {
        etTheme.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    theme = s.toString();
                    if(!theme.equals("")) {
                        loadNews();//loading news after two seconds after input theme
                    }
                }, 2000);
            }
        });
    }

    private void loadNews() {
        if (ConnectivityHelper.isConnectedToNetwork(this)) {
            presenter.attachView(this, getResources().getString(R.string.apiKey), theme); //if connection has interrupted - loading from DB
        } else {
            getDataFromDB(); //if connection has interrupted - loading from DB
        }
    }

    private void setViews() {
        etTheme = findViewById(R.id.etTheme);
        srlContainer = findViewById(R.id.srlContainer);
        rvNews = findViewById(R.id.rvNews);
    }

    private void refreshListener() {
        srlContainer.setOnRefreshListener(() -> {
            if (ConnectivityHelper.isConnectedToNetwork(this)) {
                presenter.attachView(this, getResources().getString(R.string.apiKey), theme);
            } else {
                getDataFromDB();
            }
            new Handler().postDelayed(() -> {
                if (srlContainer != null) {
                    srlContainer.setRefreshing(false);
                }
            }, 500);
        });
    }

    @Override
    public void setNews(NewsWrapper news) {

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        newsList.addAll(news.getArticles());
        setRecyclerAdapter(newsList);
        noteViewModel.delleteAll();
        for (int i = 0; i < news.getArticles().size(); i++) {
            articleEntity = new ArticleEntity(news.getArticles().get(i).getTitle(), news.getArticles().get(i).getDescription());
            noteViewModel.insert(articleEntity);
        }
    }

    private void setRecyclerAdapter(List <Article> list) {
        rvNews.setLayoutManager(new LinearLayoutManager(MainNewsVActivity.this));
        rvNews.setAdapter(adapter);
        adapter.submitList(list);
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }
}
