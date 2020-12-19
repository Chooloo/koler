package com.chooloo.www.callmanager.ui.menu;

import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.databinding.FragmentMenuBinding;
import com.chooloo.www.callmanager.ui.base.BaseBottomSheetDialogFragment;
import com.chooloo.www.callmanager.ui.menu.MenuAdapter.OnMenuItemClickListener;

import butterknife.BindView;

public class MenuFragment extends BaseBottomSheetDialogFragment implements MenuMvpView {

    private static final String ARG_MENU_LAYOUT = "menu_layout";

    private FragmentMenuBinding binding;
    protected Menu mMenu;
    private MenuAdapter mAdapter;
    private OnMenuItemClickListener mOnMenuItemClickListener;

    public static MenuFragment newInstance(@MenuRes int menuLayout) {
        Bundle args = new Bundle();
        args.putInt(ARG_MENU_LAYOUT, menuLayout);
        MenuFragment menuFragment = new MenuFragment();
        menuFragment.setArguments(args);
        return menuFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMenuBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void setUp() {
        mMenu = new PopupMenu(mActivity, null).getMenu();
        mActivity.getMenuInflater().inflate(getArgsSafely().getInt(ARG_MENU_LAYOUT), mMenu);
        mAdapter = new MenuAdapter(mActivity, mMenu);
        mAdapter.setOnMenuItemClickListener((menuItem) -> {
            if (mOnMenuItemClickListener != null) {
                mOnMenuItemClickListener.onMenuItemClick(menuItem);
            }
        });
        binding.menuRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        mOnMenuItemClickListener = onMenuItemClickListener;
    }
}
