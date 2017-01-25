package com.google.developer.udacityalumni.fragment;


import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.developer.udacityalumni.R;
import com.google.developer.udacityalumni.data.AlumContract;
import com.google.developer.udacityalumni.utility.Utility;
import com.google.developer.udacityalumni.view.slidingview.AvatarCardAdapter;
import com.google.developer.udacityalumni.view.slidingview.SlidingViewManager;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArticleDetailFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    //Viewpager order - selected article at first position and then articles sorted by date

    private static final String LOG_TAG = ArticleDetailFragment.class.getSimpleName();
    private final int LOADER_FIRST_ARTICLE = 100, LOADER_SECOND_ARTICLE = 200, LOADER_CURRENT_AND_NEXT = 300;
    private long mArticleId, mNextArticleId;
    private boolean mIsFollowing;

    private SlidingViewManager mSlidingViewManager;
    private AvatarCardAdapter mSlidingViewAdapter;

    @BindView(R.id.detail_article_title_tv)
    TextView mTitleTV;
    @BindView(R.id.detail_article_image)
    ImageView mImageView;
    @BindView(R.id.detail_article_tv)
    TextView mArticleTV;
    @BindView(R.id.detail_article_prof_pic)
    CircleImageView mProfPicCV;
    @BindView(R.id.detail_article_author_time_ago)
    TextView mAuthorTimeAgoTV;
    @BindView(R.id.detail_article_next_tv)
    TextView mNextArticleTV;
    @BindView(R.id.detail_article_back_arrow)
    ImageView mBackArrowIV;
    @BindView(R.id.detail_article_forward_arrow)
    ImageView mForwardArrowIV;
    @BindView(R.id.detail_article_spotlight)
    TextView mSpotlightTV;
    @BindView(R.id.detail_article_image_next)
    ImageView mNextImageView;
    @BindView(R.id.detail_article_follow)
    ImageView mFollowIV;
    @BindView(R.id.detail_article_preview_ll)
    LinearLayout mPreviewLL;

    public ArticleDetailFragment() {
    }

    public interface DetailArticleCallbacks {
        void onNextArticleClicked();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail_article, container, false);
        ButterKnife.bind(this, rootView);
        Bundle args = getArguments();
        if (args != null) {
            mArticleId = args.getLong(getString(R.string.article_id_key), -1L);
            boolean isFirstArticle = args.getBoolean(getString(R.string.article_is_first_key), false);
            if (isFirstArticle) mBackArrowIV.setVisibility(View.INVISIBLE);
            mNextArticleId = args.getLong(getString(R.string.article_next_id_key), -1L);
            boolean isLast = args.getBoolean(getString(R.string.article_is_last_key), false);
            if (isLast) mForwardArrowIV.setVisibility(View.INVISIBLE);
            mFollowIV.setOnClickListener(this);
            mProfPicCV.setOnClickListener(this);
            mPreviewLL.setOnClickListener(this);
            LoaderManager loaderManager = getLoaderManager();
            if (loaderManager != null) {
                if (mArticleId != -1L) {
                    if (isFirstArticle || isLast) {
                        loaderManager.initLoader(LOADER_FIRST_ARTICLE, args, this);
                        if (mNextArticleId != -1L) {
                            if (loaderManager.getLoader(LOADER_SECOND_ARTICLE) != null){
                                loaderManager.restartLoader(LOADER_SECOND_ARTICLE, args, this);
                            } else{
                                loaderManager.initLoader(LOADER_SECOND_ARTICLE, args, this);
                            }
                        }
                    } else {
                        if (mNextArticleId != -1L) {
                            loaderManager.initLoader(LOADER_CURRENT_AND_NEXT, args, this);
                        }
                    }
                }
            }
        }

        mSlidingViewManager = new SlidingViewManager(this);
        mSlidingViewAdapter = new AvatarCardAdapter(slidingCardClickListener);
        mSlidingViewManager.setAdapter(mSlidingViewAdapter);
        if (savedInstanceState != null) {
            mSlidingViewManager.onRestoreInstanceState(savedInstanceState);
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mSlidingViewManager.onSaveInstanceState(outState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = null;
        long currentArticleId = args.getLong(getString(R.string.article_id_key), -1L);
        switch (id) {
            case LOADER_FIRST_ARTICLE:
                if (currentArticleId != -1L)
                    loader = new CursorLoader(getContext(), AlumContract.ArticleEntry
                            .buildUriWithId(currentArticleId), ArticleFragment.ARTICLE_COLUMNS,
                            null, null, null);
                break;
            case LOADER_SECOND_ARTICLE:
                Log.i(LOG_TAG, "ARTICLE ID: " + mNextArticleId);
                if (mNextArticleId != -1L) {
                    loader = new CursorLoader(getContext(), AlumContract.ArticleEntry.buildUriWithId(mNextArticleId),
                            ArticleFragment.ARTICLE_COLUMNS, null, null, null);
                }
                break;

            case LOADER_CURRENT_AND_NEXT:
                String colArticleId = AlumContract.ArticleEntry.COL_ARTICLE_ID;
                if (currentArticleId != -1L){
                    loader = new CursorLoader(getContext(), AlumContract.ArticleEntry.CONTENT_URI,
                            ArticleFragment.ARTICLE_COLUMNS, colArticleId + " =? OR " + colArticleId + " =?",
                            new String[]{String.valueOf(currentArticleId), String.valueOf(mNextArticleId)},
                            AlumContract.ArticleEntry.COL_CREATED_AT + " DESC");
                }
                break;
            default:
                Log.e(LOG_TAG, "LOADER ID NOT FOUND");
        }

        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        int loaderId = loader.getId();
        if (loaderId == LOADER_FIRST_ARTICLE || loaderId == LOADER_CURRENT_AND_NEXT) {
            if (data != null && data.moveToFirst()) {
                mTitleTV.setText(data.getString(ArticleFragment.IND_TITLE));
                String author = data.getString(ArticleFragment.IND_USER_NAME);
                String image = data.getString(ArticleFragment.IND_IMAGE);
                if (data.getInt(ArticleFragment.IND_SPOTLIGHTED) == 0)
                    mSpotlightTV.setVisibility(View.GONE);
                if (image == null || image.equals("null")) {
                    mImageView.setVisibility(View.GONE);
                } else {
                    mImageView.setVisibility(View.VISIBLE);
                    Picasso.with(getContext()).load(image).placeholder(R.drawable.placeholder)
                            .error(R.drawable.udacity_logo_banner).into(mImageView);
                }
                String content = data.getString(ArticleFragment.IND_CONTENT);
                mArticleTV.setText(content);
                String profPic = data.getString(ArticleFragment.IND_USER_AVATAR);
                if (profPic == null || TextUtils.isEmpty(image) || profPic.equals("null")) {
                    mProfPicCV.setImageResource(R.drawable.ic_person);
                } else {
                    Picasso.with(getContext()).load(profPic).placeholder(R.drawable.placeholder)
                            .error(R.drawable.ic_person).into(mProfPicCV);
                }
                mAuthorTimeAgoTV.setText(Utility.formatAuthorAndTimeAgo(getContext(), author,
                        data.getLong(ArticleFragment.IND_CREATED_AT)));
                mIsFollowing = data.getInt(ArticleFragment.IND_FOLLOWING_AUTHOR) == 1;
                setFollowingIcon();

                mSlidingViewAdapter.setName(author);
                mSlidingViewAdapter.setImageUri(Uri.parse(profPic));
                mSlidingViewAdapter.setContent(content);
            }

        }
        if (loaderId == LOADER_CURRENT_AND_NEXT || loaderId == LOADER_SECOND_ARTICLE) {
            if (data != null && data.moveToNext()) {
                Log.i(LOG_TAG, "SECOND LOAD: " + data.getString(ArticleFragment.IND_TITLE));
                mNextArticleTV.setText(data.getString(ArticleFragment.IND_TITLE));
                String nextImage = data.getString(ArticleFragment.IND_IMAGE);
                if (nextImage == null || nextImage.equals("null")) {
                    mNextImageView.setVisibility(View.GONE);
                } else {
                    mNextImageView.setVisibility(View.VISIBLE);
                    Picasso.with(getContext()).load(nextImage).placeholder(R.drawable.placeholder)
                            .error(R.drawable.udacity_logo).into(mNextImageView);
                }
            }
        }

    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.detail_article_follow:
                setFollowingIcon();
                mIsFollowing = !mIsFollowing;
                ContentValues values = new ContentValues();
                values.put(AlumContract.ArticleEntry.COL_FOLLOWING_AUTHOR, mIsFollowing ? 1 : 0);
                getContext().getContentResolver().update(AlumContract.ArticleEntry.buildUriWithId(mArticleId),
                        values, null, null);
                break;
            case R.id.detail_article_prof_pic:
                //TODO: have userName bio pane slide from bottom;
                mSlidingViewManager.animate();
                break;
            case R.id.detail_article_preview_ll:
                ((DetailArticleCallbacks) getActivity()).onNextArticleClicked();
                break;
        }
    }

    private void setFollowingIcon() {
        mFollowIV.setImageResource(mIsFollowing ? R.drawable.ic_following : R.drawable.ic_add_follow);
    }

    private final AvatarCardAdapter.OnClickListener slidingCardClickListener =
            new AvatarCardAdapter.OnClickListener() {
        @Override
        public void onSeeMoreClick() {
            Toast.makeText(getContext(), "See More Clicked", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onDismissClick() {
            mSlidingViewManager.animate();
        }
    };

}
