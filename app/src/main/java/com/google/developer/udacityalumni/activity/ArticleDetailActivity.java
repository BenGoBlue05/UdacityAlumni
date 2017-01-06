package com.google.developer.udacityalumni.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.developer.udacityalumni.R;
import com.google.developer.udacityalumni.adapter.PageAdapter;
import com.google.developer.udacityalumni.fragment.ArticleDetailFragment;

public class ArticleDetailActivity extends AppCompatActivity {

    private PageAdapter mPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.status_bar_color));
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_article_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            Drawable backArrow = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back);
            backArrow.setTint(ContextCompat.getColor(this, R.color.colorAccent));
            actionBar.setHomeAsUpIndicator(backArrow);
        }
        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.black));
        long[] articleIds = getIntent().getLongArrayExtra(getString(R.string.article_list_key));
        mPageAdapter = new PageAdapter(getSupportFragmentManager());
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setUpViewpager(viewPager, articleIds);
    }

    private void setUpViewpager(ViewPager viewPager, long[] articleIds) {
        for (long id : articleIds) {
            Bundle args = new Bundle();
            args.putLong(getString(R.string.article_id_key), id);
            Fragment frag = new ArticleDetailFragment();
            frag.setArguments(args);
            mPageAdapter.addFragment(frag);
        }
        viewPager.setAdapter(mPageAdapter);
    }
}
