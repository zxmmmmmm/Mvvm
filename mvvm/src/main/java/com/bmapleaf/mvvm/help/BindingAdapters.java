package com.bmapleaf.mvvm.help;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bmapleaf.mvvm.mvvm.MvvmAdapter;

import java.util.List;

/**
 * Created by ZhangMing on 2017/08/03.
 */

public class BindingAdapters {
    @BindingAdapter("android:src")
    public static void setSrc(ImageView view, Bitmap bitmap) {
        view.setImageBitmap(bitmap);
    }


    @BindingAdapter("android:src")
    public static void setSrc(ImageView view, @DrawableRes int resId) {
        view.setImageResource(resId);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter(value = {"bindingItem", "items", "adapter", "itemIds", "viewHolder"}, requireAll = false)
    public static <T> void setAdapter(RecyclerView recyclerView,
                                      BindingItem<T> bindingItem,
                                      List<T> items,
                                      MvvmAdapter<T> adapter,
                                      MvvmAdapter.ItemIds<? super T> itemIds,
                                      MvvmAdapter.ViewHolderFactory viewHolderFactory) {
        if (bindingItem == null) {
            throw new IllegalArgumentException("bindingItem must not be null");
        }
        MvvmAdapter oldAdapter = (MvvmAdapter) recyclerView.getAdapter();
        if (adapter == null) {
            if (oldAdapter == null) {
                adapter = new MvvmAdapter<>();
            } else {
                adapter = oldAdapter;
            }
        }
        adapter.setBindingItem(bindingItem);
        adapter.setItems(items);
        adapter.setItemIds(itemIds);
        adapter.setViewHolderFactory(viewHolderFactory);
        if (oldAdapter != adapter) {
            recyclerView.setAdapter(adapter);
        }
    }

    @BindingAdapter("itemDecoration")
    public static void setItemDecoration(RecyclerView recyclerView, RecyclerView.ItemDecoration itemDecoration) {
        if (null == itemDecoration) {
            return;
        }
        recyclerView.addItemDecoration(itemDecoration);
    }

    @BindingAdapter("layoutManager")
    public static void setLayoutManager(RecyclerView recyclerView, LayoutManagers.Factory factory) {
        recyclerView.setLayoutManager(factory.create(recyclerView));
    }
}