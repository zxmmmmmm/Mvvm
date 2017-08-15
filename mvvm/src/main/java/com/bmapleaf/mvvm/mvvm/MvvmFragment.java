package com.bmapleaf.mvvm.mvvm;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bmapleaf.mvvm.Interface.IMessageObserver;
import com.bmapleaf.mvvm.Interface.IViewModel;
import com.bmapleaf.mvvm.help.Utils;

/**
 * Created by ZhangMing on 2017/07/25.
 */

public abstract class MvvmFragment<VM extends IViewModel> extends Fragment implements IMessageObserver {
    private static final String titleExtraName = "MvvmActivityTitle";
    protected final String TAG = getClass().getName();
    private View rootView;
    private VM mViewModel;
    private ViewDataBinding binding;

    protected abstract VM buildViewModel();

    protected final VM getViewModel() {
        return mViewModel;
    }

    protected final <T extends ViewDataBinding> T getBinding() {
        if (null == binding && null == (binding = DataBindingUtil.getBinding(rootView))) {
            binding = DataBindingUtil.bind(rootView);
        }
        return Utils.cast(binding);
    }

    protected final void showFragment(@IdRes int containerViewId, MvvmFragment fragment, boolean addToBackStack) {
        showFragment(getFragmentManager(), containerViewId, fragment, addToBackStack);
    }

    protected final void showChildFragment(@IdRes int containerViewId, MvvmFragment fragment, boolean addToBackStack) {
        showFragment(getChildFragmentManager(), containerViewId, fragment, addToBackStack);
    }

    protected void showFragment(FragmentManager fragmentManager, @IdRes int containerViewId, MvvmFragment fragment, boolean addToBackStack) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment, fragment.getClass().getName());
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(fragment.getClass().getName());
        }
        fragmentTransaction.commit();
    }

    protected final <T extends MvvmFragment> MvvmFragment findFragmentByTag(Class<T> fragmentClass) {
        String tag = fragmentClass.getName();
        return findFragmentByTag(tag);
    }

    protected final MvvmFragment findFragmentByTag(String tag) {
        MvvmFragment fragment = (MvvmFragment) getFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            fragment = (MvvmFragment) getChildFragmentManager().findFragmentByTag(tag);
        }
        return fragment;
    }

    protected final <T extends MvvmFragment> boolean isFragmentExist(Class<T> fragmentClass) {
        return findFragmentByTag(fragmentClass) != null;
    }

    protected final boolean isFragmentExist(String tag) {
        return findFragmentByTag(tag) != null;
    }

    protected final void startActivityWidthTitle(Intent intent, CharSequence title) {
        if (null == intent) {
            return;
        }
        if (null != title) {
            intent.putExtra(titleExtraName, title);
        }
        super.startActivity(intent);
    }

    protected final void startActivityForResultWidthTitle(Intent intent, int requestCode, CharSequence title) {
        if (null == intent) {
            return;
        }
        if (null != title) {
            intent.putExtra(titleExtraName, title);
        }
        super.startActivityForResult(intent, requestCode);
    }

    @LayoutRes
    protected abstract int getLayoutRes();

    protected void initView() {
        //Log.d(TAG, "initView: ");
    }

    /**
     * called when back to fragment
     */
    protected void rootViewNoNull() {

    }

    protected void beforeInitView() {

    }

    protected void afterInitView() {

    }

    @Override
    public final void onStart() {
        super.onStart();
        if (!(getActivity() instanceof MvvmActivity)) {
            throw new RuntimeException("host activity must extends MvvmActivity");
        }
    }

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        beforeInitView();
        if (rootView == null) {
            mViewModel = buildViewModel();
            if (null != mViewModel) {
                mViewModel.setContext(getContext());
                mViewModel.bindMessageObserver(this);
            }
            rootView = inflater.inflate(getLayoutRes(), container, false);
            initView();
        } else {
            rootViewNoNull();
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        afterInitView();
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyViewModel();
        unbindView();
    }

    private void unbindView() {
        if (null != binding) {
            binding.unbind();
            binding = null;
        }
    }

    private void destroyViewModel() {
        if (null != mViewModel) {
            mViewModel.destroy();
            mViewModel = null;
        }
    }

    public boolean onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            //Log.d(TAG, "onBackPressed: popBackStack");
            getFragmentManager().popBackStack();
            return true;
        }
        return false;
    }

    protected final <T extends View> T $(@IdRes int id) {
        if (rootView != null) {
            return Utils.getView(rootView, id);
        } else {
            return null;
        }
    }

    private MvvmActivity getBaseActivity() {
        return Utils.cast(getActivity());
    }

    @Override
    public void showLoading() {
        getBaseActivity().showLoading();
    }

    @Override
    public void showLoading(String msg) {
        getBaseActivity().showLoading(msg);
    }

    @Override
    public void showLoading(String msg, int progress) {
        getBaseActivity().showLoading(msg, progress);
    }

    @Override
    public void hideLoading() {
        getBaseActivity().hideLoading();
    }

    @Override
    public void showError(String msg) {
        getBaseActivity().showError(msg);
    }

    @Override
    public void showError(String msg, String content) {
        getBaseActivity().showError(msg, content);
    }

    @Override
    public void showMsg(String msg) {
        getBaseActivity().showMsg(msg);
    }

    @Override
    public void doExtra1(Object extra) {
        getBaseActivity().doExtra1(extra);
    }

    @Override
    public void doExtras(Object... extras) {
        getBaseActivity().doExtras(extras);
    }
}
