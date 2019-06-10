package com.chooloo.www.callmanager.ui.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.util.Utilities;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListItem extends ConstraintLayout {

    private boolean mIsSingleLine;

    @BindView(R.id.item_title) TextView mTitleText;
    @BindView(R.id.item_desc) TextView mDescText;
    @BindView(R.id.item_image) ImageView mIcon;

    public ListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ListItem, 0, 0);
        String title = a.getString(R.styleable.ListItem_title);
        String desc = a.getString(R.styleable.ListItem_description);
        Drawable src = a.getDrawable(R.styleable.ListItem_src);
        a.recycle();

        setClickable(true);
        setFocusable(true);

        LayoutInflater.from(getContext()).inflate(R.layout.item_two_line, this, true);
        mIsSingleLine = desc == null || desc.isEmpty();

        ButterKnife.bind(this);

        setTitle(title);
        setDescription(desc);
        setIcon(src);
    }

    public void setTitle(String title) {
        mTitleText.setText(title);
    }

    public void setDescription(String description) {
        if (description == null || description.isEmpty()) {
            mDescText.setVisibility(GONE);
            setMinHeight((int) Utilities.convertDpToPixel(getContext(), 56.0f));
        } else {
            mDescText.setText(description);
            mDescText.setVisibility(VISIBLE);
            setMinHeight((int) Utilities.convertDpToPixel(getContext(), 72.0f));
        }
    }

    public void setIcon(Drawable drawable) {
        mIcon.setImageDrawable(drawable);
    }
}
