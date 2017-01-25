package com.google.developer.udacityalumni.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by benjaminlewis on 1/23/17.
 */

@IgnoreExtraProperties
public class Post {

    public String uid;
    public String userName;
    public String userProfPic;
    public String text;
    public String photoUrl;
    public int numLikes = 0;
    public Map<String, Boolean> likes = new HashMap<>();

    public Post(){}

    public Post(String uid, String userName, String userProfPic, String text, String photoUrl) {
        this.uid = uid;
        this.userName = userName;
        this.userProfPic = userProfPic;
        this.text = text;
        this.photoUrl = photoUrl;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("userName", userName);
        result.put("userProfPic", userProfPic);
        result.put("text", text);
        result.put("photoUrl", photoUrl);
        result.put("numLikes", numLikes);
        result.put("likes", likes);

        return result;
    }
    // [END post_to_map]

}
