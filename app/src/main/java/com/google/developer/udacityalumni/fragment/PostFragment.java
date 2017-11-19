package com.google.developer.udacityalumni.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    private SlidingViewManager mSlidingViewManager;
    private AvatarCardAdapter mSlidingViewAdapter;

    public PostFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_post, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mRecycler = rootView.findViewById(R.id.post_rv);
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
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        mRecycler.setLayoutManager(manager);

        final Query query = mDatabase.child("posts").limitToFirst(100);
        PostFirebaseAdapter adapter = new PostFirebaseAdapter(query, getActivity(), this);
        mRecycler.setAdapter(adapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mSlidingViewManager.onSaveInstanceState(outState);
    }

    @Override
    public void onAvatarClicked(Post post) {
        mSlidingViewAdapter.setContent(post.userBio);
        mSlidingViewAdapter.setImageUri(post.userProfPic);
        mSlidingViewAdapter.setName(post.userName);
        mSlidingViewManager.animate();
    }

}
