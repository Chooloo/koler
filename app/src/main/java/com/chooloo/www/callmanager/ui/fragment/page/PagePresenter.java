package com.chooloo.www.callmanager.ui.fragment.page;

import com.chooloo.www.callmanager.ui.base.BasePresenter;
import com.chooloo.www.callmanager.ui.activity.main.MainActivity;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class PagePresenter<V extends PageContract.View> extends BasePresenter<V> implements PageContract.Presenter<V> {

    @Override
    public void onDialNumberChanged(String number) {
    }

    @Override
    public void onSearchTextChanged(String text) {
    }

    @Override
    public void onLeftClick() {
        ((MainActivity) mView.getActivity()).setBottomSheetState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onRightClick() {
        ((MainActivity) mView.getActivity()).toggleSearchBar();
    }
}
