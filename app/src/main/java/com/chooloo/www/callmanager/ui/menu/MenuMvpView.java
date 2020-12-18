package com.chooloo.www.callmanager.ui.menu;

import android.view.MenuItem;

import com.chooloo.www.callmanager.ui.base.MvpView;

interface MenuMvpView extends MvpView {
    void setOnMenuItemClickListener(MenuAdapter.OnMenuItemClickListener onMenuItemClickListener);
}
