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
    private final HashMap<String, Boolean> likes = new HashMap<>();
    private String id;
    private String userId;
    private String userDisplayName;
    private String userProfPicUrl;
    private String textContent;
    private String photoContentUrl;
    private int numLikes;
    private @ServerTimestamp
    Date timestamp;

    public Post() {
        //Empty constructor needed for FireStore toObject()
    }

    public Post(String id, String userId, String userDisplayName, String userProfPicUrl, String textContent, String photoContentUrl, int numLikes) {
        this.id = id;
        this.userId = userId;
        this.userDisplayName = userDisplayName;
        this.userProfPicUrl = userProfPicUrl;
        this.textContent = textContent;
        this.photoContentUrl = photoContentUrl;
        this.numLikes = numLikes;
    }

    public Post(String id, String userId, String userDisplayName, String userProfPicUrl) {
        this(id, userId, userDisplayName, userProfPicUrl, "", "", 0);
    }

    public Post(String id, String userId, String userDisplayName, String userProfPicUrl, String textContent) {
        this(id, userId, userDisplayName, userProfPicUrl, textContent, "", 0);
    }

    public String getId() {
        return id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }

    public String getUserProfPicUrl() {
        return userProfPicUrl;
    }

    public void setUserProfPicUrl(String userProfPicUrl) {
        this.userProfPicUrl = userProfPicUrl;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public String getPhotoContentUrl() {
        return photoContentUrl;
    }

    public void setPhotoContentUrl(String photoContentUrl) {
        this.photoContentUrl = photoContentUrl;
    }

    public int getNumLikes() {
        return numLikes;
    }

    public void setNumLikes(int numLikes) {
        this.numLikes = numLikes;
    }

    public HashMap<String, Boolean> getLikes() {
        return likes;
    }
}
