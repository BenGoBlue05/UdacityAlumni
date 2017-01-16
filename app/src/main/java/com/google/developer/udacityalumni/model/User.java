package com.google.developer.udacityalumni.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by benjaminlewis on 1/14/17.
 */

@IgnoreExtraProperties
public class User {

    public String name;
    public String email;
    public String photoUrl;
    public int followerCount = 0;
    public int followingCount = 0;
    public Map<String, Boolean> followers = new HashMap<>();
    public Map<String, Boolean> following =  new HashMap<>();
    public Map<String, Boolean> nanodegrees = new HashMap<>();
    public Map<String, Boolean> projects = new HashMap<>();

    public User() {
    }

    public User(String name, String email, String photoUrl) {
        this.name = name;
        this.email = email;
        this.photoUrl = photoUrl;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("email", email);
        result.put("photoUrl", photoUrl);
        result.put("followers_count", followerCount);
        result.put("followers", followers);
        result.put("following_count", followingCount);
        result.put("following", following);
        result.put("projects", projects);
        result.put("nanodegrees", nanodegrees);
        return result;
    }
}
