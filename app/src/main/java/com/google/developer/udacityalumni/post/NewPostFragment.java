package com.google.developer.udacityalumni.post;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.developer.udacityalumni.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewPostFragment extends Fragment {


    public NewPostFragment() {
        // Required empty public constructor
    }

    public static NewPostFragment newInstance() {

        Bundle args = new Bundle();

        NewPostFragment fragment = new NewPostFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_post, container, false);
    }
}
