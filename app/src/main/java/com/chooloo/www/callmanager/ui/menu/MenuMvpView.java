package com.chooloo.www.callmanager.ui.menu;

import com.chooloo.www.callmanager.ui.base.MvpView;
import com.chooloo.www.callmanager.adapter.MenuAdapter.OnMenuItemClickListener;

interface MenuMvpView extends MvpView {
    void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener);
}
