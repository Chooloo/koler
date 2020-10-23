package com.chooloo.www.callmanager.ui2.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.ui2.fragment.base.AbsBaseFragment;
import com.chooloo.www.callmanager.util.Utilities;

import butterknife.BindView;

public class SearchBarFragment extends AbsBaseFragment {

    private boolean mIsToggled = false;

    OnFocusChangedListener mOnFocusChangedListener;
    OnTextChangedListener mOnTextChangedListener;

    private ViewGroup.LayoutParams mInputParams;

    public @BindView(R.id.search_input) EditText mSearchInput;

    public SearchBarFragment() {
        mOnFocusChangedListener = isFocused -> {
        };

        mOnTextChangedListener = text -> {
        };
    }

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
        mSearchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mOnTextChangedListener.onTextChanged(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mSearchInput.setOnFocusChangeListener((v, hasFocus) -> mOnFocusChangedListener.onFocusChanged(hasFocus));
    }

    public String getText() {
        return String.valueOf(mSearchInput.getText());
    }

    public void setFocus() {
        mSearchInput.requestFocus();
    }

    /**
     * Toggles the search bar according to it's current state
     */
    public void toggleSearchBar(boolean isShow) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);
        if (isShow) {
            mIsToggled = true;
            ft.show(this);
            setFocus();
            Utilities.toggleKeyboard(getContext(), mSearchInput, true);
        } else {
            mIsToggled = false;
            ft.hide(this);
            Utilities.toggleKeyboard(getContext(), mSearchInput, false);
        }
        ft.commit();
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
