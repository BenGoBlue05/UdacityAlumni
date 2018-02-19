package com.google.developer.udacityalumni.networking;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

/**
 * Created by benjaminlewis on 1/20/18.
 */

public abstract class FirestoreQueryResource<T> implements NetworkBoundResource<T> {

    private final MutableLiveData<Resource<T>> result = new MutableLiveData<>();

    public FirestoreQueryResource(@NonNull Query query) {
        result.setValue(Resource.loading(null));
        getDocuments(query);
    }

    private void getDocuments(Query query) {
        query.get()
                .addOnSuccessListener(documentSnapshots -> setValue(Resource.success(
                        FirestoreQueryResource.this.toObjects(documentSnapshots))))
                .addOnFailureListener(e -> setValue(null));
    }

    @MainThread
    private void setValue(Resource<T> newValue) {
        if (!Objects.equals(result.getValue(), newValue)) {
            result.setValue(newValue);
        }
    }

    @Override
    public LiveData<Resource<T>> asLiveData() {
        return result;
    }

    public abstract T toObjects(QuerySnapshot documentSnapshots);

}
