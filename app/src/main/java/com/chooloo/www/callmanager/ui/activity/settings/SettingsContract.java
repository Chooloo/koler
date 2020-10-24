package com.chooloo.www.callmanager.ui.activity.settings;

import com.chooloo.www.callmanager.ui.base.BaseContract;

public class SettingsContract implements BaseContract {

    interface View extends BaseContract.View {
        void setUp();
    }

    interface Presenter<V extends View> extends BaseContract.Presenter<V> {

    }
}
