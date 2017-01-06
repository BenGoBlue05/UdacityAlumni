package com.google.developer.udacityalumni.fragment;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.developer.udacityalumni.R;
import com.google.developer.udacityalumni.data.AlumContract;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArticleDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    @BindView(R.id.detail_article_title_tv)
    TextView mTitleTV;
    @BindView(R.id.detail_article_date)
    TextView mDateTV;
    @BindView(R.id.detail_article_author_tv)
    TextView mAuthorTV;
    @BindView(R.id.detail_article_image)
    ImageView mImageView;
    @BindView(R.id.detail_article_tv)
    TextView mArticleTV;
    @BindView(R.id.detail_article_prof_pic)
    CircleImageView mProfPicCV;
    @BindView(R.id.detail_article_author_tv1)
    TextView mAuthor1TV;

    Long mArticleId;
    int LOADER_ARTICLE = 100;


    public ArticleDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail_article, container, false);
        ButterKnife.bind(this, rootView);
        mArticleId = getArguments().getLong(getContext().getString(R.string.article_id_key), -1L);
        if (mArticleId != -1L) getLoaderManager().initLoader(LOADER_ARTICLE, null, this);
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(), AlumContract.ArticleEntry.buildUriWithId(mArticleId),
                ArticleFragment.ARTICLE_COLUMNS, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()){
            mTitleTV.setText(data.getString(ArticleFragment.IND_TITLE));
            String author = data.getString(ArticleFragment.IND_USER_NAME);
            mAuthorTV.setText(author);
            String image = data.getString(ArticleFragment.IND_IMAGE);
            if (image == null || image.equals("null")){
                mImageView.setVisibility(View.GONE);
            } else{
                mImageView.setVisibility(View.VISIBLE);
                Picasso.with(getContext()).load(image).placeholder(R.drawable.placeholder)
                        .error(R.drawable.udacity_logo_banner).into(mImageView);
            }
            String profPic = data.getString(ArticleFragment.IND_USER_AVATAR);
            if (image == null || image.equals("null")){
                mProfPicCV.setVisibility(View.GONE);
            } else{
                mProfPicCV.setVisibility(View.VISIBLE);
                Picasso.with(getContext()).load(profPic).placeholder(R.drawable.placeholder)
                        .error(R.drawable.ic_person).into(mProfPicCV);
            }
            mAuthor1TV.setText(author);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
