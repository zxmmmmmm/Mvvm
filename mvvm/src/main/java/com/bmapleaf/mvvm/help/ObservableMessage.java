package com.bmapleaf.mvvm.help;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.Observable;

import com.bmapleaf.mvvm.BR;
import com.bmapleaf.mvvm.Interface.IMessageObserver;

/**
 * Created by ZhangMing on 2017/08/04.
 */

public class ObservableMessage extends BaseObservable implements IMessageObserver {
    @Bindable
    @MessageType
    private int type;
    private String msg;
    private String content;
    private int progress;
    private Object extra;
    private Object[] extras;

    public static Observable.OnPropertyChangedCallback bind(BaseObservable observable, final IMessageObserver observer) {
        if (null == observable || null == observer) {
            return null;
        }
        Observable.OnPropertyChangedCallback callback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (sender instanceof ObservableMessage) {
                    ObservableMessage observable = (ObservableMessage) sender;
                    switch (observable.getType()) {
                        case MessageType.MSG_ERROR1:
                            observer.showError(observable.getMsg());
                            break;
                        case MessageType.MSG_ERROR2:
                            observer.showError(observable.getMsg(), observable.getContent());
                            break;
                        case MessageType.MSG_LOADING0:
                            observer.showLoading();
                            break;
                        case MessageType.MSG_LOADING1:
                            observer.showLoading(observable.getMsg());
                            break;
                        case MessageType.MSG_LOADING2:
                            observer.showLoading(observable.getMsg(), observable.getProgress());
                            break;
                        case MessageType.MSG_MSG1:
                            observer.showMsg(observable.getMsg());
                            break;
                        case MessageType.MSG_EXTRA1:
                            observer.doExtra1(observable.getExtra());
                            break;
                        case MessageType.MSG_EXTRAS:
                            observer.doExtras(observable.getExtras());
                            break;
                        case MessageType.MSG_LOADING3:
                            observer.hideLoading();
                            break;
                        case MessageType.MSG_UNKNOWN:
                            break;
                    }
                    observable.clear();
                }
            }
        };
        observable.addOnPropertyChangedCallback(callback);
        return callback;
    }

    public static void unbind(BaseObservable observable, Observable.OnPropertyChangedCallback callback) {
        if (null == observable || null == callback) {
            return;
        }
        observable.removeOnPropertyChangedCallback(callback);
    }

    private Object getExtra() {
        return extra;
    }

    private Object[] getExtras() {
        return extras;
    }

    @MessageType
    private int getType() {
        return type;
    }

    private String getMsg() {
        return msg;
    }

    private String getContent() {
        return content;
    }

    private int getProgress() {
        return progress;
    }

    private void execute() {
        notifyPropertyChanged(BR.type);
    }

    private void clear() {
        type = MessageType.MSG_UNKNOWN;
        msg = null;
        content = null;
        progress = 0;
        extra = null;
        extras = null;
    }

    @Override
    public void showLoading() {
        this.type = MessageType.MSG_LOADING0;
        execute();
    }

    @Override
    public void showLoading(String msg) {
        this.type = MessageType.MSG_LOADING1;
        this.msg = msg;
        execute();
    }

    @Override
    public void showLoading(String msg, int progress) {
        this.type = MessageType.MSG_LOADING2;
        this.msg = msg;
        this.progress = progress;
        execute();
    }

    @Override
    public void hideLoading() {
        this.type = MessageType.MSG_LOADING3;
        execute();
    }

    @Override
    public void showMsg(String msg) {
        this.type = MessageType.MSG_MSG1;
        this.msg = msg;
        execute();
    }

    @Override
    public void showError(String msg) {
        this.type = MessageType.MSG_ERROR1;
        this.msg = msg;
        execute();
    }

    @Override
    public void showError(String msg, String content) {
        this.type = MessageType.MSG_ERROR2;
        this.msg = msg;
        this.content = content;
        execute();
    }

    @Override
    public void doExtra1(Object extra) {
        this.type = MessageType.MSG_EXTRA1;
        this.extra = extra;
        execute();
    }

    @Override
    public void doExtras(Object... extras) {
        this.type = MessageType.MSG_EXTRAS;
        this.extras = extras;
        execute();
    }
}
