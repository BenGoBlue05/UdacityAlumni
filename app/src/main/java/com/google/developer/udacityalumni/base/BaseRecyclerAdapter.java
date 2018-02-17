package com.google.developer.udacityalumni.base;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by benjaminlewis on 2/7/18.
 */

public abstract class BaseRecyclerAdapter<M, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected List<M> models;

    public BaseRecyclerAdapter() {
        this(new ArrayList<>());
    }

    public BaseRecyclerAdapter(List<M> models) {
        this.models = models;
    }

    @Override
    public int getItemCount() {
        return models == null ? 0 : models.size();
    }

    public void setModels(List<M> models) {
        this.models = models;
        notifyDataSetChanged();
    }

    public void addModel(M model) {
        this.models.add(model);
        notifyDataSetChanged();
    }

    public void addModels(List<M> models) {
        this.models.addAll(models);
        notifyDataSetChanged();
    }

    protected M getModel(int position) {
        return models.get(position);
    }
}
