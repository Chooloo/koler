package com.chooloo.www.callmanager.ui.dialpad;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chooloo.www.callmanager.databinding.FragmentDialpadBinding;
import com.chooloo.www.callmanager.ui.base.BaseBottomSheetDialogFragment;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomDialpadFragment extends BaseBottomSheetDialogFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return FragmentDialpadBinding.inflate(inflater).getRoot();
    }

    @Override
    public void onSetup() {

    }
}
