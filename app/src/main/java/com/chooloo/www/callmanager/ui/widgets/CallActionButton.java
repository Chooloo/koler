package com.chooloo.www.callmanager.ui.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.databinding.CallActionButtonLayoutBinding;

public class CallActionButton extends LinearLayout {
    private CallActionButtonLayoutBinding binding;

    private final Context mContext;

    private final AttributeSet mAttrs;

    private final int mDefStyle;

    public CallActionButton(Context context) {
        this(context, null);
    }

    public CallActionButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CallActionButton(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        mAttrs = attrs;
        mDefStyle = defStyle;
        setUp();
    }

    private void setUp() {
        binding = CallActionButtonLayoutBinding.inflate(LayoutInflater.from(getContext()), this, true);
        TypedArray attributes = mContext.obtainStyledAttributes(mAttrs, R.styleable.CallActionButton, mDefStyle, 0);
        setText(attributes.getString(R.styleable.CallActionButton_text));
        setIcon(attributes.getResourceId(R.styleable.CallActionButton_icon, NO_ID));
        setFocusable(true);
        setClickable(true);
    }

    public void setText(String text) {
        binding.callActionText.setText(text);
    }

    public void setIcon(@DrawableRes int drawableRes) {
        binding.callActionIcon.setImageDrawable(ContextCompat.getDrawable(mContext, drawableRes));
    }
}
