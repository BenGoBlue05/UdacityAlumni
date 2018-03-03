package com.google.developer.udacityalumni.networking;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

/**
 * Created by benjaminlewis on 1/22/18.
 */

public class FirestoreSetterResource implements NetworkBoundResource<Void> {

    private final MutableLiveData<Resource<Void>> result = new MutableLiveData<>();


    public FirestoreSetterResource(@NonNull DocumentReference document, Object pojo) {
        result.setValue(Resource.loading(null));
        document.set(pojo)
                .addOnSuccessListener(aVoid -> handleSuccess())
                .addOnFailureListener(e -> handleError());
    }

    public FirestoreSetterResource(@NonNull CollectionReference collection, Object pojo) {
        result.setValue(Resource.loading(null));
        collection.add(pojo)
                .addOnSuccessListener(aVoid -> handleSuccess())
                .addOnFailureListener(e -> handleError());
    }

    private void handleSuccess(){
        result.setValue(Resource.success(null));
    }

    private void handleError(){
        result.setValue(Resource.error(null));
    }

    @Override
    public LiveData<Resource<Void>> asLiveData() {
        return result;
    }

}
