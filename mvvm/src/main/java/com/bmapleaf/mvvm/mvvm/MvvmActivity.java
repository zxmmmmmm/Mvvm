package com.bmapleaf.mvvm.mvvm;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bmapleaf.mvvm.Interface.IMessageObserver;
import com.bmapleaf.mvvm.Interface.IViewModel;
import com.bmapleaf.mvvm.R;
import com.bmapleaf.mvvm.help.Utils;

/**
 * Created by ZhangMing on 2017/07/31.
 */

public abstract class MvvmActivity<VM extends IViewModel> extends AppCompatActivity implements IMessageObserver {
    private static final String titleExtraName = "MvvmActivityTitle";
    protected final String TAG = getClass().getName();
    protected RelativeLayout mContent;
    private View rootView;
    private Toolbar mToolbar;
    private VM mViewModel;
    private ViewDataBinding binding;
    private TextView mTitle;

    protected abstract VM buildViewModel();

    protected final VM getViewModel() {
        return mViewModel;
    }

    protected final Toolbar getToolbar() {
        return mToolbar;
    }

    protected final <T extends ViewDataBinding> T getBinding() {
        if (null == binding && null == (binding = DataBindingUtil.getBinding(rootView))) {
            binding = DataBindingUtil.bind(rootView);
        }
        return Utils.cast(binding);
    }

    protected final <V extends View> V $(@IdRes int id) {
        return Utils.getView(this, id);
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(getBaseActivityLayout());

        mViewModel = buildViewModel();
        if (null != mViewModel) {
            mViewModel.setContext(this);
            mViewModel.bindMessageObserver(this);
        }

        mContent = $(R.id.content);

        mToolbar = $(R.id.toolbar);
        if (null != mToolbar) {
            setSupportActionBar(mToolbar);
            if (isDisplayHomeAsUpEnabled()) {
                ActionBar actionBar = getSupportActionBar();
                if (null != actionBar) {
                    actionBar.setDisplayHomeAsUpEnabled(true);
                }
            }
            mTitle = Utils.getView(mToolbar, R.id.title);
        }

        String title = getIntent().getStringExtra(titleExtraName);
        if (null != title && !title.isEmpty()) {
            setTitle(title);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        if (null != mTitle) {
            super.setTitle(null);
            mTitle.setText(title);
        } else {
            super.setTitle(title);
        }
    }

    protected boolean isDisplayHomeAsUpEnabled() {
        return true;
    }

    /**
     * @return base layout resId, must contain a viewGroup(android:id="@+id/content")
     */
    @LayoutRes
    protected int getBaseActivityLayout() {
        return R.layout.activity_base_dark;
    }

    @Override
    public final void setContentView(@LayoutRes int layoutResID) {
        //super.setContentView(layoutResID);
        setContentView(View.inflate(this, layoutResID, null));
    }

    @Override
    public final void setContentView(View view) {
        //super.setContentView(view);
        setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public final void setContentView(View view, ViewGroup.LayoutParams params) {
        //super.setContentView(view, params);
        if (null == mContent) {
            throw new RuntimeException("base layout must contain a viewGroup(android:id=\"@+id/content\")");
        }
        int childCount = mContent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            mContent.removeView(mContent.getChildAt(i));
        }
        addContentView(view, params);
    }

    @Override
    public final void addContentView(View view, ViewGroup.LayoutParams params) {
        //super.addContentView(view, params);
        mContent.addView(rootView = view, params);
    }

    @Override
    protected void onDestroy() {
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

    protected final void onOptionsHomeSelected() {
        Intent upIntent = NavUtils.getParentActivityIntent(this);
        if (null == upIntent) {
            finish();
        } else {
            if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                TaskStackBuilder.create(this)
                        .addNextIntentWithParentStack(upIntent)
                        .startActivities();
            } else {
                upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                NavUtils.navigateUpTo(this, upIntent);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onOptionsHomeSelected();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showLoading() {
        Log.d(TAG, "showLoading: ");
    }

    @Override
    public void showLoading(String msg) {
        Log.d(TAG, "showLoading: " + msg);
    }

    @Override
    public void showLoading(String msg, int progress) {
        Log.d(TAG, "showLoading: " + msg + "/" + progress);
    }

    @Override
    public void hideLoading() {
        Log.d(TAG, "hideLoading: ");
    }

    @Override
    public void showMsg(String msg) {
        Log.d(TAG, "showMsg: " + msg);
    }

    @Override
    public void showError(String msg) {
        Log.d(TAG, "showError: " + msg);
    }

    @Override
    public void showError(String msg, String content) {
        Log.d(TAG, "showError: " + msg + "/" + content);
    }

    @Override
    public void doExtra1(Object extra) {
        Log.d(TAG, "doExtra1: " + extra);
    }

    @Override
    public void doExtras(Object... extras) {
        Log.d(TAG, "doExtras: " + extras.length + "/" + extras);
    }
}
