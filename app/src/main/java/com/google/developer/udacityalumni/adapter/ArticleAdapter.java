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
import com.google.developer.udacityalumni.fragment.ArticleFragment;
import com.google.developer.udacityalumni.utility.Utility;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    private static final String LOG_TAG = ArticleAdapter.class.getSimpleName();
    private Cursor mCursor;
    final private Context mContext;
    final private ArticleClickHandler mArticleClickHandler;

    public ArticleAdapter(Context mContext, ArticleClickHandler mArticleClickHandler) {
        this.mContext = mContext;
        this.mArticleClickHandler = mArticleClickHandler;
    }

    public interface ArticleClickHandler {
        void onArticleClick(long articleId, ArticleViewHolder vh);
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (parent instanceof RecyclerView) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item, parent, false);
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
            holder.mProfPicCV.setVisibility(View.GONE);
        } else{
            holder.mProfPicCV.setVisibility(View.VISIBLE);
            Picasso.with(mContext)
                    .load(mCursor.getString(ArticleFragment.IND_USER_AVATAR))
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.ic_person)
                    .into(holder.mProfPicCV);
        }
        holder.mAuthorTimeAgo.setText(Utility.formatAuthorAndTimeAgo(mContext,
                mCursor.getString(ArticleFragment.IND_USER_NAME), mCursor.getLong(ArticleFragment.IND_CREATED_AT)));
        if (mCursor.getInt(ArticleFragment.IND_SPOTLIGHTED) == 1){
            holder.mSpotLightTV.setVisibility(View.VISIBLE);
        } else{
            holder.mSpotLightTV.setVisibility(View.INVISIBLE);
        }
        String image = mCursor.getString(ArticleFragment.IND_IMAGE);
        if (TextUtils.isEmpty(image)){
            holder.mImageView.setVisibility(View.GONE);
        } else{
            holder.mImageView.setVisibility(View.VISIBLE);
            Picasso.with(mContext).load(image).into(holder.mImageView);
        }
        holder.mTitleTV.setText(mCursor.getString(ArticleFragment.IND_TITLE));
        holder.mTagTV.setText(mCursor.getString(ArticleFragment.IND_RANDOM_TAG));
    }

    public void swapCursor(Cursor cursor){
        mCursor = cursor;
        notifyDataSetChanged();
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

        ArticleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mCursor.moveToPosition(getAdapterPosition());
            mArticleClickHandler.onArticleClick(mCursor.getLong(ArticleFragment.IND_ARTICLE_ID), this);
        }
    }

}
