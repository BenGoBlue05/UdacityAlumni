package com.google.developer.udacityalumni.post;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.developer.udacityalumni.R;
import com.google.developer.udacityalumni.base.BaseView;
import com.google.developer.udacityalumni.constants.CollectionNames;
import com.google.developer.udacityalumni.data.JsonReader;
import com.google.developer.udacityalumni.networking.Resource;
import com.google.developer.udacityalumni.user.User;
import com.google.developer.udacityalumni.utils.CollectionUtils;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by benjaminlewis on 1/23/18.
 */

public class PostsPresenter {

    private static final String TAG = PostsPresenter.class.getSimpleName();

    public void processPosts(@Nullable Resource<List<Post>> posts, BaseView<List<Post>> view, Resources resources) {
        if (posts != null) {
            posts.setLoadingVisibility(view.getLoadingIndicator());
            if (posts.isSuccess()) {
                if (CollectionUtils.isEmpty(posts.data)) {
                    List<Post> fakePosts = getFakePosts(resources);
                    view.displayData(fakePosts);
                    setPosts(fakePosts);
                } else {
                    view.displayData(posts.data);
                }
            } else if (posts.isError()) {
                view.showError();
            }
        }
    }

    private void setPosts(@NonNull List<Post> posts) {
        for (Post post : posts) {
            FirebaseFirestore.getInstance().collection(CollectionNames.POSTS).add(post);
        }
    }

    private void setFakeUsers(Resources resources) {
        Map<String, User> userMap = new HashMap<>();
        for (User user : getFakeUsers(resources)) {
            userMap.put(user.getId(), user);
        }
        FirebaseFirestore.getInstance().collection(CollectionNames.USERS).add(userMap);
    }

    public List<Post> getFakePosts(Resources resources) {
        InputStream inputStream = resources.openRawResource(R.raw.posts);
        Type productListType = new TypeToken<ArrayList<Post>>() {
        }.getType();
        try {
            List<Post> posts = JsonReader.readJsonStream(inputStream, productListType);
            setFakeUsers(resources);
            setPosts(posts);
            return posts;
        } catch (IOException e) {
            Log.e(TAG, "Error reading JSON product list", e);
            return new ArrayList<>();
        }
    }
    private List<User> getFakeUsers(Resources resources) {
        InputStream inputStream = resources.openRawResource(R.raw.users);
        Type userTypeList = new TypeToken<ArrayList<User>>() {
        }.getType();
        try {
            return JsonReader.readJsonStream(inputStream, userTypeList);
        } catch (IOException e) {
            Log.e(TAG, "Error reading JSON user list", e);
            return new ArrayList<>();
        }
    }



}
