package com.chooloo.www.callmanager.ui.dialpad;

import android.os.Bundle;

import com.chooloo.www.callmanager.ui.base.BaseBottomSheetDialogFragment;

public class DialpadBottomDialogFragment extends BaseBottomSheetDialogFragment {

    public static final String ARG_DIALER = "is_dialer";

    public DialpadFragment mDialpadFragment;

    public static DialpadBottomDialogFragment newInstance(boolean isDialer) {
        Bundle args = new Bundle();
        args.putBoolean(ARG_DIALER, isDialer);
        DialpadBottomDialogFragment fragment = new DialpadBottomDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSetup() {
        mDialpadFragment = DialpadFragment.newInstance(getArgsSafely().getBoolean(ARG_DIALER));
        putFragment(mDialpadFragment);
    }
}
