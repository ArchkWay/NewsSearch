package com.example.news.newsmain;

import android.os.Handler;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import android.widget.Toast;

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
import com.example.news.utils.ConnectivityHelper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;


public class MainNewsVActivity extends AppCompatActivity implements MainNewsContract.View {
    List <Article> newsList = new ArrayList <>();
    String theme = "russia";//default theme
    RecyclerView rvNews;
    SearchView svTheme;
    MainNewsAdapter adapter = new MainNewsAdapter();
    SwipeRefreshLayout srlContainer;
    ArticleEntity articleEntity;
    NoteViewModel noteViewModel;
    boolean errorHandlerNoConnect;
    @Inject
    MainNewsContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BaseApp.get(this).getInjector().inject(this);
        setViews();
        setRecyclerView();
        loadNews(savedInstanceState);
        refreshListener();
        editSearchThemeListener(savedInstanceState);

    }
    private void setRecyclerView(){
        rvNews.setLayoutManager(new LinearLayoutManager(MainNewsVActivity.this));
        rvNews.setAdapter(adapter);
    }

    private void editSearchThemeListener(Bundle savedInstance) {
        svTheme.setQueryHint(getString(R.string.enter_theme));
        final Handler handler = new Handler();
        svTheme.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                theme = query;
                loadNews(savedInstance);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                theme = newText;
                handler.postDelayed(() -> {
                if (!newText.equals("")) {
                        loadNews(savedInstance);//loading news after two seconds after input theme
                }
                }, 1500);
                return false;
            }
        });
    }

    private void loadNews(Bundle savedInstance) {
        if (ConnectivityHelper.isConnectedToNetwork(this)) {
            presenter.attachView(this, getResources().getString(R.string.apiKey), theme); //if connection has interrupted - loading from DB
        } else {
            if (savedInstance != null) {
                presenter.attachDB(this, this, this);//if connection has interrupted - loading from DB
            } else {
                Toast.makeText(this, this.getString(R.string.error), Toast.LENGTH_SHORT).show();
                errorHandlerNoConnect = true;
            }
        }
    }

    private void setViews() {
        svTheme = findViewById(R.id.svTheme);
        srlContainer = findViewById(R.id.srlContainer);
        rvNews = findViewById(R.id.rvNews);
    }

    private void refreshListener() {
        srlContainer.setOnRefreshListener(() -> {
            if (ConnectivityHelper.isConnectedToNetwork(this)) {
                presenter.attachView(this, getResources().getString(R.string.apiKey), theme);
            } else {
                if (!errorHandlerNoConnect) {
                presenter.attachDB(this, this, this);
                } else Toast.makeText(getApplicationContext(), this.getString(R.string.error), Toast.LENGTH_SHORT).show();
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
        newsList = listWithoutEmpties(news.getArticles());
        errorHandlerNoConnect = false;
        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        setItemsToAdapter(newsList);
        noteViewModel.deleteAll();
        for (int i = 0; i < news.getArticles().size(); i++) {
            articleEntity = new ArticleEntity(news.getArticles().get(i).getTitle(), news.getArticles().get(i).getDescription());
            noteViewModel.insert(articleEntity);
        }
    }

    @Override
    public void setNewsFromDB(List <Article> list) {
        setItemsToAdapter(list);
    }

    private void setItemsToAdapter(List <Article> list) {
        rvNews.setLayoutManager(new LinearLayoutManager(MainNewsVActivity.this));
        rvNews.setAdapter(adapter);
        adapter.submitList(list);
    }
    private List <Article> listWithoutEmpties(List<Article> list){
        for(Article article: list) {
            if (article.getTitle().equals("_No_")){
                list.remove(article);
            }
        }
        return list;

    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }
}
