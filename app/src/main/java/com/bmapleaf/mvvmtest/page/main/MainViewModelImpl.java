package com.bmapleaf.mvvmtest.page.main;

import android.databinding.ObservableInt;

/**
 * Created by ZhangMing on 2017/08/10.
 */

public class MainViewModelImpl extends MainInterface.MainViewModel {
    public final ObservableInt count = new ObservableInt();

    @Override
    protected MainInterface.MainModel buildModel() {
        return new MainModelImpl();
    }

    @Override
    protected void initObservables() {
        super.initObservables();
        count.set(0);
    }

    @Override
    void doTest() {
        addSubscribe(
                getModel().doTest(count.get()),
                new DataListener<Integer>() {
                    @Override
                    protected void onSuccess(Integer integer) {
                        count.set(integer);
                    }
                }
        );
    }
}