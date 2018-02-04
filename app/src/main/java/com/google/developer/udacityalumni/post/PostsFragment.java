package com.google.developer.udacityalumni.post;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.developer.udacityalumni.BuildConfig;
import com.google.developer.udacityalumni.R;
import com.google.developer.udacityalumni.base.BaseFragment;
import com.google.developer.udacityalumni.base.BaseView;
import com.google.developer.udacityalumni.constants.CollectionNames;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostsFragment extends BaseFragment implements BaseView<List<Post>> {

    private ProgressBar progressBar;

    private PostAdapter adapter;

    private PostsPresenter presenter;

    private static final int LIMIT = 50;

    public PostsFragment() {
    }

    public static PostsFragment newInstance() {
        Bundle args = new Bundle();
        PostsFragment fragment = new PostsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_mainv2, container, false);
        progressBar = rootView.findViewById(R.id.progressBar);
        if (savedInstanceState == null) {
            adapter = new PostAdapter();
            presenter = new PostsPresenter();
        }
        setUpRecyclerView(rootView);
        return rootView;
    }

    private void setUpRecyclerView(@NonNull View rootView) {
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
        Context context = getContext();
        if (context != null) {
            recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        PostsViewModel viewModel = ViewModelProviders.of(this).get(PostsViewModel.class);
        viewModel.setQuery(getPostsQuery());
        viewModel.getPosts().observe(this, posts -> {
            presenter.processPosts(posts, this, getResources());
        });
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
        if (data == null && BuildConfig.DEBUG) {
            data = presenter.getFakePosts(getResources());
        }
        adapter.setPosts(data);
    }

    @Override
    public View getLoadingIndicator() {
        return progressBar;
    }

}
