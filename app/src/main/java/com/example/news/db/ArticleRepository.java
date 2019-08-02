package com.example.news.db;

import android.app.Application;
import android.os.AsyncTask;


import androidx.lifecycle.LiveData;

import java.util.List;

public class ArticleRepository {

    private DaoArticle daoArticle;
    private LiveData <List<ArticleEntity>> allArticles;

    public ArticleRepository(Application application){
        ArticleDataBase dataBase = ArticleDataBase.getInstance(application);
        daoArticle = dataBase.daoArticle();
        allArticles = daoArticle.getAll();
    }

    public void insert(ArticleEntity articleEntity){
        new InsertArticleAsyncTask(daoArticle).execute(articleEntity);
    }

    public void update(ArticleEntity articleEntity){
        new UpdateArticleAsyncTask(daoArticle).execute(articleEntity);
    }

    public void delete(ArticleEntity articleEntity){
        new DeleteArticleAsyncTask(daoArticle).execute(articleEntity);
    }

    public void deleteAll(){
        new DeleteAllArticlesAsyncTask(daoArticle).execute();
    }

    public LiveData<List<ArticleEntity>> getAllArticles(){
        return allArticles;
    }



  private static class InsertArticleAsyncTask extends AsyncTask<ArticleEntity, Void, Void>{
        private DaoArticle daoArticle;
        private InsertArticleAsyncTask(DaoArticle daoArticle){
            this.daoArticle = daoArticle;
        }
      @Override
      protected Void doInBackground(ArticleEntity... articleEntities) {
            daoArticle.insert(articleEntities[0]);
          return null;
      }
  }
    private static class UpdateArticleAsyncTask extends AsyncTask<ArticleEntity, Void, Void>{
        private DaoArticle daoArticle;
        private UpdateArticleAsyncTask(DaoArticle daoArticle){
            this.daoArticle = daoArticle;
        }
        @Override
        protected Void doInBackground(ArticleEntity... articleEntities) {
            daoArticle.update(articleEntities[0]);
            return null;
        }
    }
    private static class DeleteArticleAsyncTask extends AsyncTask<ArticleEntity, Void, Void>{
        private DaoArticle daoArticle;
        private DeleteArticleAsyncTask(DaoArticle daoArticle){
            this.daoArticle = daoArticle;
        }
        @Override
        protected Void doInBackground(ArticleEntity... articleEntities) {
            daoArticle.delete(articleEntities[0]);
            return null;
        }
    }
    private static class DeleteAllArticlesAsyncTask extends AsyncTask<Void, Void, Void>{
        private DaoArticle daoArticle;
        private DeleteAllArticlesAsyncTask(DaoArticle daoArticle){
            this.daoArticle = daoArticle;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            daoArticle.deleteAll();
            return null;
        }
    }

}