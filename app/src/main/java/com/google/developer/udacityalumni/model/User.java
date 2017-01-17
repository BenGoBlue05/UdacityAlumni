package com.google.developer.udacityalumni.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by benjaminlewis on 1/14/17.
 */

@IgnoreExtraProperties
public class User {

    public String name;
    public String email;
    public String photoUrl;

    public User() {
    }

    public User(String name, String email, String photoUrl) {
        this.name = name;
        this.email = email;
        this.photoUrl = photoUrl;
    }
}
