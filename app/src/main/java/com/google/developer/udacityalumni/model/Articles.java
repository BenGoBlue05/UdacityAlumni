package com.google.developer.udacityalumni.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rajan Maurya
 *         On 19/03/17.
 */

public class Articles {

    @SerializedName("articles")
    List<Article> articles = new ArrayList<>();

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}
