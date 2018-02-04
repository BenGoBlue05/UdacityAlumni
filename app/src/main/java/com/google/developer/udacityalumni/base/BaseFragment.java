package com.google.developer.udacityalumni.base;


import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.developer.udacityalumni.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment {

    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_base, container, false);
    }

    public Snackbar getSnackbar(@IdRes int container, @StringRes int message) {
        FragmentActivity activity = getActivity();
        if (activity instanceof BaseActivity) {
            return ((BaseActivity) activity).getSnackBar(container, message);
        }
        return null;
    }

    public Snackbar getSnackbar(@IdRes int container, String message) {
        FragmentActivity activity = getActivity();
        if (activity instanceof BaseActivity) {
            return ((BaseActivity) activity).getSnackBar(container, message);
        }
        return null;
    }

    public void showSnackbar(@IdRes int container, @StringRes int message) {
        FragmentActivity activity = getActivity();
        if (activity instanceof BaseActivity) {
            ((BaseActivity) activity).showSnackbar(container, message);
        }
    }
}
