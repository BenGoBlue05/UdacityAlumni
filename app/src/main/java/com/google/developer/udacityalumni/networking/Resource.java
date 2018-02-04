package com.google.developer.udacityalumni.networking;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by benjaminlewis on 1/20/18.
 */

public class Resource<T> {

    public static final int LOADING = 0;

    public static final int SUCCESS = 1;

    public static final int ERROR = 2;

    public static final int GETTER = 0;

    public static final int SETTER = 1;

    public final int status;

    @Nullable
    public final T data;

    public Resource(@LoadingState int status, @Nullable T data) {
        this.status = status;
        this.data = data;
    }

    public static <T> Resource<T> loading(@Nullable T data) {
        return new Resource<>(LOADING, data);
    }

    public static <T> Resource<T> success(@Nullable T data) {
        return new Resource<>(SUCCESS, data);
    }

    public static <T> Resource<T> error(@Nullable T data) {
        return new Resource<>(ERROR, data);
    }

    public boolean isError() {
        return status == ERROR;
    }

    public boolean isSuccess() {
        return status == SUCCESS;
    }

    public boolean isLoading() {
        return status == LOADING;
    }

    public void setLoadingVisibility(@NonNull View loadingIndicator) {
        loadingIndicator.setVisibility(status == LOADING ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Resource<?> resource = (Resource<?>) o;

        return status == resource.status &&
                (data != null ? data.equals(resource.data) : resource.data == null);
    }

}
