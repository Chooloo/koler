package com.chooloo.www.callmanager.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.chooloo.www.callmanager.util.Utilities;

import org.jetbrains.annotations.NotNull;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment implements MvpView {

    protected String[] mRequiredPermissions;
    protected BaseActivity mActivity;

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            this.mActivity = (BaseActivity) context;
            mActivity.onAttachFragment(this);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, getView());
        setUp();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
    public void setRequiredPermissions(String[] permissions) {
        this.mRequiredPermissions = permissions;
    }

    @Override
    public void askForPermissions() {
        if (mActivity != null) {
            mActivity.askForPermissions();
        }
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

    protected abstract void setUp();
}
