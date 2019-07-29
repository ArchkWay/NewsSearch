package com.example.news.db;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;


@Database(entities = {ArticleEntity.class}, version = 1, exportSchema = false)
public abstract class ArticleDataBase extends RoomDatabase {

    public abstract DaoArticle daoArticle();
    private static ArticleDataBase instance;
    public static synchronized ArticleDataBase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), ArticleDataBase.class, "article_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDBAsyncTask(instance).execute();
        }
    };

    private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void> {
        private DaoArticle daoArticle;

        public PopulateDBAsyncTask(ArticleDataBase db) {
            this.daoArticle = db.daoArticle();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            daoArticle.insert(new ArticleEntity("Title 1", "Description 1"));
            daoArticle.insert(new ArticleEntity("Title 2", "Description 2"));
            daoArticle.insert(new ArticleEntity("Title 3", "Description 3"));
            return null;
        }
    }
}