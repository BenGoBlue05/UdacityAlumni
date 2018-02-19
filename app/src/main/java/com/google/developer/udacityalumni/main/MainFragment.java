package com.google.developer.udacityalumni.main;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.firebase.ui.auth.AuthUI;
import com.google.developer.udacityalumni.R;
import com.google.developer.udacityalumni.app.AppsFragment;
import com.google.developer.udacityalumni.base.BaseFragment;
import com.google.developer.udacityalumni.fakedata.FakeDataActivity;
import com.google.developer.udacityalumni.login.LoginActivity;
import com.google.developer.udacityalumni.post.NewPostActivity;
import com.google.developer.udacityalumni.post.PostsFragment;
import com.google.developer.udacityalumni.view.UAViewUtils;

/**
 * Created by benjaminlewis on 2/15/18.
 */

public class MainFragment extends BaseFragment
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    public static final int HOME = 0;

    public static final int APPS = 1;

    public static final int NEWS = 2;
    private ViewPager viewPager;
    private MainAdapter adapter;
    private TabLayout tabs;
    private Fragment[] appsFragments;
    private Fragment postsFragment;
    private String[] appsTitles;
    private MainViewModel viewModel;
    private FrameLayout container;
    private DrawerLayout drawer;
    private NavigationView navView;
    private FloatingActionButton fab;
    private boolean fragmentAdded;

    public MainFragment() {
    }

    public static MainFragment newInstance() {
        Bundle args = new Bundle();
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        if (savedInstanceState == null) {
            fragmentAdded = false;
        }
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        setHasOptionsMenu(true);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("");
        drawer = view.findViewById(R.id.drawer);
        navView = view.findViewById(R.id.navView);
        toolbar.setNavigationOnClickListener(v -> drawer.openDrawer(navView));
        container = view.findViewById(R.id.fragmentContainer);
        viewPager = view.findViewById(R.id.viewpager);
        if (adapter == null) adapter = new MainAdapter(getFragmentManager());
        viewPager.setAdapter(adapter);
        tabs = view.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FragmentActivity activity = getActivity();
        if (activity instanceof AppCompatActivity) {
            ((AppCompatActivity) activity).setSupportActionBar(toolbar);
        }
        BottomNavigationView bottomNavView = view.findViewById(R.id.bottomNav);
        bottomNavView.setOnNavigationItemSelectedListener(this);
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            if (getContext() != null) startActivity(NewPostActivity.getLaunchIntent(getContext()));
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getNavState().observe(this, state -> {
            if (state != null) updateState(state);
        });
    }

    private void updateState(@NavState int state) {
        switch (state) {
            case HOME:
                displayHome();
                break;
            case APPS:
                displayApps();
                break;
            case NEWS:
                displayNews();
                break;
            default:
                throw new IllegalStateException(getNavTypeNotSetMessage());
        }
    }

    private void displayNews() {
        UAViewUtils.setVisibleGone(viewPager, false);
        UAViewUtils.setVisibleGone(container, true);
        UAViewUtils.setVisibleGone(tabs, false);
        if (fab != null) fab.hide();
    }

    private void displayApps() {
        UAViewUtils.setVisibleGone(viewPager, true);
        UAViewUtils.setVisibleGone(container, false);
        UAViewUtils.setVisibleGone(tabs, true);
        if (fab != null) fab.hide();
        if (appsFragments == null && adapter != null) {
            initAppsFragments();
            adapter.notifyDataSetChanged();
        }
    }

    private void initAppsFragments() {
        if (appsFragments == null) {
            appsFragments = new Fragment[]{AppsFragment.newInstance(AppsFragment.ANDROID),
                    AppsFragment.newInstance(AppsFragment.WEB)};
        }
        if (appsTitles == null) {
            appsTitles = new String[]{getString(R.string.android), getString(R.string.web)};
        }
    }

    private void displayHome() {
        UAViewUtils.setVisibleGone(viewPager, false);
        UAViewUtils.setVisibleGone(container, true);
        UAViewUtils.setVisibleGone(tabs, false);
        if (!fragmentAdded) {
            addFragment(R.id.fragmentContainer, getPostsFragment());
            fragmentAdded = true;
        } else {
            replaceFragment(R.id.fragmentContainer, getPostsFragment());
        }
        if (fab != null) fab.show();
    }

    private Fragment getPostsFragment() {
        if (postsFragment == null) {
            postsFragment = PostsFragment.newInstance();
        }
        return postsFragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out:
                signOut();
                break;
            case R.id.fakeData:
                Context context = getContext();
                if (context != null) startActivity(FakeDataActivity.getLaunchIntent(context));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        Context context = getContext();
        if (context != null) {
            AuthUI.getInstance()
                    .signOut(context)
                    .addOnSuccessListener(aVoid -> startActivity(LoginActivity.getLaunchIntent(context)))
                    .addOnFailureListener(Throwable::printStackTrace);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (viewModel != null) viewModel.setNavState(getNavState(item));
        return true;
    }

    @NavState
    private int getNavState(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                return HOME;
            case R.id.navigation_apps:
                return APPS;
            case R.id.navigation_news:
                return NEWS;
            default:
                throw new IllegalStateException(getNavTypeNotSetMessage());
        }
    }

    private String getNavTypeNotSetMessage() {
        return "Nav Type not set";
    }

    @IntDef({HOME, APPS, NEWS})
    @interface NavState {
    }

    private class MainAdapter extends FragmentPagerAdapter {

        private MainAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (appsFragments != null && appsFragments.length > position)
                return appsFragments[position];
            return null;
        }

        @Override
        public int getCount() {
            return appsFragments != null ? appsFragments.length : 0;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return appsTitles != null ? appsTitles[position] : null;
        }
    }
}
