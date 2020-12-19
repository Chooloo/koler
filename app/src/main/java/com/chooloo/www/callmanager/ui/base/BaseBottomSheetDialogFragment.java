package com.chooloo.www.callmanager.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public abstract class BaseBottomSheetDialogFragment extends BottomSheetDialogFragment implements MvpView {

    private static final int PERMISSION_RC = 10;

    private boolean mIsShown = false;

    protected String[] mRequiredPermissions;

    protected BaseActivity mActivity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            this.mActivity = (BaseActivity) context;
            mActivity.onAttachFragment(this);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRequiredPermissions = getPermissions();
        setUp();
    }

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        if (!mIsShown) {
            super.show(manager, tag);
            this.mIsShown = true;
        }
    }

    @Override
    public void dismiss() {
        if (mIsShown) {
            dismiss();
            this.mIsShown = false;
        }
    }

    @Override
    public String[] getPermissions() {
        return new String[]{};
    }

    @Override
    public boolean hasPermission(String permission) {
        return false;
    }

    @Override
    public boolean hasPermissions() {
        for (String permission : mRequiredPermissions) {
            if (!mActivity.hasPermission(permission)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void askForPermissions() {
        requestPermissions(mRequiredPermissions, PERMISSION_RC);
    }

    @Override
    public void showMessage(String message) {
        if (mActivity != null) {
            mActivity.showMessage(message);
        }
    }

    @Override
    public void showMessage(@StringRes int stringResId) {
        if (mActivity != null) {
            mActivity.showMessage(stringResId);
        }
    }

    @Override
    public void showError(String message) {
        if (mActivity != null) {
            mActivity.showError(message);
        }
    }

    @Override
    public void showError(@StringRes int stringResId) {
        if (mActivity != null) {
            mActivity.showError(getString(stringResId));
        }
    }

    protected Bundle getArgsSafely() {
        Bundle args = super.getArguments();
        if (args == null) {
            throw new IllegalArgumentException("You must create this fragment with newInstance()");
        }
        return args;
    }

    public boolean ismIsShown() {
        return mIsShown;
    }
}
