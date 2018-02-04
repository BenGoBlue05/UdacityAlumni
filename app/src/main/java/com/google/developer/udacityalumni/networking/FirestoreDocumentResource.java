package com.google.developer.udacityalumni.networking;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import com.google.firebase.firestore.DocumentReference;

import java.util.Objects;

/**
 * Created by benjaminlewis on 1/22/18.
 */

public class FirestoreDocumentResource<T> implements NetworkBoundResource<T> {

    private MutableLiveData<Resource<T>> result = new MutableLiveData<>();

    @MainThread
    public FirestoreDocumentResource(@NonNull DocumentReference docRef, final Class<T> clazz) {
        docRef.get()
                .addOnSuccessListener(snapshot -> setValue(Resource.success(snapshot.toObject(clazz))))
                .addOnFailureListener(e -> setValue(Resource.error(null)));
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
}
