package com.chooloo.www.callmanager.ui.fragment.page;

import android.app.Activity;

public class PageContract implements BaseContract {
    interface View extends BaseContract.View {
        void setUp();

        Activity getActivity();
    }

    interface Presenter<V extends View> extends BaseContract.Presenter<V> {
        void onDialNumberChanged(String number);

        void onSearchTextChanged(String text);

        void onLeftClick();

        void onRightClick();
    }
}
