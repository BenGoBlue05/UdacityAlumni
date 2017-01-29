package com.google.developer.udacityalumni.viewholder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.developer.udacityalumni.R;
import com.google.developer.udacityalumni.model.Post;
import com.google.developer.udacityalumni.view.slidingview.AvatarCardAdapter;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


public class PostViewHolder extends RecyclerView.ViewHolder{

    private static final String LOG_TAG = PostViewHolder.class.getSimpleName();

    @BindView(R.id.item_post_text)
    TextView mTextTv;
    @BindView(R.id.item_post_image)
    ImageView mImageIv;
    @BindView(R.id.item_post_user_name)
    TextView mUserNameTv;
    @BindView(R.id.item_post_prof_pic)
    CircleImageView mProfPicIv;
    @BindView(R.id.item_post_time_ago)
    TextView mTimeAgoTv;

    public PostViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindToPost(@Nullable Post post, @NonNull Context context, @NonNull FirebaseStorage storage){

        if (post == null) {
            return;
        }

        final String text = post.text;
        final String userName = post.userName;
        final String userProfilePic = post.userProfPic;
        final String photoUrl = post.photoUrl;

        if (!TextUtils.isEmpty(text)) {
            mTextTv.setText(text);
        }

        if(!TextUtils.isEmpty(userName)) {
            mUserNameTv.setText(userName);
        }

        Picasso.with(context)
                .load(userProfilePic)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(mProfPicIv);

        if(!TextUtils.isEmpty(photoUrl)) {
            Log.i(LOG_TAG, photoUrl);
            final StorageReference reference = storage.getReferenceFromUrl(photoUrl);
            Glide.with(context)
                    .using(new FirebaseImageLoader())
                    .load(reference)
                    .into(mImageIv);
        }

        //TODO: Add TimeAgo
        //TODO: Add Comments
        //TODO: Add OnClickListener

    }

    public void setAvatarOnClickListener(View.OnClickListener listener) {
        mProfPicIv.setOnClickListener(listener);
    }

}
