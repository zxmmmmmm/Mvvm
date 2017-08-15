package com.bmapleaf.mvvmtest.page.main;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by ZhangMing on 2017/08/10.
 */

class MainModelImpl extends MainInterface.MainModel {
    @Override
    Observable<Integer> doTest(int num) {
        return Observable.just(num)
                .map(new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(@NonNull Integer integer) throws Exception {
                        return ++integer;
                    }
                });
    }
}