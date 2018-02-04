package com.google.developer.udacityalumni.networking;

import android.arch.lifecycle.LiveData;

/**
 * Created by benjaminlewis on 1/22/18.
 */

public interface NetworkBoundResource<T> {

    LiveData<Resource<T>> asLiveData();
}
