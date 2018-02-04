package com.google.developer.udacityalumni.post;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.developer.udacityalumni.BR;
import com.google.developer.udacityalumni.R;
import com.google.developer.udacityalumni.activity.GlideApp;
import com.google.developer.udacityalumni.databinding.PostItemBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by benjaminlewis on 1/9/18.
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private List<Post> posts;

    public PostAdapter() {
        posts = new ArrayList<>();
    }

    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(PostItemBinding.inflate(inflater, parent, false));

    }

    @Override
    public void onBindViewHolder(PostAdapter.ViewHolder holder, int position) {
        holder.bind(posts.get(position));
    }

    @Override
    public int getItemCount() {
        return posts == null ? 0 : posts.size();
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }

    public void addPosts(List<Post> posts) {
        this.posts.addAll(posts);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ViewDataBinding binding;
        private final TextView timeAgoTextView;
        private Context context;

        ViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            context = binding.getRoot().getContext();
            timeAgoTextView = binding.getRoot().findViewById(R.id.timeTextView);
        }

        public void bind(Post post) {
            binding.setVariable(BR.post, post);
            binding.executePendingBindings();
        }
    }

    private String getTimeAgo(@NonNull Context context, long timeStamp){
        return DateUtils.formatDateTime(context, timeStamp, DateUtils.FORMAT_SHOW_TIME);
    }

    @BindingAdapter("userProfPicUrl")
    public static void setProfPicUrl(ImageView imageView, String url) {
        Context context = imageView.getContext();
        GlideApp.with(context)
                .load(url)
                .placeholder(R.drawable.ic_person)
                .into(imageView);
    }

    @BindingAdapter("photoContentUrl")
    public static void setPhotoContentUrl(ImageView imageView, String url) {
        Context context = imageView.getContext();
        GlideApp.with(context)
                .load(url)
                .into(imageView);
    }




}
