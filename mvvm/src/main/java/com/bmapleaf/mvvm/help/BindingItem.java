package com.bmapleaf.mvvm.help;

import android.content.Context;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.util.SparseArray;

/**
 * Created by ZhangMing on 2017/08/03.
 */

public class BindingItem<T> {
    public static final int VAR_NONE = 0;
    private static final int VAR_INVALID = -1;
    private static final int LAYOUT_NONE = 0;
    private final OnItemBind<T> onItemBind;
    private int variableId;
    @LayoutRes
    private int layoutRes;
    private SparseArray<Object> extraBindings;

    private BindingItem(OnItemBind<T> onItemBind) {
        this.onItemBind = onItemBind;
    }

    public static <T> BindingItem<T> of(OnItemBind<T> onItemBind) {
        if (onItemBind == null) {
            throw new NullPointerException("onItemBind == null");
        }
        return new BindingItem<>(onItemBind);
    }

    public static <T> BindingItem<T> of(int variableId, @LayoutRes int layoutRes) {
        return new BindingItem<T>(null).set(variableId, layoutRes);
    }

    public final BindingItem<T> set(int variableId, @LayoutRes int layoutRes) {
        this.variableId = variableId;
        this.layoutRes = layoutRes;
        return this;
    }

    public final BindingItem<T> variableId(int variableId) {
        this.variableId = variableId;
        return this;
    }

    public final BindingItem<T> layoutRes(@LayoutRes int layoutRes) {
        this.layoutRes = layoutRes;
        return this;
    }

    public final BindingItem<T> bindExtra(int variableId, Object value) {
        if (extraBindings == null) {
            extraBindings = new SparseArray<>(1);
        }
        extraBindings.put(variableId, value);
        return this;
    }

    public final BindingItem<T> clearExtras() {
        if (extraBindings != null) {
            extraBindings.clear();
        }
        return this;
    }

    public BindingItem<T> removeExtra(int variableId) {
        if (extraBindings != null) {
            extraBindings.remove(variableId);
        }
        return this;
    }

    public final int variableId() {
        return variableId;
    }

    @LayoutRes
    public final int layoutRes() {
        return layoutRes;
    }

    public final Object extraBinding(int variableId) {
        if (extraBindings == null) {
            return null;
        }
        return extraBindings.get(variableId);
    }

    public void onItemBind(int position, T item) {
        if (onItemBind != null) {
            variableId = VAR_INVALID;
            layoutRes = LAYOUT_NONE;
            onItemBind.onItemBind(this, position, item);
            if (variableId == VAR_INVALID) {
                throw new IllegalStateException("variableId not set in onItemBind()");
            }
            if (layoutRes == LAYOUT_NONE) {
                throw new IllegalStateException("layoutRes not set in onItemBind()");
            }
        }
    }

    public boolean bind(ViewDataBinding binding, T item) {
        if (variableId == VAR_NONE) {
            return false;
        }
        boolean result = binding.setVariable(variableId, item);
        if (!result) {
            throwMissingVariable(binding, variableId, layoutRes);
        }
        if (extraBindings != null) {
            for (int i = 0, size = extraBindings.size(); i < size; i++) {
                int variableId = extraBindings.keyAt(i);
                Object value = extraBindings.valueAt(i);
                if (variableId != VAR_NONE) {
                    binding.setVariable(variableId, value);
                }
            }
        }
        return true;
    }

    private static void throwMissingVariable(ViewDataBinding binding, int bindingVariable, @LayoutRes int layoutRes) {
        Context context = binding.getRoot().getContext();
        Resources resources = context.getResources();
        String layoutName = resources.getResourceName(layoutRes);
        String bindingVariableName = DataBindingUtil.convertBrIdToString(bindingVariable);
        throw new IllegalStateException("Could not bind variable '" + bindingVariableName + "' in layout '" + layoutName + "'");
    }

    public interface OnItemBind<T> {
        void onItemBind(BindingItem bindingItem, int position, T item);
    }
}
