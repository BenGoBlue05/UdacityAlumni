package com.google.developer.udacityalumni.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.developer.udacityalumni.R;
import com.google.developer.udacityalumni.model.Post;
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

    public void bindToPost(Post post, Context context){
        if (post != null){
            if (post.text != null && !post.text.isEmpty()) mTextTv.setText(post.text);
            if (post.userName != null && !post.userName.isEmpty()) mUserNameTv.setText(post.userName);
//            TODO: Add TimeAgo
//            TODO: Add Comments
//            TODO: Add OnClickListener
            if (post.userProfPic != null && !post.userProfPic.isEmpty()) {
                Picasso.with(context).load(post.userProfPic).placeholder(R.drawable.placeholder)
                        .error(R.drawable.ic_person).into(mProfPicIv);
            } else{
                mProfPicIv.setImageResource(R.drawable.ic_person);
            }
            if (post.photoUrl != null && !post.photoUrl.isEmpty()){
                mProfPicIv.setVisibility(View.VISIBLE);
                Picasso.with(context).load(post.photoUrl)
                        .placeholder(R.drawable.placeholder).into(mImageIv);
            }
        } else{
            Log.i(LOG_TAG, "Post is null");
        }
    }
}
