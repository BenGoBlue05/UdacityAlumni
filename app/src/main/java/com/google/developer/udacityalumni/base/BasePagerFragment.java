package com.google.developer.udacityalumni.base;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.developer.udacityalumni.R;

/**
 * Created by benjaminlewis on 2/7/18.
 */

public abstract class BasePagerFragment extends BaseFragment {

    private FragmentPagerAdapter adapter;

    private ViewPager viewPager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_base_pager, container, false);
        if (adapter == null) {
            adapter = initPagerAdapter();
        }
        viewPager = rootView.findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);
        TabLayout tabs = rootView.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        return rootView;
    }

    @NonNull
    public abstract FragmentPagerAdapter initPagerAdapter();

    public FragmentPagerAdapter getAdapter() {
        return adapter;
    }

    public ViewPager getViewPager() {
        return viewPager;
    }


}
