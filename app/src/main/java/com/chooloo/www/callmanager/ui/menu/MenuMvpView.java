package com.chooloo.www.callmanager.ui.menu;

import android.view.MenuItem;

import com.chooloo.www.callmanager.ui.base.MvpView;
import com.chooloo.www.callmanager.ui.menu.MenuAdapter.OnMenuItemClickListener;

interface MenuMvpView extends MvpView {
    void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener);
}
