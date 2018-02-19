package com.google.developer.udacityalumni.post;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.google.developer.udacityalumni.base.BaseRecyclerFragment;
import com.google.developer.udacityalumni.base.BaseView;
import com.google.developer.udacityalumni.constants.CollectionNames;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostsFragment extends BaseRecyclerFragment<Post, PostsAdapter>
        implements BaseView<List<Post>> {

    private static final int LIMIT = 50;
    private PostsPresenter presenter;

    public PostsFragment() {
    }

    public static PostsFragment newInstance() {
        Bundle args = new Bundle();
        PostsFragment fragment = new PostsFragment();
        args.putBoolean(BaseRecyclerFragment.DIVIDER_KEY, true);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            setAdapter(new PostsAdapter());
            presenter = new PostsPresenter();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        PostsViewModel viewModel = ViewModelProviders.of(this).get(PostsViewModel.class);
        viewModel.setQuery(getPostsQuery());
        viewModel.getPosts().observe(this, posts -> presenter.processPosts(posts, this));
    }

    private Query getPostsQuery() {
        return FirebaseFirestore.getInstance()
                .collection(CollectionNames.POSTS)
                .limit(LIMIT);
    }

    @Override
    public void showError() {
    }

    @Override
    public void displayData(List<Post> data) {
        setModels(data);
    }

    @Override
    public View getLoadingIndicator() {
        return getProgressBar();
    }

}
