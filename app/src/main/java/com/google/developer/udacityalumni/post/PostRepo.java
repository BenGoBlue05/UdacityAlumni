package com.google.developer.udacityalumni.post;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.google.developer.udacityalumni.constants.CollectionNames;
import com.google.developer.udacityalumni.networking.FirestoreQueryResource;
import com.google.developer.udacityalumni.networking.FirestoreSetterResource;
import com.google.developer.udacityalumni.networking.Resource;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

/**
 * Created by benjaminlewis on 1/20/18.
 */

public class PostRepo {

    private static PostRepo instance;

    private PostRepo() {
    }

    public static PostRepo getInstance() {
        if (instance == null) {
            instance = new PostRepo();
        }
        return instance;
    }

    public LiveData<Resource<List<Post>>> getPosts(Query query) {
        return new FirestoreQueryResource<List<Post>>(query) {
            @Override
            public List<Post> toObjects(QuerySnapshot documentSnapshots) {
                return documentSnapshots.toObjects(Post.class);
            }
        }.asLiveData();
    }

    public LiveData<Resource<Void>> setPost(@NonNull Post post) {
        DocumentReference ref = FirebaseFirestore.getInstance().collection(CollectionNames.POSTS).document();
        return new FirestoreSetterResource(ref, post).asLiveData();
    }
}
