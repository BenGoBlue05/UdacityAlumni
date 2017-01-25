package com.google.developer.udacityalumni.fragment;

import android.support.v4.app.Fragment;

//Credit to Firebase's Database Sample

public class PostFragment extends Fragment {

    private static final String LOG_TAG = PostFragment.class.getSimpleName();

//    private DatabaseReference mDatabase;
//    private FirebaseRecyclerAdapter<Post, PostViewHolder> mAdapter;
//    private RecyclerView mRecycler;
//    private LinearLayoutManager mManager;
//
//    public PostFragment() {
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View rootView =  inflater.inflate(R.layout.fragment_post, container, false);
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//        mRecycler = (RecyclerView) rootView.findViewById(R.id.post_rv);
//        mRecycler.setHasFixedSize(true);
//        return  rootView;
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        mManager = new LinearLayoutManager(getActivity());
//        mManager.setReverseLayout(true);
//        mManager.setStackFromEnd(true);
//        mRecycler.setLayoutManager(mManager);
//
//        Query query = mDatabase.child("post");
//    }

}
