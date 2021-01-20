package com.chooloo.www.callmanager.ui.widgets;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.widget.AppCompatEditText;

import com.chooloo.www.callmanager.util.Utilities;

import java.util.Objects;

public class DialpadEditText extends AppCompatEditText {

    public DialpadEditText(Context context) {
        this(context, null, 0);
    }

    public DialpadEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DialpadEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setUp();
    }

    private void setUp() {
        setInputType(getInputType() | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        setShowSoftInputOnFocus(true);
        setTextIsSelectable(true);
        setFocusableInTouchMode(true);
        setCursorVisible(true);
        setBackgroundColor(Color.TRANSPARENT);
        canScrollHorizontally(LAYOUT_DIRECTION_RTL | LAYOUT_DIRECTION_LTR);
        setInputType(InputType.TYPE_CLASS_PHONE);
        setMaxLines(1);
        setSingleLine(true);
        setTextAlignment(TEXT_ALIGNMENT_CENTER);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        final InputMethodManager imm = ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE));
        if (imm != null && imm.isActive(this)) {
            imm.hideSoftInputFromWindow(getApplicationWindowToken(), 0);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final boolean ret = super.onTouchEvent(event);
        // Must be done after super.onTouchEvent()
        final InputMethodManager imm = ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE));
        if (imm != null && imm.isActive(this)) {
            imm.hideSoftInputFromWindow(getApplicationWindowToken(), 0);
        }
        return ret;
    }

    public boolean isEmpty() {
        return this.length() == 0;
    }

    public String getNumbers() {
        return Utilities.getOnlyNumbers(Objects.requireNonNull(this.getText()).toString());
    }
}
