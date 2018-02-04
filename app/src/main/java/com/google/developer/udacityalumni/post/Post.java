package com.google.developer.udacityalumni.post;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by benjaminlewis on 1/23/17.
 */

@IgnoreExtraProperties
public class Post {
    private String userId;
    private String userDisplayName;
    private String userProfPicUrl;
    private String textContent;
    private String photoContentUrl;
    private int numLikes;
    private final HashMap<String, Boolean> likes = new HashMap<>();
    private @ServerTimestamp
    Date timestamp;

    public Post() {
        //Empty constructor needed for FireStore toObject()
    }

    public Post(String userId, String userDisplayName, String userProfPicUrl, String textContent, String photoContentUrl, int numLikes) {
        this.userId = userId;
        this.userDisplayName = userDisplayName;
        this.userProfPicUrl = userProfPicUrl;
        this.textContent = textContent;
        this.photoContentUrl = photoContentUrl;
        this.numLikes = numLikes;
    }

    public Post(String userId, String userDisplayName, String userProfPicUrl) {
        this(userId, userDisplayName, userProfPicUrl, "", "", 0);
    }

    public Post(String userId, String userDisplayName, String userProfPicUrl, String textContent) {
        this(userId, userDisplayName, userProfPicUrl, textContent, "", 0);
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public String getUserProfPicUrl() {
        return userProfPicUrl;
    }

    public String getTextContent() {
        return textContent;
    }

    public String getPhotoContentUrl() {
        return photoContentUrl;
    }

    public int getNumLikes() {
        return numLikes;
    }

    public HashMap<String, Boolean> getLikes() {
        return likes;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setUserProfPicUrl(String userProfPicUrl) {
        this.userProfPicUrl = userProfPicUrl;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public void setPhotoContentUrl(String photoContentUrl) {
        this.photoContentUrl = photoContentUrl;
    }

    public void setNumLikes(int numLikes) {
        this.numLikes = numLikes;
    }
}
