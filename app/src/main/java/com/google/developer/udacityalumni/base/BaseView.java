package com.google.developer.udacityalumni.base;

import android.view.View;

/**
 * Created by benjaminlewis on 1/23/18.
 */

public interface BaseView<T> {

    void showError();

    void displayData(T data);

    View getLoadingIndicator();
}
