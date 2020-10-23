package com.chooloo.www.callmanager.ui.base;

import android.content.Context;

import androidx.lifecycle.Lifecycle;

public interface BaseContract {
    interface View {
    }

    interface Presenter<V extends View> {
        void bind(V view, Lifecycle lifecycle);

        void unbind();

        void subscribe(Context context);
    }
}
