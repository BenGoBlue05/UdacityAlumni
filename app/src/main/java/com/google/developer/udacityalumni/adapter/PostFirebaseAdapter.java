package com.google.developer.udacityalumni.adapter;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.developer.udacityalumni.R;
import com.google.developer.udacityalumni.model.Post;
import com.google.developer.udacityalumni.viewholder.PostViewHolder;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;

/**
 * Created by Tom Calver on 28/01/17.
 */

public final class PostFirebaseAdapter extends FirebaseRecyclerAdapter<Post, PostViewHolder> {

    private final OnClickListener mListener;

    public PostFirebaseAdapter(Query query, LifecycleOwner owner, OnClickListener listener) {
        super(new FirebaseRecyclerOptions.Builder<Post>()
                .setQuery(query, Post.class)
                .setLifecycleOwner(owner)
                .build());
        mListener = listener;
    }

    @Override
    protected void onBindViewHolder(PostViewHolder holder, int position, final Post model) {
        final Context ctx = holder.itemView.getContext();
        final FirebaseStorage storage = FirebaseStorage.getInstance();

        holder.bindToPost(model, ctx, storage);
        holder.setAvatarOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAvatarClicked(model);
            }
        });
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PostViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false));
    }

    public interface OnClickListener {
        void onAvatarClicked(Post post);
    }

}
