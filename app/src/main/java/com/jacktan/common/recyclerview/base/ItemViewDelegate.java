package com.jacktan.common.recyclerview.base;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public interface ItemViewDelegate<T, V extends RecyclerView.ViewHolder> {

    public abstract int getItemViewLayoutId();
    public abstract boolean isForViewType(T data, int p);
    public abstract void bind(V viewHolder, T data, int position);
    public V createViewHolder(View view);
}
