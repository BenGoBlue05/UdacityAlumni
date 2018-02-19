package com.google.developer.udacityalumni.app;

import android.databinding.BindingAdapter;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.developer.udacityalumni.BR;
import com.google.developer.udacityalumni.R;
import com.google.developer.udacityalumni.activity.GlideApp;
import com.google.developer.udacityalumni.base.BaseRecyclerAdapter;
import com.google.developer.udacityalumni.databinding.ItemAppBinding;

/**
 * Created by benjaminlewis on 2/9/18.
 */

public class AppAdapter extends BaseRecyclerAdapter<App, AppAdapter.ViewHolder> {

    public AppAdapter() {
        super();
    }

    @BindingAdapter("userPhotoUrl")
    public static void setUserPhotoUrl(ImageView imageView, String url) {
        GlideApp.with(imageView.getContext())
                .load(url)
                .placeholder(R.drawable.ic_person)
                .into(imageView);
    }

    @BindingAdapter("photoUrl")
    public static void setPhotoContentUrl(ImageView imageView, String url) {
        GlideApp.with(imageView.getContext())
                .load(url)
                .into(imageView);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(ItemAppBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(getModel(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ViewDataBinding binding;

        public ViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(App app) {
            binding.setVariable(BR.app, app);
            binding.executePendingBindings();
        }
    }
}
