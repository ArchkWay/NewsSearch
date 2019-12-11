package com.example.news.newsmain.func;

import com.example.news.apiservice.pojos.Article;

import java.util.List;

public class ListWithOutEmpties {
    public List <Article> cleanList(List<Article> listWithoutEmpties){
        for (Article article : listWithoutEmpties) {
            if (article.getTitle().equals("_No_")) {
                listWithoutEmpties.remove(article);
            }
        }
        return listWithoutEmpties;
    }
}
