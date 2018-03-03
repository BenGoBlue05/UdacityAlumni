package com.google.developer.udacityalumni.app;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.developer.udacityalumni.R;
import com.google.developer.udacityalumni.base.BaseContract;
import com.google.developer.udacityalumni.base.BasePresenter;
import com.google.developer.udacityalumni.base.BaseRecyclerFragment;
import com.google.developer.udacityalumni.constants.CollectionNames;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;


public class AppsFragment extends BaseRecyclerFragment<App, AppAdapter> implements BaseContract.View<List<App>> {

    public static final int ANDROID = 2;
    public static final int WEB = 3;
    private static final String APP_TYPE_KEY = "appTypeKey";
    private int type;
    private Query query;
    private BaseContract.Presenter<List<App>> presenter;

    public AppsFragment() {
    }

    public static AppsFragment newInstance(@AppType int type) {
        Bundle args = new Bundle();
        AppsFragment fragment = new AppsFragment();
        args.putInt(APP_TYPE_KEY, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            Bundle args = getArguments();
            type = args != null ? args.getInt(APP_TYPE_KEY, -1) : -1;
            if (type == -1) throw new IllegalArgumentException("Must set item_app type");
            presenter = new BasePresenter<>();
            query = buildQuery();
            setAdapter(new AppAdapter());
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppsViewModel viewModel = ViewModelProviders.of(this).get(AppsViewModel.class);
        viewModel.setQuery(query);
        viewModel.getApps().observe(this, apps -> presenter.processResponse(apps, AppsFragment.this));
    }

    @Override
    public void showError() {
        showSnackbar(getView(), R.string.error_connection);
    }

    @Override
    public void displayData(List<App> data) {
        setModels(data);
    }

    @Override
    public View getLoadingIndicator() {
        return getProgressBar();
    }

    private Query buildQuery() {
        String appType = getString(type == ANDROID ? R.string.android : R.string.web);
        return FirebaseFirestore.getInstance()
                .collection(CollectionNames.APPS)
                .whereEqualTo("type", appType)
                .limit(50);
    }

    @IntDef({ANDROID, WEB})
    public @interface AppType {
    }


}
