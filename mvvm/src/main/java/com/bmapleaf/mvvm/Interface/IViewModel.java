package com.bmapleaf.mvvm.Interface;

import android.content.Context;

import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * Created by ZhangMing on 2017/07/31.
 */

public interface IViewModel {
    void setContext(Context context);

    <T> void addSubscribe(Observable<T> observable, Observer<T> observer);

    void cancel();

    void bindMessageObserver(IMessageObserver observer);

    void destroy();
}
