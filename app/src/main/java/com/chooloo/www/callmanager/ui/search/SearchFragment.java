package com.chooloo.www.callmanager.ui.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.ui.base.BaseFragment;

import butterknife.BindView;

public class SearchFragment extends BaseFragment implements SearchMvpView {

    OnFocusChangedListener mOnFocusChangedListener;
    OnTextChangedListener mOnTextChangedListener;

    private SearchPresenter<SearchMvpView> mPresenter;

    public @BindView(R.id.search_input) EditText mEditText;

    public static SearchFragment newInstance() {
        Bundle args = new Bundle();
        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_bar, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDetach();
    }

    @Override
    public void setUp() {
        mPresenter = new SearchPresenter<>();
        mPresenter.onAttach(this, getLifecycle());

        mOnFocusChangedListener = isFocused -> {
        };
        mOnTextChangedListener = text -> {
        };

        mEditText.addTextChangedListener(new TextWatcher() {
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
        mEditText.setOnFocusChangeListener((v, hasFocus) -> {
            mPresenter.onFocusChanged(hasFocus);
            mOnFocusChangedListener.onFocusChanged(hasFocus);
        });
    }

    @Override
    public String getText() {
        return String.valueOf(mEditText.getText());
    }

    @Override
    public void setFocus() {
        mEditText.requestFocus();
    }

    @Override
    public void showIcon(boolean isShow) {
        mEditText.setCompoundDrawablesWithIntrinsicBounds(isShow ? ContextCompat.getDrawable(getContext(), R.drawable.ic_search_black_24dp) : null, null, null, null);
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
