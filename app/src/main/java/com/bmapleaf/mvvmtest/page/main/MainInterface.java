package com.bmapleaf.mvvmtest.page.main;

import com.bmapleaf.mvvmtest.base.BaseActivity;
import com.bmapleaf.mvvmtest.base.BaseModel;
import com.bmapleaf.mvvmtest.base.BaseViewModel;

import io.reactivex.Observable;

/**
 * Created by ZhangMing on 2017/08/10.
 */

interface MainInterface {
    abstract class MainModel extends BaseModel {
        abstract Observable<Integer> doTest(int num);
    }

    abstract class MainView extends BaseActivity<MainViewModel> {
        public abstract void onTest();
    }

    abstract class MainViewModel extends BaseViewModel<MainModel> {
        abstract void doTest();
    }
}