package com.chooloo.www.callmanager.ui.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.databinding.CallActionButtonLayoutBinding;

public class CallActionButton extends LinearLayout implements View.OnClickListener {
    private CallActionButtonLayoutBinding binding;

    private final Context mContext;

    private final AttributeSet mAttrs;

    private final int mDefStyle;

    private String mDefaultText;
    private String mOnCLickText;
    @DrawableRes private int mDefaultIcon;
    @DrawableRes private int mOnClickIcon;

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

    @Override
    public void onClick(View view) {
        Toast.makeText(mContext, "Clicked", Toast.LENGTH_SHORT).show();
        toggleActivated();
    }

    private void setUp() {
        binding = CallActionButtonLayoutBinding.inflate(LayoutInflater.from(getContext()), this, true);
        TypedArray attributes = mContext.obtainStyledAttributes(mAttrs, R.styleable.CallActionButton, mDefStyle, 0);

        setText(attributes.getString(R.styleable.CallActionButton_text));
        setTextOnClick(attributes.getString(R.styleable.CallActionButton_activatedText));
        setIcon(attributes.getResourceId(R.styleable.CallActionButton_icon, NO_ID));
        setIconOnClick(attributes.getResourceId(R.styleable.CallActionButton_activatedIcon, NO_ID));

        setFocusable(true);
        setClickable(true);
        setOnClickListener(this);
    }

    public void setText(String text) {
        mDefaultText = text;
        applyText(text);
    }

    public void setTextOnClick(String textOnClick) {
        mOnCLickText = textOnClick;
    }

    public void setIcon(@DrawableRes int icon) {
        mDefaultIcon = icon;
        applyIcon(icon);
    }

    public void setIconOnClick(@DrawableRes int onClickIcon) {
        mOnClickIcon = onClickIcon;
    }

    private void applyIcon(@DrawableRes int icon) {
        binding.callActionIcon.setImageDrawable(ContextCompat.getDrawable(mContext, icon));
    }

    private void applyText(String text) {
        binding.callActionText.setText(text);
    }

    private void toggleActivated() {
        setActivated(!isActivated());
        if (mOnClickIcon != NO_ID) {
            applyIcon(isActivated() ? mOnClickIcon : mDefaultIcon);
        }
        if (mOnCLickText != null) {
            applyText(isActivated() ? mOnCLickText : mDefaultText);
        }
    }

}
