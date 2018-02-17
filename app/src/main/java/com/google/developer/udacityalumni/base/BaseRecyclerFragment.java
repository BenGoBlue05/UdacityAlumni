package com.google.developer.udacityalumni.base;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.developer.udacityalumni.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseRecyclerFragment<M, A extends BaseRecyclerAdapter<M, ? extends RecyclerView.ViewHolder>>
        extends BaseFragment {

    public static final String DIVIDER_KEY = "dividerKey";
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private A adapter;

    public BaseRecyclerFragment() {
    }

    public static BaseRecyclerFragment newInstance() {
        return newInstance(false);
    }

    public static BaseRecyclerFragment newInstance(boolean hasRecylerViewDivider) {
        Bundle args = new Bundle();
        BaseRecyclerFragment fragment = new BaseRecyclerFragment();
        args.putBoolean(DIVIDER_KEY, hasRecylerViewDivider);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_base_recycler_view, container, false);
        progressBar = rootView.findViewById(R.id.progressBar);
        setUpRecyclerView(rootView);
        return rootView;
    }

    public void showLoadingIndicator(boolean isVisible) {
        progressBar.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    public void setAdapter(A adapter) {
        this.adapter = adapter;
        if (recyclerView != null) {
            recyclerView.setAdapter(adapter);
        }
    }

    protected void setUpRecyclerView(@NonNull View rootView) {
        recyclerView = rootView.findViewById(R.id.recyclerView);
        if (adapter != null) {
            recyclerView.setAdapter(adapter);
        }
        Bundle args = getArguments();
        if (args != null && args.getBoolean(DIVIDER_KEY, false)) {
            Context context = getContext();
            if (context != null) {
                recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
            }
        }
    }

    public void setModels(List<M> models) {
        if (adapter != null) adapter.setModels(models);
    }

    public void addModel(M model) {
        if (adapter != null) adapter.addModel(model);
    }

    public void addModels(List<M> models) {
        if (adapter != null) adapter.addModels(models);
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

}
