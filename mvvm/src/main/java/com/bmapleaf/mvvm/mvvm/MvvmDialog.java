package com.bmapleaf.mvvm.mvvm;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by ZhangMing on 2016/11/28.
 */

public abstract class MvvmDialog extends DialogFragment {
    private View rootView;

    @LayoutRes
    protected abstract int getLayoutRes();

    protected void initView(View rootView) {

    }

    protected void rootViewNoNull() {

    }

    protected void beforeInitView() {

    }

    protected float getXPercentage() {
        return 0;
    }

    protected float getYPercentage() {
        return 0;
    }

    protected int getGravity() {
        return Gravity.CENTER;
    }

    protected float getHorizontalMargin() {
        return 0;
    }

    protected float getVerticalMargin() {
        return 0;
    }

    protected float getDimAmount() {
        return 0.6f;
    }

    @Nullable
    protected DialogInterface.OnKeyListener getOnKeyListener() {
        return null;
    }

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        beforeInitView();
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutRes(), container, false);
            initView(rootView);
        } else {
            rootViewNoNull();
        }
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setOnKeyListener(getOnKeyListener());
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    public final void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            /*background*/
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams attributes = dialog.getWindow().getAttributes();
            attributes.dimAmount = getDimAmount();
            if (getWindowAnimations() != 0) {
                attributes.windowAnimations = getWindowAnimations();
            }

            /*size & position*/
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();

            layoutParams.gravity = getGravity();
            layoutParams.horizontalMargin = getHorizontalMargin();
            layoutParams.verticalMargin = getVerticalMargin();

            int width = getXPercentage() != 0 ? (int) (dm.widthPixels * getXPercentage()) : layoutParams.width;
            int height = getYPercentage() != 0 ? (int) (dm.heightPixels * getYPercentage()) : layoutParams.height;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @StyleRes
    protected int getWindowAnimations() {
        return 0;
    }
}
