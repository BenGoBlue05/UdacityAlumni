package com.google.developer.udacityalumni.adapter;

import android.content.Context;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.developer.udacityalumni.R;
import com.google.developer.udacityalumni.model.Post;
import com.google.developer.udacityalumni.viewholder.PostViewHolder;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;

/**
 *
 * Created by Tom Calver on 28/01/17.
 */

public final class PostFirebaseAdapter extends FirebaseRecyclerAdapter<Post, PostViewHolder> {

    private final OnClickListener mListener;

    public PostFirebaseAdapter(Query query, OnClickListener listener) {
        super(Post.class, R.layout.item_post, PostViewHolder.class, query);
        mListener = listener;
    }

    @Override
    protected void populateViewHolder(final PostViewHolder viewHolder, final Post post, int position) {

        final Context ctx = viewHolder.itemView.getContext();
        final FirebaseStorage storage = FirebaseStorage.getInstance();

        viewHolder.bindToPost(post, ctx, storage);
        viewHolder.setAvatarOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAvatarClicked(post);
            }
        });
    }

    public interface OnClickListener {
        void onAvatarClicked(Post post);
    }

}
