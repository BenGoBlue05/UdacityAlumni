package com.google.developer.udacityalumni.base;

import com.google.developer.udacityalumni.networking.Resource;

/**
 * Created by benjaminlewis on 2/9/18.
 */

public interface BaseContract {

    interface View<T> {
        void showError();

        void displayData(T data);

        android.view.View getLoadingIndicator();
    }

    interface Presenter<T> {
        void processResponse(Resource<T> resource, View<T> view);
    }
}
