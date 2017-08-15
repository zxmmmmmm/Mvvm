package com.bmapleaf.mvvm.Interface;

/**
 * Created by ZhangMing on 2017/07/31.
 */

public interface IMessageObserver {
    void showLoading();

    void showLoading(String msg);

    void showLoading(String msg, int progress);

    void hideLoading();

    void showMsg(String msg);

    void showError(String msg);

    void showError(String msg, String content);

    void doExtra1(Object extra);

    void doExtras(Object... extras);
}
