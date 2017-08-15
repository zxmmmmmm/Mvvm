package com.bmapleaf.mvvm.mvvm;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableList;
import android.databinding.OnRebindCallback;
import android.databinding.ViewDataBinding;
import android.os.Looper;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;


import com.bmapleaf.mvvm.help.BindingItem;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by ZhangMing on 2017/08/03.
 */

public class MvvmAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected final String TAG = getClass().getSimpleName();
    private static final Object DATA_INVALIDATION = new Object();
    private LayoutInflater inflater;
    private BindingItem<T> bindingItem;
    private ViewHolderFactory viewHolderFactory;
    private RecyclerView recyclerView;
    private List<T> items;
    private ItemIds<? super T> itemIds;
    private ObservableListChangedCallback<T> callback;

    public final void setViewHolderFactory(@Nullable ViewHolderFactory viewHolderFactory) {
        this.viewHolderFactory = viewHolderFactory;
    }

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (null == inflater) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        ViewDataBinding binding = onCreateBinding(inflater, viewType, parent);
        final RecyclerView.ViewHolder holder = onCreateViewHolder(binding);
        binding.addOnRebindCallback(new OnRebindCallback() {
            @Override
            public boolean onPreBind(ViewDataBinding binding) {
                return recyclerView != null && recyclerView.isComputingLayout();
            }

            @Override
            public void onCanceled(ViewDataBinding binding) {
                if (recyclerView == null || recyclerView.isComputingLayout()) {
                    return;
                }
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    notifyItemChanged(position, DATA_INVALIDATION);
                }
            }
        });
        return holder;
    }

    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        T item = items.get(position);
        ViewDataBinding binding = DataBindingUtil.getBinding(holder.itemView);
        onBindBinding(binding, bindingItem.variableId(), bindingItem.layoutRes(), position, item);
    }

    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        if (isForDataBinding(payloads)) {
            ViewDataBinding binding = DataBindingUtil.getBinding(holder.itemView);
            binding.executePendingBindings();
        } else {
            super.onBindViewHolder(holder, position, payloads);
        }
    }

    @Override
    public final int getItemViewType(int position) {
        bindingItem.onItemBind(position, items.get(position));
        return bindingItem.layoutRes();
    }

    @Override
    public final void onAttachedToRecyclerView(RecyclerView recyclerView) {
        if (isDataIsObserableList()) {
            addObservableListCallback();
        }
        this.recyclerView = recyclerView;
    }

    @Override
    public final void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        if (isDataIsObserableList()) {
            removeObservableListCallback();
        }
        this.recyclerView = null;
    }

    @Override
    public final int getItemCount() {
        return null == items ? 0 : items.size();
    }

    @Override
    public final long getItemId(int position) {
        return null == itemIds ? position : itemIds.getItemId(position, items.get(position));
    }

    public final int getPosition(T item) {
        return items.indexOf(item);
    }

    public final void setItemIds(ItemIds<? super T> itemIds) {
        if (itemIds != this.itemIds) {
            this.itemIds = itemIds;
            setHasStableIds(null != itemIds);
        }
    }

    public final BindingItem<T> getBindingItem() {
        return bindingItem;
    }

    public final void setBindingItem(BindingItem<T> bindingItem) {
        this.bindingItem = bindingItem;
    }

    public final void setItems(@Nullable List<T> items) {
        if (items == this.items) {
            return;
        }
        this.items = items;
        if (null == recyclerView) {
            if (this.items instanceof ObservableList) {
                removeObservableListCallback();
            }
            if (items instanceof ObservableList) {
                addObservableListCallback();
            }
        }
        notifyDataSetChanged();
    }

    public final T getItem(int position) {
        return items.get(position);
    }

    private void addObservableListCallback() {
        if (null == this.items) {
            return;
        }
        callback = new ObservableListChangedCallback<>(this, (ObservableList<T>) items);
        ((ObservableList<T>) this.items).addOnListChangedCallback(callback);
    }

    private void removeObservableListCallback() {
        if (null == this.items) {
            return;
        }
        ((ObservableList<T>) this.items).removeOnListChangedCallback(callback);
        callback = null;
    }

    protected ViewDataBinding onCreateBinding(LayoutInflater inflater, @LayoutRes int layoutRes, ViewGroup viewGroup) {
        return DataBindingUtil.inflate(inflater, layoutRes, viewGroup, false);
    }

    protected void onBindBinding(ViewDataBinding binding, int variableId, @LayoutRes int layoutRes, int position, T item) {
        if (bindingItem.bind(binding, item)) {
            binding.executePendingBindings();
        }
    }

    private boolean isDataIsObserableList() {
        return null != this.recyclerView && null != items && items instanceof ObservableList;
    }

    private boolean isForDataBinding(List<Object> payloads) {
        if (null == payloads || payloads.isEmpty()) {
            return false;
        }
        for (int i = 0, size = payloads.size(); i < size; i++) {
            Object obj = payloads.get(i);
            if (DATA_INVALIDATION != obj) {
                return false;
            }
        }
        return true;
    }

    private RecyclerView.ViewHolder onCreateViewHolder(ViewDataBinding binding) {
        if (null != viewHolderFactory) {
            return viewHolderFactory.createViewHolder(binding);
        } else {
            return new BindingViewHolder(binding);
        }
    }

    public interface ItemIds<T> {
        long getItemId(int position, T item);
    }

    public interface ViewHolderFactory {
        RecyclerView.ViewHolder createViewHolder(ViewDataBinding binding);
    }

    private static class BindingViewHolder extends RecyclerView.ViewHolder {
        public BindingViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
        }
    }

    private static class ObservableListChangedCallback<T> extends ObservableList.OnListChangedCallback<ObservableList<T>> {
        private WeakReference<MvvmAdapter<T>> adapterWRef;

        public ObservableListChangedCallback(MvvmAdapter<T> adapter, ObservableList<T> items) {
            adapterWRef = AdapterReferenceCollector.createRef(adapter, items, this);
        }

        private static void ensureChangeOnMainThread() {
            if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
                throw new IllegalStateException("You must only modify the ObservableList on the main thread.");
            }
        }

        @Override
        public void onChanged(ObservableList<T> sender) {
            MvvmAdapter<T> adapter = getAdapter();
            if (null == adapter) {
                return;
            }
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(ObservableList<T> sender, int positionStart, int itemCount) {
            MvvmAdapter<T> adapter = getAdapter();
            if (null == adapter) {
                return;
            }
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeInserted(ObservableList<T> sender, int positionStart, int itemCount) {
            MvvmAdapter<T> adapter = getAdapter();
            if (null == adapter) {
                return;
            }
            adapter.notifyDataSetChanged();
            adapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(ObservableList<T> sender, int fromPosition, int toPosition, int itemCount) {
            MvvmAdapter<T> adapter = getAdapter();
            if (null == adapter) {
                return;
            }
            for (int i = 0; i < itemCount; i++) {
                adapter.notifyItemMoved(fromPosition + i, toPosition + i);
            }
        }

        @Override
        public void onItemRangeRemoved(ObservableList<T> sender, int positionStart, int itemCount) {
            MvvmAdapter<T> adapter = getAdapter();
            if (null == adapter) {
                return;
            }
            adapter.notifyDataSetChanged();
            adapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

        private MvvmAdapter<T> getAdapter() {
            MvvmAdapter<T> adapter = adapterWRef.get();
            if (adapter != null) {
                ensureChangeOnMainThread();
            }
            return adapter;
        }
    }

    private static class AdapterReferenceCollector {
        static final ReferenceQueue<Object> QUEUE = new ReferenceQueue<>();
        static PollReferenceThread thread;

        static <T, A extends MvvmAdapter<T>> AdapterWRef<T, A> createRef(A adapter, ObservableList<T> items, ObservableList.OnListChangedCallback<ObservableList<T>> callback) {
            if (thread == null || !thread.isAlive()) {
                thread = new PollReferenceThread();
                thread.start();
            }
            return new AdapterWRef<>(adapter, items, callback);
        }

        private static class PollReferenceThread extends Thread {
            @Override
            public void run() {
                while (true) {
                    try {
                        Reference<?> ref = QUEUE.remove();
                        if (ref instanceof AdapterWRef) {
                            ((AdapterWRef) ref).unregister();
                        }
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        }

        static class AdapterWRef<T, A extends MvvmAdapter<T>> extends WeakReference<A> {
            private final ObservableList<T> items;
            private final ObservableList.OnListChangedCallback<ObservableList<T>> callback;

            AdapterWRef(A referent, ObservableList<T> items, ObservableList.OnListChangedCallback<ObservableList<T>> callback) {
                super(referent, QUEUE);
                this.items = items;
                this.callback = callback;
            }

            void unregister() {
                items.removeOnListChangedCallback(callback);
            }
        }
    }
}
