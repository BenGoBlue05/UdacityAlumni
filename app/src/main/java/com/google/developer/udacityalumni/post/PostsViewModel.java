package com.google.developer.udacityalumni.post;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.google.developer.udacityalumni.networking.Resource;
import com.google.firebase.firestore.Query;

import java.util.List;
import java.util.Objects;

/**
 * Created by benjaminlewis on 1/21/18.
 */

public class PostsViewModel extends ViewModel {

    private final LiveData<Resource<List<Post>>> posts;

    private final MutableLiveData<Query> query = new MutableLiveData<>();

    public PostsViewModel() {
        posts = Transformations.switchMap(query, query -> {
            if (query == null) {
                return new MutableLiveData<>();
            }
            return PostRepo.getInstance().getPosts(query);
        });
    }

    public LiveData<Resource<List<Post>>> getPosts() {
        return posts;
    }

    public void setQuery(@NonNull Query query) {
        if (!Objects.equals(query, this.query.getValue())) {
            this.query.postValue(query);
        }
    }
}
