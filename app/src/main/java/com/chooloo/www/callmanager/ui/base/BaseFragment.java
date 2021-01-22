package com.chooloo.www.callmanager.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.jetbrains.annotations.NotNull;

import kotlin.TypeCastException;

public abstract class BaseFragment extends Fragment implements MvpView {

    private static final int PERMISSION_RC = 10;
    protected String[] mRequiredPermissions;
    protected BaseActivity mActivity;
    protected ViewModelProvider mViewModelProvider;

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            this.mActivity = (BaseActivity) context;
            mActivity.onAttachFragment(this);
            mViewModelProvider = mActivity.mViewModelProvider;
        } else {
            throw new TypeCastException("Fragment not a child of base activity");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRequiredPermissions = onGetPermissions();
        onSetup();
    }

    @Override
    public String[] onGetPermissions() {
        return new String[]{};
    }

    @Override
    public boolean hasPermission(String permission) {
        return false;
    }

    @Override
    public boolean hasPermissions() {
        if (mRequiredPermissions == null) {
            return false;
        }
        for (String permission : mRequiredPermissions) {
            if (!mActivity.hasPermission(permission)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void askForPermission(String permission, int requestCode) {
        askForPermissions(new String[]{permission}, requestCode);
    }

    @Override
    public void askForPermissions(String[] permissions, int requestCode) {
        requestPermissions(permissions, requestCode);
    }

    @Override
    public void askForPermissions() {
        askForPermissions(mRequiredPermissions, PERMISSION_RC);
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

}
