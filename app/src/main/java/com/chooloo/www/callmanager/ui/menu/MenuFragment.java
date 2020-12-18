package com.chooloo.www.callmanager.ui.menu;

import android.os.Bundle;
import android.view.Menu;
import android.widget.PopupMenu;

import androidx.annotation.LayoutRes;
import androidx.annotation.MenuRes;

import com.chooloo.www.callmanager.ui.base.BaseBottomSheetDialogFragment;

public class MenuFragment extends BaseBottomSheetDialogFragment implements MenuMvpView {

    private static final String ARG_MENU_LAYOUT = "menu_layout";

    protected Menu mMenu;
    private MenuAdapter mAdapter;

    public static MenuFragment newInstance(@MenuRes int menuLayout) {
        Bundle args = new Bundle();
        args.putInt(ARG_MENU_LAYOUT, menuLayout);
        MenuFragment menuFragment = new MenuFragment();
        menuFragment.setArguments(args);
        return menuFragment;
    }

    @Override
    public void setUp() {
        mMenu = new PopupMenu(mActivity, null).getMenu();
        mActivity.getMenuInflater().inflate(getArgsSafely().getInt(ARG_MENU_LAYOUT), mMenu);
        mAdapter = new MenuAdapter(mActivity, mMenu);
    }

    @Override
    public void setOnMenuItemClickListener(MenuAdapter.OnMenuItemClickListener onMenuItemClickListener) {
        mAdapter.setOnMenuItemClickListener(onMenuItemClickListener);
    }
}
