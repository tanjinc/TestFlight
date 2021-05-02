package com.jacktan.common.recyclerview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jacktan.common.recyclerview.base.ItemViewDelegate;
import com.jacktan.common.recyclerview.base.ItemViewDelegateManager;

import java.util.ArrayList;
import java.util.List;

public class MultiItemTypeAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected Context mContext;
    protected List<T> mDataArray = new ArrayList<>();
    protected ItemViewDelegateManager mItemViewDelegateManager;

    public MultiItemTypeAdapter(Context context) {
        this(context, new ArrayList<T>());
    }

    public MultiItemTypeAdapter(Context context, List<T> data) {
        super();
        mDataArray.clear();
        mDataArray.addAll(data);
        mContext = context;
        mItemViewDelegateManager = new ItemViewDelegateManager();
        notifyDataSetChanged();
    }

    public void setData(List<T> data) {
        mDataArray.clear();
        mDataArray.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        ItemViewDelegate itemViewDelegate = mItemViewDelegateManager.getItemViewDelegate(viewType);
        if (itemViewDelegate != null) {
            View view = LayoutInflater.from(mContext).inflate(itemViewDelegate.getItemViewLayoutId(), viewGroup, false);
            return mItemViewDelegateManager.getItemViewDelegate(viewType).createViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        mItemViewDelegateManager.bind(viewHolder, mDataArray.get(i), viewHolder.getAdapterPosition());
    }

    @Override
    public int getItemViewType(int position) {
        return mItemViewDelegateManager.getItemViewType(mDataArray.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mDataArray != null ? mDataArray.size() : 0;
    }


    public void addViewDelegate(ItemViewDelegate delegate) {
        mItemViewDelegateManager.addDelegate(delegate);
    }

    public void addViewDelegate(int type, ItemViewDelegate delegate) {
        mItemViewDelegateManager.addDelegate(type, delegate);
    }
}
