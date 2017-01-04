package com.google.developer.udacityalumni.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.facebook.stetho.Stetho;
import com.google.developer.udacityalumni.R;
import com.google.developer.udacityalumni.adapter.ArticleAdapter;
import com.google.developer.udacityalumni.adapter.PageAdapter;
import com.google.developer.udacityalumni.fragment.ArticleFragment;
import com.google.developer.udacityalumni.service.ArticleIntentService;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ArticleFragment.ArticleCallback{

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    TabLayout.OnTabSelectedListener mTabListener;

    @BindView(R.id.navview_bottom)
    BottomNavigationView mNavViewBottom;
    @BindView(R.id.drawer)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tabs)
    TabLayout mTabs;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    TabLayout.Tab mArticleTab;
    TabLayout.Tab mCareersTab;
    TabLayout.Tab mMentorshipTab;
    TabLayout.Tab mMeetUpsTab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Stetho.initializeWithDefaults(this);
        ButterKnife.bind(this);
        startService(new Intent(this, ArticleIntentService.class));
        setSupportActionBar(mToolbar);
        setupViewPager(mViewPager);
        mTabs.setupWithViewPager(mViewPager);
        setUpTabs();
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            VectorDrawableCompat indicator
                    = VectorDrawableCompat.create(getResources(), R.drawable.ic_menu, getTheme());
            assert indicator != null;
            indicator.setTint(ResourcesCompat.getColor(getResources(), R.color.colorAccent, getTheme()));
            supportActionBar.setHomeAsUpIndicator(indicator);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        int[][] states = new int[][] {
                new int[] { android.R.attr.state_checked}, // enabled
                new int[] {-android.R.attr.state_checked}, // disabled
        };

        int[] colors = new int[] {
                ContextCompat.getColor(MainActivity.this, R.color.colorAccent),
                ContextCompat.getColor(MainActivity.this, R.color.unselected),
        };
        ColorStateList myList = new ColorStateList(states, colors);
        mNavViewBottom.setItemIconTintList(myList);
        mNavViewBottom.setItemTextColor(myList);
        mTabListener = new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Drawable icon = tab.getIcon();
                assert icon != null;
                icon.setTint(ContextCompat.getColor(MainActivity.this, R.color.colorAccent));
                int bottomNavVisibility = View.GONE;
                switch (tab.getPosition()){
                    case 0:
                        mToolbar.setTitle(getString(R.string.home));
                        bottomNavVisibility = View.VISIBLE;
                        break;
                    case 1:
                        mToolbar.setTitle(getString(R.string.careers));
                        break;
                    case 2:
                        mToolbar.setTitle(getString(R.string.mentorship));
                        break;
                    case 3:
                        mToolbar.setTitle(getString(R.string.meetups));
                        break;
                    default:
                        Log.e(LOG_TAG, "TAB POSITION UNRECOGINIZED");
                }
                mNavViewBottom.setVisibility(bottomNavVisibility);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Drawable icon = tab.getIcon();
                assert icon != null;
                icon.setTint(ContextCompat.getColor(MainActivity.this, R.color.unselected));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        };

    }

    private void setupViewPager(ViewPager viewPager) {
        PageAdapter mPageAdapter = new PageAdapter(getSupportFragmentManager());
        mPageAdapter.addFragment(new ArticleFragment());
        mPageAdapter.addFragment(new ArticleFragment());
        mPageAdapter.addFragment(new ArticleFragment());
        mPageAdapter.addFragment(new ArticleFragment());
        viewPager.setAdapter(mPageAdapter);
    }

    private void setUpTabs(){
        mArticleTab = mTabs.getTabAt(0);
        mCareersTab = mTabs.getTabAt(1);
        mMentorshipTab = mTabs.getTabAt(2);
        mMeetUpsTab = mTabs.getTabAt(3);
        Drawable homeIcon = ContextCompat.getDrawable(this, R.drawable.ic_home);
        homeIcon.setTint(ContextCompat.getColor(this, R.color.colorAccent));
        mArticleTab.setIcon(R.drawable.ic_home);
        mCareersTab.setIcon(R.drawable.ic_careers);
        mMentorshipTab.setIcon(R.drawable.ic_mentorship);
        mMeetUpsTab.setIcon(R.drawable.ic_meetups);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mTabs.addOnTabSelectedListener(mTabListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mTabs.removeOnTabSelectedListener(mTabListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onArticleSelected(long articleId, ArticleAdapter.ArticleViewHolder vh) {
        startActivity(new Intent(this, ArticleDetailActivity.class)
                .putExtra(this.getString(R.string.article_id_key), articleId));
    }
}
