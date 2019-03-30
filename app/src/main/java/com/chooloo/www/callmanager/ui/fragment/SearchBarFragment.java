package com.chooloo.www.callmanager.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.ui.fragment.base.BaseFragment;
import com.chooloo.www.callmanager.util.Utilities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Optional;

public class SearchBarFragment extends BaseFragment {

    SharedSearchViewModel mSearchViewModel;

    Context mContext;

    private ViewGroup.LayoutParams mInputParams;
    // Text watcher (watches the text as the user writes)
    private TextWatcher mTextWatcher;

    public @BindView(R.id.search_input) EditText mSearchInput;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreateView() {
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
//                    final float scale = getContext().getResources().getDisplayMetrics().density;
//                    int pixels = (int) (45 * scale + 0.5f);
//                    mInputParams.height = pixels;
//                    mInputParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
//                    mSearchInput.setLayoutParams(mInputParams);
                } else {
                    mSearchViewModel.setIsFocused(false);
//                    final float scale = getContext().getResources().getDisplayMetrics().density;
//                    int pixels = (int) (30 * scale + 0.5f);
//                    mInputParams.height = pixels;
//                    mSearchInput.setLayoutParams(mInputParams);
                }
            }
        });
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_search_bar;
    }

    public void setFocus() {
        mSearchInput.requestFocus();
    }
}
