package com.google.developer.udacityalumni.app;

import android.arch.lifecycle.LiveData;

import com.google.developer.udacityalumni.networking.FirestoreQueryResource;
import com.google.developer.udacityalumni.networking.Resource;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

/**
 * Created by benjaminlewis on 2/9/18.
 */

public class AppsRepo {

    private static AppsRepo instance;

    private AppsRepo() {
    }

    public static AppsRepo getInstance() {
        if (instance == null) {
            instance = new AppsRepo();
        }
        return instance;
    }

    public LiveData<Resource<List<App>>> getApps(Query query) {
        return new FirestoreQueryResource<List<App>>(query) {

            @Override
            public List<App> toObjects(QuerySnapshot documentSnapshots) {
                return documentSnapshots.toObjects(App.class);
            }
        }.asLiveData();
    }
}
