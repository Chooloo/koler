package com.chooloo.www.callmanager.ui.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chooloo.www.callmanager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DialpadKeyButton extends FrameLayout {

    private int mKeyCode;
    private String mNumber;
    private String mLetters;

    @BindView(R.id.dialpad_key_number) TextView mNumberView;
    @Nullable @BindView(R.id.dialpad_key_letters) TextView mLettersView;
    @Nullable @BindView(R.id.dialpad_key_icon) ImageView mIconView;

    private OnPressedListener mOnPressedListener;

    public DialpadKeyButton(Context context, String number, String letters, int keyCode) {
        super(context, null);
        this.setKeyCode(keyCode);
        this.setLetters(letters);
        this.setNumber(number);
    }

    public DialpadKeyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
        if (mOnPressedListener != null) {
            mOnPressedListener.onPressed(this, pressed);
        }
    }

    public void setNumber(String number) {
        mNumber = number;
        mNumberView.setText(number);
    }

    public String getNumber() {
        return mNumber;
    }

    public void setLetters(String letters) {
        mLetters = letters;
        if (mLettersView != null) {
            mLettersView.setText(letters);
        }
    }

    public String getLetters() {
        return mLetters;
    }

    public void setKeyCode(int keyCode) {
        mKeyCode = keyCode;
    }

    public int getKeyCode() {
        return mKeyCode;
    }

    public void showLetters(boolean isShow) {
        mLettersView.setVisibility(isShow ? VISIBLE : GONE);
    }

    public void setOnPressedListener(OnPressedListener onPressedListener) {
        mOnPressedListener = onPressedListener;
    }

    public interface OnPressedListener {
        void onPressed(View view, boolean pressed);
    }
}
