package com.chooloo.www.callmanager.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.ui.fragment.base.AbsBaseFragment;
import com.chooloo.www.callmanager.viewmodels.SharedSearchViewModel;

import butterknife.BindView;

public class SearchBarFragment extends AbsBaseFragment {

    SharedSearchViewModel mSearchViewModel;

    Context mContext;

    private ViewGroup.LayoutParams mInputParams;
    // Text watcher (watches the text as the user writes)
    private TextWatcher mTextWatcher;

    public @BindView(R.id.search_input) EditText mSearchInput;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_bar, container, false);
    }

    @Override
    protected void onFragmentReady() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mInputParams = mSearchInput.getLayoutParams();

        mSearchViewModel = ViewModelProviders.of(getActivity()).get(SharedSearchViewModel.class);

        mTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mSearchViewModel.setText(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        mSearchInput.addTextChangedListener(mTextWatcher);

        mSearchInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mSearchViewModel.setIsFocused(true);
                } else {
                    mSearchViewModel.setIsFocused(false);
                }
            }
        });
    }

    public void setFocus() {
        mSearchInput.requestFocus();
    }

}
