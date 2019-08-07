package com.example.news.newsmain;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.news.db.ArticleEntity;
import com.example.news.db.ArticleRepository;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    private ArticleRepository repository;
    private LiveData <List <ArticleEntity>> allArticles;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        repository = new ArticleRepository(application);
        allArticles = repository.getAllArticles();
    }

    public void insert(ArticleEntity note) {
        repository.insert(note);
    }

    public void update(ArticleEntity note) {
        repository.update(note);
    }

    public void delete(ArticleEntity note) {
        repository.delete(note);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public LiveData <List <ArticleEntity>> getAllArticles() {
        return allArticles;
    }

}
