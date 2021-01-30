package com.chooloo.www.callmanager.ui.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.databinding.FragmentSearchBarBinding;
import com.chooloo.www.callmanager.ui.base.BaseFragment;

public class SearchFragment extends BaseFragment implements SearchMvpView {

    OnFocusChangedListener mOnFocusChangedListener;
    OnTextChangedListener mOnTextChangedListener;

    private SearchMvpPresenter<SearchMvpView> mPresenter;
    private FragmentSearchBarBinding binding;

    public static SearchFragment newInstance() {
        Bundle args = new Bundle();
        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchBarBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.attach(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detach();
    }

    @Override
    public void onSetup() {
        mPresenter = new SearchPresenter<>();
        mPresenter.attach(this);

        mOnFocusChangedListener = isFocused -> {
        };
        mOnTextChangedListener = text -> {
        };

        binding.searchInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPresenter.onTextChanged(String.valueOf(s));
                mOnTextChangedListener.onTextChanged(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        binding.searchInputEditText.setOnFocusChangeListener((v, hasFocus) -> {
            mPresenter.onFocusChanged(hasFocus);
            mOnFocusChangedListener.onFocusChanged(hasFocus);
        });
    }

    @Override
    public String getText() {
        return binding.searchInputEditText.getText().toString();
    }

    @Override
    public void setFocus() {
        binding.searchInputEditText.requestFocus();
    }

    @Override
    public void showIcon(boolean isShow) {
//        binding.searchInputEditText.icon
//        binding.searchInputLayout.setStartIconVisible(isShow);
        binding.searchInputEditText.setCompoundDrawablesWithIntrinsicBounds(isShow ? ContextCompat.getDrawable(_activity, R.drawable.ic_search_black_24dp) : null, null, null, null);
    }

    public void setOnFocusChangedListener(OnFocusChangedListener onFocusChangedListener) {
        this.mOnFocusChangedListener = onFocusChangedListener;
    }

    public void setOnTextChangedListener(OnTextChangedListener onTextChangedListener) {
        this.mOnTextChangedListener = onTextChangedListener;
    }

    public interface OnFocusChangedListener {
        void onFocusChanged(boolean isFocused);
    }

    public interface OnTextChangedListener {
        void onTextChanged(String text);
    }
}
