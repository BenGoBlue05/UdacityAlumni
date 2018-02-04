package com.google.developer.udacityalumni.main;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.developer.udacityalumni.R;
import com.google.developer.udacityalumni.activity.ArticleDetailActivity;
import com.google.developer.udacityalumni.login.LoginActivity;
import com.google.developer.udacityalumni.activity.NewAppActivity;
import com.google.developer.udacityalumni.post.NewPostActivity;
import com.google.developer.udacityalumni.data.AlumContract;
import com.google.developer.udacityalumni.fragment.ArticleFragment;
import com.google.developer.udacityalumni.service.NavMenuServiceConnection;
import com.google.developer.udacityalumni.view.slidingview.BottomNavBarManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
@Deprecated
public class MainFragment extends Fragment
        implements ArticleFragment.ArticleCallback,
        LoaderManager.LoaderCallbacks<Cursor>, TabLayout.OnTabSelectedListener,
        NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener
{


    private List<Long> mArticleIds;
    private List<Integer> mBookmarks;
    private List<String> mTags;
    private static final int LOADER = 101;

    private NavMenuServiceConnection mNavMenuCustomTabs;
    private BottomNavBarManager mBottomNavManager;

    @BindView(R.id.drawer)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tabs)
    TabLayout mTabs;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.nav_view)
    NavigationView mNavView;
    @BindView(R.id.nav_bottom)
    BottomNavigationView mBottomNv;
    @BindView(R.id.main_fab)
    FloatingActionButton mFab;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(rootView);

//        mFab.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentActivity fragmentActivity = getActivity();
        if (fragmentActivity instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) fragmentActivity;
            activity.setSupportActionBar(mToolbar);
            ActionBar supportActionBar = activity.getSupportActionBar();
            if (supportActionBar != null) {
                VectorDrawableCompat indicator
                        = VectorDrawableCompat.create(getResources(), R.drawable.ic_menu, activity.getTheme());
                assert indicator != null;
                indicator.setTint(ResourcesCompat.getColor(getResources(), R.color.colorAccent, activity.getTheme()));
                supportActionBar.setHomeAsUpIndicator(indicator);
                supportActionBar.setDisplayHomeAsUpEnabled(true);
            }
            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseUser user = auth.getCurrentUser();
            TextView tv = mNavView.getHeaderView(0).findViewById(R.id.nav_header_name_tv);
            CircleImageView cv = mNavView.getHeaderView(0).findViewById(R.id.nav_header_prof_pic);
            if (user != null){
                tv.setText(user.getDisplayName());
                if (user.getPhotoUrl() != null) {
                    Picasso.with(getContext()).load(user.getPhotoUrl()).placeholder(R.drawable.placeholder)
                            .error(R.drawable.ic_person).into(cv);
                } else {
                    cv.setImageResource(R.drawable.ic_person);
                }
            }

            Drawable overflowIcon = mToolbar.getOverflowIcon();
            if (overflowIcon != null && getContext() != null)
                overflowIcon.setTint(ContextCompat.getColor(getContext(), R.color.colorAccent));
            mTabs.setupWithViewPager(mViewPager);

            mNavView.setNavigationItemSelectedListener(this);

            mBottomNv.setOnNavigationItemSelectedListener(this);
            mBottomNavManager = new BottomNavBarManager(mBottomNv, () -> {
                if (mViewPager.getCurrentItem() == 1) {
                    mBottomNavManager.setShown();
                } else {
                    mBottomNavManager.setHidden();
                }
            });
        } else {
            throw new IllegalStateException("Host Activity not instance of FragmentActivity");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mTabs.addOnTabSelectedListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mTabs.removeOnTabSelectedListener(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (getActivity() != null) {
            getActivity().getMenuInflater().inflate(R.menu.main, menu);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.sign_out:
                FragmentActivity activity = getActivity();
                if (activity != null) {
                    AuthUI.getInstance()
                            .signOut(activity)
                            .addOnCompleteListener(task -> {
                                startActivity(new Intent(activity, LoginActivity.class));
                                activity.finish();
                            });
                }

                break;
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_classroom || id == R.id.nav_catalog || id == R.id.nav_success) {
            @NavMenuServiceConnection.Urls String url = null;
            switch (id) {
                case R.id.nav_classroom:
                    url = NavMenuServiceConnection.CLASSROOM;
                    break;
                case R.id.nav_catalog:
                    url = NavMenuServiceConnection.CATALOG;
                    break;
                case R.id.nav_success:
                    url = NavMenuServiceConnection.SUCCESS;
                    break;
            }
            mNavMenuCustomTabs.launchUrl(url);
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        return true;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (getContext() != null) {
            return new CursorLoader(getContext(), AlumContract.ArticleEntry.CONTENT_URI,
                    new String[]{AlumContract.ArticleEntry.COL_ARTICLE_ID, AlumContract.ArticleEntry.COL_BOOKMARKED,
                            AlumContract.ArticleEntry.COL_RANDOM_TAG},
                    null, null, AlumContract.ArticleEntry.COL_CREATED_AT + " DESC");
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.getCount() > 0) {
            final long firstId = mArticleIds.get(0);
            while (data.moveToNext()) {
                if (mArticleIds.size() > 10) break;
                long id = data.getLong(0);
                if (firstId != id) {
                    mArticleIds.add(data.getLong(0));
                    mBookmarks.add(data.getInt(1));
                    mTags.add(data.getString(2));
                }
            }
            int len = mArticleIds.size();
            long[] ids = new long[len];
            int[] isBookmarked = new int[len];
            String[] tags = new String[len];
            for (int i = 0; i < len; i++) {
                ids[i] = mArticleIds.get(i);
                isBookmarked[i] = mBookmarks.get(i);
                tags[i] = mTags.get(i);
            }
            startActivity(new Intent(getActivity(), ArticleDetailActivity.class)
                    .putExtra(getString(R.string.article_list_key), ids)
                    .putExtra(getString(R.string.article_bookmarks_key), isBookmarked)
                    .putExtra(getString(R.string.tag_key), tags));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onArticleSelected(long articleId, boolean isBookmarked, String tag) {
        mArticleIds = new ArrayList<>();
        mArticleIds.add(articleId);
        mBookmarks = new ArrayList<>();
        mBookmarks.add(isBookmarked ? 1 : 0);
        mTags = new ArrayList<>();
        mTags.add(tag);
        Loader loader = getLoaderManager().getLoader(LOADER);
        if (loader == null || !loader.isStarted())
            getLoaderManager().initLoader(LOADER, null, this);
        else
            getLoaderManager().restartLoader(LOADER, null, this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        final int pos = tab.getPosition();
        if (pos == 1) {
            mBottomNavManager.show();
        } else {
            mBottomNavManager.hide();
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }

    @Override
    public void onPause() {
        Loader loader = getLoaderManager().getLoader(LOADER);
        if (loader != null) {
            getLoaderManager().destroyLoader(loader.getId());
        }
        super.onPause();
    }


    @Override
    public void onClick(View v) {
        if (mTabs.getTabCount() == 0) return;
        switch (mTabs.getSelectedTabPosition()) {
            case 0:                 //home frag
                startActivity(new Intent(getActivity(), NewPostActivity.class));
                break;
            case 1:
                startActivity(new Intent(getActivity(), NewAppActivity.class));
                break;
        }
    }
}
