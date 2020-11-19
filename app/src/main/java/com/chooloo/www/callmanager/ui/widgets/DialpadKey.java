package com.chooloo.www.callmanager.ui.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chooloo.www.callmanager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DialpadKey extends LinearLayout {

    private int mKeyCode;
    private String mDigit;
    private String mLetters;

    @BindView(R.id.digit) TextView mDigitView;
    @BindView(R.id.letters) TextView mLettersView;

    public DialpadKey(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.dialpad_key_layout, this);
        ButterKnife.bind(this);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.DialpadKey);
        updateDigit(attributes.getString(R.styleable.DialpadKey_digit));
    }

    private void updateDigit(String digit) {
        setDigit(digit);
        switch (digit) {
            case "0":
                setKeyCode(KeyEvent.KEYCODE_0);
                setLetters(getContext().getString(R.string.dialpad_0_letters));
                break;
            case "1":
                setKeyCode(KeyEvent.KEYCODE_1);
                setLetters(getContext().getString(R.string.dialpad_0_letters));
                break;
            case "2":
                setKeyCode(KeyEvent.KEYCODE_2);
                setLetters(getContext().getString(R.string.dialpad_2_letters));
                break;
            case "3":
                setKeyCode(KeyEvent.KEYCODE_3);
                setLetters(getContext().getString(R.string.dialpad_3_letters));
                break;
            case "4":
                setKeyCode(KeyEvent.KEYCODE_4);
                setLetters(getContext().getString(R.string.dialpad_4_letters));
                break;
            case "5":
                setKeyCode(KeyEvent.KEYCODE_5);
                setLetters(getContext().getString(R.string.dialpad_5_letters));
                break;
            case "6":
                setKeyCode(KeyEvent.KEYCODE_6);
                setLetters(getContext().getString(R.string.dialpad_6_letters));
                break;
            case "7":
                setKeyCode(KeyEvent.KEYCODE_7);
                setLetters(getContext().getString(R.string.dialpad_7_letters));
                break;
            case "8":
                setKeyCode(KeyEvent.KEYCODE_8);
                setLetters(getContext().getString(R.string.dialpad_8_letters));
                break;
            case "9":
                setKeyCode(KeyEvent.KEYCODE_9);
                setLetters(getContext().getString(R.string.dialpad_9_letters));
                break;
            case "*":
                setKeyCode(KeyEvent.KEYCODE_STAR);
                setLetters(getContext().getString(R.string.dialpad_star_letters));
                break;
            case "#":
                setKeyCode(KeyEvent.KEYCODE_POUND);
                setLetters(getContext().getString(R.string.dialpad_pound_letters));
                break;
            default:
                break;
        }
    }

    public void setDigit(String digit) {
        mDigit = digit;
        mDigitView.setText(digit);
    }

    public String getDigit() {
        return mDigit;
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
}
