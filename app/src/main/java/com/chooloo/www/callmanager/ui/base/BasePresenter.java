package com.chooloo.www.callmanager.ui.base;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;

public class BasePresenter<V extends BaseContract.View> implements BaseContract.Presenter<V>, LifecycleObserver {

    protected Context mContext;
    protected V mView;
    protected Lifecycle mLifecycle;

    public void subscribe(@NonNull Context context) {
        this.mContext = context;
    }

    public void bind(BaseContract.View view, Lifecycle lifecycle) {
        this.mView = (V) view;
        this.mLifecycle = lifecycle;
        mLifecycle.addObserver(this);
    }

    public void unbind() {
        this.mView = null;
    }

}
