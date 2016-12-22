package com.google.developer.udacityalumni.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.graphics.drawable.VectorDrawableCompat;
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
import com.google.developer.udacityalumni.adapter.PageAdapter;
import com.google.developer.udacityalumni.fragment.ArticleFragment;

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
        setupViewPager(mViewPager);
        mTabs.setupWithViewPager(mViewPager);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            VectorDrawableCompat indicator
                    = VectorDrawableCompat.create(getResources(), R.drawable.ic_menu, getTheme());
            assert indicator != null;
            indicator.setTint(ResourcesCompat.getColor(getResources(), android.R.color.white, getTheme()));
            supportActionBar.setHomeAsUpIndicator(indicator);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        mNavViewBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.articles:
                        mTabs.setVisibility(View.VISIBLE);
                        mTabs.setupWithViewPager(mViewPager);
                        break;
                    default:
                        mTabs.removeAllTabs();
                        mTabs.setVisibility(View.GONE);
                        break;
                }
                return true;
            }
        });

    }

    private void setupViewPager(ViewPager viewPager) {
        mPageAdapter = new PageAdapter(getSupportFragmentManager());
        mPageAdapter.addFragment(new ArticleFragment(), getString(R.string.spotlight));
        mPageAdapter.addFragment(new ArticleFragment(), getString(R.string.feed));
        viewPager.setAdapter(mPageAdapter);
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
            case R.id.careers:
                Log.i(LOG_TAG, "CAREER TAB SELECTED");
                mTabs.removeAllTabs();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void displayFragment() {

    }
}
