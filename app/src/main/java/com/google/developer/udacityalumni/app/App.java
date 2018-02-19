package com.google.developer.udacityalumni.app;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

/**
 * Created by benjaminlewis on 2/9/18.
 */

public class App {

    public static final String ANDROID = "Android";
    public static final String WEB = "Web";
    private String id;
    private String name;
    private String photoUrl;
    private String type;
    private String link;
    private String githubUrl;
    private String userId;
    private String userName;
    private String userPhotoUrl;
    private boolean isReact;
    private @ServerTimestamp
    Date timestamp;

    public App() {
    }

    public App(String id, String name, String photoUrl, String type, String link, String githubUrl, String userId, String userName) {
        this(id, name, photoUrl, type, link, githubUrl, userId, userName, "", false);
    }

    public App(String id, String name, String photoUrl, String type, String link, String githubUrl,
               String userId, String userName, String userPhotoUrl, boolean isReact) {
        this.id = id;
        this.name = name;
        this.photoUrl = photoUrl;
        this.type = type;
        this.link = link;
        this.githubUrl = githubUrl;
        this.userId = userId;
        this.userName = userName;
        this.userPhotoUrl = userPhotoUrl;
        this.isReact = isReact;
    }

    public boolean isReact() {
        return isReact;
    }

    public String getType() {
        return type;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getGithubUrl() {
        return githubUrl;
    }

    public void setGithubUrl(String githubUrl) {
        this.githubUrl = githubUrl;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhotoUrl() {
        return userPhotoUrl;
    }

    public void setUserPhotoUrl(String userPhotoUrl) {
        this.userPhotoUrl = userPhotoUrl;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
