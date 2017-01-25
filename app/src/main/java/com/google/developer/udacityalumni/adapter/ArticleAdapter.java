package com.google.developer.udacityalumni.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.developer.udacityalumni.R;
import com.google.developer.udacityalumni.animation.AnimationUtils;
import com.google.developer.udacityalumni.fragment.ArticleFragment;
import com.google.developer.udacityalumni.utility.Utility;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    private Cursor mCursor;
    final private Context mContext;
    final private ArticleItemClickHandler mArticleClickHandler;

    public ArticleAdapter(Context mContext, ArticleItemClickHandler mArticleClickHandler) {
        this.mContext = mContext;
        this.mArticleClickHandler = mArticleClickHandler;
    }

    public interface ArticleItemClickHandler {
        void onArticleClick(long articleId, boolean isBookmarked, String tag);

        void onProfPicClick(long userId, int position);

        void onFollowUserClick(long userId, long articleId, boolean wasFollowingBeforeClick, ImageView icon);

        void onShareClick(String title);

        void onBookmarkClick(long articleId, boolean wasBookmarkedBeforeClick, ImageView icon);
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (parent instanceof RecyclerView) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_article, parent, false);
            view.setFocusable(true);
            return new ArticleViewHolder(view);
        } else {
            throw new RuntimeException("NOT BOUND TO RECYCLER_VIEW");
        }
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        String profPic = mCursor.getString(ArticleFragment.IND_USER_AVATAR);
        if (TextUtils.isEmpty(profPic) || profPic.equals("null")){
            holder.mProfPicCV.setImageResource(R.drawable.ic_person);
        } else{
            Picasso.with(mContext).load(profPic).placeholder(R.drawable.placeholder)
                    .error(R.drawable.ic_person).into(holder.mProfPicCV);
        }
        holder.mAuthorTimeAgo.setText(Utility.formatAuthorAndTimeAgo(mContext,
                mCursor.getString(ArticleFragment.IND_USER_NAME), mCursor.getLong(ArticleFragment.IND_CREATED_AT)));
        if (mCursor.getInt(ArticleFragment.IND_SPOTLIGHTED) == 1) {
            holder.mSpotLightTV.setVisibility(View.VISIBLE);
        } else {
            holder.mSpotLightTV.setVisibility(View.GONE);
        }
        String image = mCursor.getString(ArticleFragment.IND_IMAGE);
        if (TextUtils.isEmpty(image) || image.equals("null")) {
            holder.mImageView.setVisibility(View.GONE);
        } else {
            holder.mImageView.setVisibility(View.VISIBLE);
            Picasso.with(mContext).load(image).placeholder(R.drawable.placeholder)
                    .error(R.drawable.ic_person).into(holder.mImageView);
        }
        holder.mTitleTV.setText(mCursor.getString(ArticleFragment.IND_TITLE));
        holder.mTagTV.setText(mCursor.getString(ArticleFragment.IND_RANDOM_TAG));
        boolean isBookmarked = mCursor.getInt(ArticleFragment.IND_BOOKMARKED) == 1;
        holder.mBookmarkIV.setImageResource(isBookmarked ? R.drawable.ic_bookmark : R.drawable.ic_bookmark_outline);
        boolean isFollowing = mCursor.getInt(ArticleFragment.IND_FOLLOWING_AUTHOR) == 1;
        holder.mFollowIV.setImageResource(isFollowing ? R.drawable.ic_following : R.drawable.ic_add_follow);

        //Animation on Article
        AnimationUtils.scaleXY(holder);

    }

    public void swapCursor(Cursor cursor) {
        mCursor = cursor;
        notifyDataSetChanged();
    }

    public Cursor getCursor() {
        return mCursor;
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) return 0;
        return mCursor.getCount();
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.item_prof_pic)
        CircleImageView mProfPicCV;
        @BindView(R.id.item_username)
        TextView mAuthorTimeAgo;
        @BindView(R.id.item_spotlight)
        TextView mSpotLightTV;
        @BindView(R.id.item_image)
        ImageView mImageView;
        @BindView(R.id.item_title)
        TextView mTitleTV;
        @BindView(R.id.item_tag)
        TextView mTagTV;
        @BindView(R.id.item_bookmark)
        ImageView mBookmarkIV;
        @BindView(R.id.item_share)
        ImageView mShareIV;
        @BindView(R.id.item_follow)
        ImageView mFollowIV;


        ArticleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            mProfPicCV.setOnClickListener(this);
            mBookmarkIV.setOnClickListener(this);
            mShareIV.setOnClickListener(this);
            mFollowIV.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mCursor != null && mCursor.moveToPosition(getAdapterPosition())){
                int viewId = v.getId();
                long articleId = mCursor.getLong(ArticleFragment.IND_ARTICLE_ID);
                long authorId = mCursor.getLong(ArticleFragment.IND_USER_ID);
                boolean isBookmarked = mCursor.getInt(ArticleFragment.IND_BOOKMARKED) == 1;
                //for some reason switch statement doesn't work for this
                if (viewId == mProfPicCV.getId()){
                    mArticleClickHandler.onProfPicClick(authorId, getAdapterPosition());
                } else if (viewId == mBookmarkIV.getId()){
                    mArticleClickHandler.onBookmarkClick(articleId, isBookmarked, mBookmarkIV);
                } else if (viewId == mShareIV.getId()){
                    mArticleClickHandler.onShareClick(mCursor.getString(ArticleFragment.IND_TITLE));
                } else if (viewId == mFollowIV.getId()){
                    mArticleClickHandler.onFollowUserClick(authorId, articleId,
                            mCursor.getInt(ArticleFragment.IND_FOLLOWING_AUTHOR) == 1, mFollowIV);
                } else{
                    mArticleClickHandler.onArticleClick(articleId, isBookmarked,
                            mCursor.getString(ArticleFragment.IND_RANDOM_TAG));
                }
            }
        }
    }

}
