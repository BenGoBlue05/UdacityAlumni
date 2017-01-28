package com.google.developer.udacityalumni.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.developer.udacityalumni.R;
import com.google.developer.udacityalumni.adapter.PostFirebaseAdapter;
import com.google.developer.udacityalumni.model.Post;
import com.google.developer.udacityalumni.view.slidingview.AvatarCardAdapter;
import com.google.developer.udacityalumni.view.slidingview.SlidingViewManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

//Credit to Firebase's Database Sample

public class PostFragment extends Fragment implements PostFirebaseAdapter.OnClickListener {

    private static final String LOG_TAG = PostFragment.class.getSimpleName();

    private DatabaseReference mDatabase;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    private PostFirebaseAdapter mAdapter;

    private SlidingViewManager mSlidingViewManager;
    private AvatarCardAdapter mSlidingViewAdapter;

    public PostFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_post, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mRecycler = (RecyclerView) rootView.findViewById(R.id.post_rv);
        mRecycler.setHasFixedSize(true);

        mSlidingViewManager = new SlidingViewManager(this);
        mSlidingViewAdapter = new AvatarCardAdapter(
                new AvatarCardAdapter.SimpleOnClickListener(mSlidingViewManager)
        );
        mSlidingViewManager.setAdapter(mSlidingViewAdapter);
        if (savedInstanceState != null) {
            mSlidingViewManager.onRestoreInstanceState(savedInstanceState);
        }

        return  rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        final Query query = mDatabase.child("posts").limitToFirst(100);
        mAdapter = new PostFirebaseAdapter(query, this);
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mSlidingViewManager.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        mAdapter.cleanup();
    }

    @Override
    public void onAvatarClicked(Post post) {
        //todo Replace 'Content' with User Bio
        mSlidingViewAdapter.setContent(post.text);
        mSlidingViewAdapter.setImageUri(Uri.parse(post.userProfPic));
        mSlidingViewAdapter.setName(post.userName);
        mSlidingViewManager.animate();
    }

}
