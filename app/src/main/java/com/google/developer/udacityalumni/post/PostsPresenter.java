package com.google.developer.udacityalumni.post;

import android.content.res.Resources;
import android.support.annotation.Nullable;

import com.google.developer.udacityalumni.base.BaseView;
import com.google.developer.udacityalumni.networking.Resource;

import java.util.List;

/**
 * Created by benjaminlewis on 1/23/18.
 */

public class PostsPresenter {

    private static final String TAG = PostsPresenter.class.getSimpleName();

    public void processPosts(@Nullable Resource<List<Post>> posts, BaseView<List<Post>> view, Resources resources) {
        if (posts != null) {
            posts.setLoadingVisibility(view.getLoadingIndicator());
            if (posts.isSuccess()) {
                view.displayData(posts.data);
            } else if (posts.isError()) {
                view.showError();
            }
        }
    }
}
