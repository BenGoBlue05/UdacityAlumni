package com.google.developer.udacityalumni.app;

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
 * Created by benjaminlewis on 2/9/18.
 */

public class AppsViewModel extends ViewModel {

    private final LiveData<Resource<List<App>>> apps;

    private final MutableLiveData<Query> query = new MutableLiveData<>();

    public AppsViewModel() {
        apps = Transformations.switchMap(query, query -> {
            if (query == null) return new MutableLiveData<>();
            return AppsRepo.getInstance().getApps(query);
        });
    }

    public void setQuery(@NonNull Query query) {
        if (!Objects.equals(query, this.query.getValue())) {
            this.query.postValue(query);
        }
    }

    public LiveData<Resource<List<App>>> getApps() {
        return apps;
    }
}
