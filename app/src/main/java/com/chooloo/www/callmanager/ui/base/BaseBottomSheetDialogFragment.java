package com.chooloo.www.callmanager.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.FragmentManager;

import com.chooloo.www.callmanager.databinding.FragmentBottomDialogBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import static com.chooloo.www.callmanager.util.PermissionUtils.RC_DEFAULT;

public abstract class BaseBottomSheetDialogFragment extends BottomSheetDialogFragment implements MvpView {

    private boolean mIsShown = false;

    protected String[] mRequiredPermissions;

    protected BaseActivity mActivity;

    protected FragmentBottomDialogBinding binding;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            this.mActivity = (BaseActivity) context;
            mActivity.onAttachFragment(this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBottomDialogBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRequiredPermissions = onGetPermissions();
        binding.bottomDialogFragmentCloseButton.setOnClickListener(view1 -> dismiss());
        onSetup();
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
            super.dismiss();
            this.mIsShown = false;
        }
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
        askForPermissions(mRequiredPermissions, RC_DEFAULT);
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

    protected void putFragment(BaseFragment fragment) {
        getChildFragmentManager().beginTransaction().replace(binding.bottomDialogFragmentPlaceholder.getId(), fragment).commit();
    }

    protected Bundle getArgsSafely() {
        Bundle args = super.getArguments();
        if (args == null) {
            throw new IllegalArgumentException("You must create this fragment with newInstance()");
        }
        return args;
    }

    public boolean isShown() {
        return mIsShown;
    }
}
