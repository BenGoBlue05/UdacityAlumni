package com.google.developer.udacityalumni.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.developer.udacityalumni.R;
import com.google.developer.udacityalumni.fragment.ArticleFragment;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by benjaminlewis on 1/3/17.
 */

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
        holder.mTitle.setText(mCursor.getString(ArticleFragment.IND_TITLE));
        Picasso.with(mContext).load(mCursor.getString(ArticleFragment.IND_IMAGE)).into(holder.mImageView);

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
        @BindView(R.id.item_image)
        ImageView mImageView;
        @BindView(R.id.item_title)
        TextView mTitle;

        public ArticleViewHolder(View itemView) {
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
