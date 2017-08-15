package com.bmapleaf.mvvm.help;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bmapleaf.mvvm.mvvm.MvvmAdapter;

/**
 * Created by ZhangMing on 2017/08/07.
 */

public class RecyclerViewModel<T> {
    public final ObservableList<T> items;
    public final MvvmAdapter<T> adapter;
    public final OnItemClickListener<T> listener;
    public final BindingItem<T> bindingItem;
    public ObservableField<RecyclerView.ItemDecoration> itemDecoration;
    public ObservableField<RecyclerView.LayoutManager> layoutManager;

    public RecyclerViewModel(int itemVariableId, @LayoutRes int itemLayoutResId, MvvmAdapter<T> adapter, OnItemClickListener<T> listener, int listenerVariableId) {
        this.items = new ObservableArrayList<>();
        this.adapter = null == adapter ? new MvvmAdapter<T>() : adapter;
        this.listener = listener;
        this.bindingItem = BindingItem.<T>of(itemVariableId, itemLayoutResId);
        if (null != listener) {
            this.bindingItem.bindExtra(listenerVariableId, listener);
        }
        this.itemDecoration = new ObservableField<>();
        this.layoutManager = new ObservableField<>();
    }

    public RecyclerViewModel(int itemVariableId, @LayoutRes int itemLayoutResId, OnItemClickListener<T> listener, int listenerVariableId) {
        this(itemVariableId, itemLayoutResId, null, listener, listenerVariableId);
    }

    public void setItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        this.itemDecoration.set(itemDecoration);
    }

    public interface OnItemClickListener<T> {
        void onItemClick(View view, T item);
    }
}
