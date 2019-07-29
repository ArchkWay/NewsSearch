package com.example.news.db;




import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface DaoArticle {

    @Insert
    Long insert(ArticleEntity article);

    @Update
    void update(ArticleEntity article);

    @Delete
    void delete(ArticleEntity article);

    @Query("DELETE FROM article_table")
    void deleteAll();

    @Query("SELECT* FROM article_table ORDER BY title DESC")
    LiveData <List <ArticleEntity>> getAll();
}