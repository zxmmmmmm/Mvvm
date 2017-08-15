package com.bmapleaf.mvvmtest.page.main;

import android.os.Bundle;

import com.bmapleaf.mvvmtest.BR;
import com.bmapleaf.mvvmtest.R;
import com.bmapleaf.mvvmtest.databinding.ActivityMainBinding;

/**
 * Created by ZhangMing on 2017/08/10.
 */
public class MainActivity extends MainInterface.MainView {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = getBinding();
        binding.setVariable(BR.viewModel, getViewModel());
        binding.setVariable(BR.view, this);
    }

    @Override
    protected MainInterface.MainViewModel buildViewModel() {
        return new MainViewModelImpl();
    }

    @Override
    public void onTest() {
        getViewModel().doTest();
    }
}