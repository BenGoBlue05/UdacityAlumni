package com.google.developer.udacityalumni.base;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.developer.udacityalumni.networking.Resource;

/**
 * Created by benjaminlewis on 2/9/18.
 */

public class BasePresenter<T> implements BaseContract.Presenter<T> {

    public BasePresenter() {
    }

    @Override
    public void processResponse(@Nullable Resource<T> resource, @NonNull BaseContract.View<T> view) {
        if (resource != null) {
            View loadingIndicator = view.getLoadingIndicator();
            if (loadingIndicator != null) {
                view.getLoadingIndicator().setVisibility(resource.isLoading() ? View.VISIBLE : View.GONE);
            }
            if (resource.isSuccess()) {
                view.displayData(resource.getData());
            } else if (resource.isError()) {
                view.showError();
            }
        }
    }
}
