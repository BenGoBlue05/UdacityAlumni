package com.google.developer.udacityalumni.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.developer.udacityalumni.R;
import com.google.developer.udacityalumni.adapter.PageAdapter;
import com.google.developer.udacityalumni.data.AlumContract;
import com.google.developer.udacityalumni.fragment.ArticleDetailFragment;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleDetailActivity extends AppCompatActivity
        implements ViewPager.OnPageChangeListener, ArticleDetailFragment.DetailArticleCallbacks {
    private static final String LOG_TAG = ArticleDetailActivity.class.getSimpleName();
    private PageAdapter mPageAdapter;
    private ViewPager mViewPager;
    @BindView(R.id.detail_article_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.detail_toolbar_tv)
    TextView mToolbarTitleTV;
    private long[] mArticleIds;
    private int[] mBookMarks;
    private String[] mTags;
    private Menu mMenu;
    private Integer mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (savedInstanceState != null)
            mPosition = savedInstanceState.getInt(getString(R.string.viewpager_position_key));
        if (intent != null) {
            mArticleIds = intent.getLongArrayExtra(getString(R.string.article_list_key));
            mBookMarks = intent.getIntArrayExtra(getString(R.string.article_bookmarks_key));
            mTags = intent.getStringArrayExtra(getString(R.string.tag_key));
            Log.i(LOG_TAG, Arrays.toString(mArticleIds));
            if (mToolbar != null) {
                String title = mPosition == null ? mTags[0] : mTags[mPosition];
                mToolbarTitleTV.setText(formatToolbarTitle(title));
            }
        }
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.status_bar_color));
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            Drawable backArrow = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back);
            backArrow.setTint(ContextCompat.getColor(this, R.color.colorAccent));
            actionBar.setHomeAsUpIndicator(backArrow);
            actionBar.setDisplayShowTitleEnabled(false);
        }

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
            } else {
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
        Intent intent = getIntent();
        getMenuInflater().inflate(R.menu.detail_article, menu);
        if (intent != null) {
            int[] bookmarks = intent.getIntArrayExtra(getString(R.string.article_bookmarks_key));
            MenuItem item = menu.findItem(R.id.menu_detail_bookmark);

            if (item != null && bookmarks.length > 0) {
                int position = mPosition == null ? 0 : mPosition;
                setBookmarkIcon(item, bookmarks[position] == 1);
            }
            mMenu = menu;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int ind = mViewPager != null ? mViewPager.getCurrentItem() : -1;
        long articleId = ind != -1 && mArticleIds.length > ind ? mArticleIds[ind] : -1;
        switch (item.getItemId()) {
            case R.id.menu_detail_bookmark:
                if (articleId != -1 && mBookMarks.length > ind) {
                    boolean wasBookmarkedBeforeClick = mBookMarks[ind] == 1;
                    setBookmarkIcon(item, !wasBookmarkedBeforeClick);
                    int newBookmark = !wasBookmarkedBeforeClick ? 1 : 0;
                    mBookMarks[ind] = newBookmark;
                    ContentValues values = new ContentValues();
                    values.put(AlumContract.ArticleEntry.COL_BOOKMARKED, newBookmark);
                    getContentResolver().update(AlumContract.ArticleEntry.buildUriWithId(articleId),
                            values, null, null);
                }
                break;
            case R.id.menu_detail_share:
                if (articleId != -1) {
                    //TODO: Share link to article
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mViewPager != null) mViewPager.addOnPageChangeListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mViewPager != null) mViewPager.removeOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        if (mBookMarks.length > position && mArticleIds.length > position) {
            if (mMenu != null) {
                MenuItem item = mMenu.findItem(R.id.menu_detail_bookmark);
                if (item != null) setBookmarkIcon(item, mBookMarks[position] == 1);
            } if (mToolbar != null && mTags.length > 0){
                mToolbarTitleTV.setText(formatToolbarTitle(mTags[position]));
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private void setBookmarkIcon(MenuItem item, boolean isBookmarked) {
        item.setIcon(isBookmarked ? R.drawable.ic_bookmark : R.drawable.ic_bookmark_outline);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mViewPager != null) outState.putInt(getString(R.string.viewpager_position_key),
                mViewPager.getCurrentItem());

    }

    @Override
    public void onNextArticleClicked() {
        if (mViewPager != null && mPageAdapter != null){
            int ind = mViewPager.getCurrentItem();
            int count = mPageAdapter.getCount();
            if (ind > -1 && count > 1){
                if (ind != count - 1)
                    mViewPager.setCurrentItem(ind + 1);
                else
                    mViewPager.setCurrentItem(ind - 1);
            }
        }
    }

    private SpannableString formatToolbarTitle(String tag){
        String articles = getString(R.string.articles);
        SpannableString str = new SpannableString(getString(R.string.articles) + "\n" + tag);
        int articlesEnd = articles.length();
        str.setSpan(new StyleSpan(Typeface.BOLD), 0, articlesEnd, 0);
        str.setSpan(new RelativeSizeSpan(1.2f), 0, articlesEnd, 0);
        str.setSpan(new ForegroundColorSpan(Color.BLACK), 0, articlesEnd, 0);
        str.setSpan(new StyleSpan(Typeface.ITALIC), articlesEnd, str.length(), 0);
        str.setSpan(new RelativeSizeSpan(.8f), articlesEnd, str.length(), 0);
        str.setSpan(new ForegroundColorSpan(Color.GRAY), articlesEnd, str.length(), 0);
        return str;
    }
}
