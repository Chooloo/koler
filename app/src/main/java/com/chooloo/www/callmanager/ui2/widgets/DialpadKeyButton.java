package com.chooloo.www.callmanager.ui2.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.chooloo.www.callmanager.R;

import butterknife.BindView;

public class DialpadKeyButton extends FrameLayout {

    private int mKeyCode;
    private String mNumber;
    private String mLetters;

    private @BindView(R.id.dialpad_key_number) TextView mNumberView;
    private @BindView(R.id.dialpad_key_letters) TextView mLettersView;

    private OnPressedListener mOnPressedListener;

    public DialpadKeyButton(Context context, String number, String letters, int keyCode) {
        super(context, null);

        mKeyCode = keyCode;
    }

    public DialpadKeyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
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
        mLettersView.setText(letters);
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

    public void setOnPressedListener(OnPressedListener onPressedListener) {
        mOnPressedListener = onPressedListener;
    }

    public interface OnPressedListener {
        void onPressed(View view, boolean pressed);
    }
}
