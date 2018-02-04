package com.google.developer.udacityalumni.apps;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.developer.udacityalumni.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppsFragment extends BaseFragment {

    public AppsFragment() {
    }

    public static AppsFragment newInstance() {

        Bundle args = new Bundle();

        AppsFragment fragment = new AppsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
