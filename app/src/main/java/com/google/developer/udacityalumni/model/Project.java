package com.google.developer.udacityalumni.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;


@IgnoreExtraProperties
public class Project {
    public String name;
    public String uid;
    public int starCount = 0;
    public Map<String, Boolean> stars;
    public Map<String, String> links;
    public Map<String, String> publisherNames;

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("uid", uid);
        result.put("star_count", starCount);
        result.put("likes", stars);
        result.put("links", links);
        result.put("publisher_names", publisherNames);
        return result;
    }
}
