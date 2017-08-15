package com.bmapleaf.mvvm.mvvm;

import android.content.Context;

import com.bmapleaf.mvvm.Interface.IModel;


/**
 * Created by ZhangMing on 2017/07/31.
 */

public abstract class MvvmModel implements IModel {
    protected final String TAG = getClass().getName();
    private Context context;

    @Override
    public void setContext(Context context) {
        this.context = context;
    }

    protected final Context getContext() {
        return context;
    }
}
