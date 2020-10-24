package com.chooloo.www.callmanager.ui.activity.call;

import com.chooloo.www.callmanager.ui.base.BaseContract;

public interface CallContract extends BaseContract {
    interface View extends BaseContract.View {

    }

    interface Presenter<V extends View> extends BaseContract.Presenter<V> {

    }
}
