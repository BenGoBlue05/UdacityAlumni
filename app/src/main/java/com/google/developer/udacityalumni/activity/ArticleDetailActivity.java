package com.google.developer.udacityalumni.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.developer.udacityalumni.R;
import com.google.developer.udacityalumni.adapter.PageAdapter;
import com.google.developer.udacityalumni.fragment.ArticleDetailFragment;

public class ArticleDetailActivity extends AppCompatActivity {
    private static final String LOG_TAG = ArticleDetailActivity.class.getSimpleName();
    private PageAdapter mPageAdapter;
    private ViewPager mViewPager;
    private long[] mArticleIds;

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
        mArticleIds = getIntent().getLongArrayExtra(getString(R.string.article_list_key));
        mPageAdapter = new PageAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        setUpViewpager(mViewPager, mArticleIds);
    }

    private void setUpViewpager(ViewPager viewPager, long[] articleIds) {
        for (int i = 0; i < articleIds.length; i++) {
            Bundle args = new Bundle();
            if (i == 0) args.putBoolean(getString(R.string.article_is_first_key), true);
            if (articleIds.length > 1 && i == articleIds.length - 1) {
                args.putBoolean(getString(R.string.article_is_last_key), true);
                args.putLong(getString(R.string.article_next_id_key), articleIds[i - 1]);
            } else{
                args.putLong(getString(R.string.article_next_id_key), articleIds[i + 1]);
            }
            args.putLong(getString(R.string.article_id_key), articleIds[i]);
            Fragment frag = new ArticleDetailFragment();
            frag.setArguments(args);
            mPageAdapter.addFragment(frag);
        }
        viewPager.setAdapter(mPageAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_article, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        long articleId = mArticleIds.length > 0 && mViewPager != null ?
                mArticleIds[mViewPager.getCurrentItem()] : -1;
        switch (item.getItemId()){
            case R.id.menu_detail_bookmark:
                if (articleId != -1){
                    item.setIcon(R.drawable.ic_bookmark);
                    //TODO: Store article id as user favorite and toggle bookmark icon
                    //NOT READY TO DO YET: STILL UPDATING DATA STRUCTURE
                }
                break;
            case R.id.menu_detail_share:
                if (articleId != -1){
                    //TODO: Share link to article
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
