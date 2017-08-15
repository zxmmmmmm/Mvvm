package com.bmapleaf.mvvm.mvvm;

import android.content.Context;
import android.databinding.Observable;
import android.support.annotation.StringRes;

import com.bmapleaf.mvvm.Interface.IMessageObserver;
import com.bmapleaf.mvvm.Interface.IModel;
import com.bmapleaf.mvvm.Interface.IViewModel;
import com.bmapleaf.mvvm.help.ObservableMessage;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by ZhangMing on 2017/07/31.
 */

public abstract class MvvmViewModel<M extends IModel> implements IViewModel {
    protected final String TAG = getClass().getName();
    private M mModel;
    private Context mContext;
    private ObservableMessage observableMessage;
    private Observable.OnPropertyChangedCallback messageCallback;
    protected CompositeDisposable compositeDisposable;

    protected final ObservableMessage getObservableMessage() {
        return observableMessage;
    }

    protected final Context getContext() {
        return mContext;
    }

    @Override
    public final void setContext(Context context) {
        mContext = context;
        mModel = buildModel();
        mModel.setContext(mContext);
        initObservables();
    }

    @Override
    public final void bindMessageObserver(IMessageObserver observer) {
        if (null != messageCallback) {
            ObservableMessage.unbind(observableMessage, messageCallback);
            messageCallback = null;
        }
        messageCallback = ObservableMessage.bind(observableMessage, observer);
    }

    protected void initObservables() {
        observableMessage = new ObservableMessage();
    }

    protected final M getModel() {
        return mModel;
    }

    protected abstract M buildModel();

    @Override
    public final void destroy() {
        cancel();
        mModel = null;
        mContext = null;
        ObservableMessage.unbind(observableMessage, messageCallback);
        observableMessage = null;
        messageCallback = null;
    }

    @Override
    public final <T> void addSubscribe(io.reactivex.Observable<T> observable, Observer<T> observer) {
        if (null == observable || null == observer) {
            return;
        }
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public final void cancel() {
        if (null != compositeDisposable) {
            compositeDisposable.dispose();
            compositeDisposable.clear();
            compositeDisposable = null;
        }
    }

    protected final String getString(@StringRes int resId) {
        return getContext().getString(resId);
    }

    protected abstract class DataListener<T> implements Observer<T> {
        @Override
        public final void onSubscribe(@NonNull Disposable d) {
            if (null == compositeDisposable) {
                compositeDisposable = new CompositeDisposable();
            }
            compositeDisposable.add(d);
            onStart();
        }

        @Override
        public final void onNext(@NonNull T t) {
            onSuccess(t);
        }

        @Override
        public void onError(@NonNull Throwable e) {
            e.printStackTrace();
            onFailure(e.getLocalizedMessage());
            onFinished();
        }

        @Override
        public final void onComplete() {
            onFinished();
        }

        protected abstract void onSuccess(T t);

        protected void onFailure(String error) {
            getObservableMessage().showError(error);
        }

        protected void onStart() {
            getObservableMessage().showLoading();
        }

        protected void onFinished() {
            getObservableMessage().hideLoading();
        }
    }
}
