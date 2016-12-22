package com.google.developer.udacityalumni.activity;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.google.developer.udacityalumni.fragment.ArticleFragment;
import com.google.developer.udacityalumni.R;
import com.google.developer.udacityalumni.adapter.PageAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private PageAdapter mPageAdapter;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Stetho.initializeWithDefaults(this);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        if (mViewPager != null){
            setupViewPager(mViewPager);
            mTabs.setupWithViewPager(mViewPager);
        } else{
            Log.i(LOG_TAG, "mVIEWPAGER IS NULL");
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        mPageAdapter = new PageAdapter(getSupportFragmentManager());
        mPageAdapter.addFragment(new ArticleFragment(), getString(R.string.spotlight));
        mPageAdapter.addFragment(new ArticleFragment(), getString(R.string.feed));
        viewPager.setAdapter(mPageAdapter);
    }



}
