package com.chooloo.www.callmanager.ui.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.chooloo.www.callmanager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListItem extends ConstraintLayout {

    @BindView(R.id.item_title) TextView mTitleText;
    @Nullable @BindView(R.id.item_desc) TextView mDescText;
    @BindView(R.id.item_image) ImageView mIcon;

    public ListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ListItem, 0, 0);
        String title = a.getString(R.styleable.ListItem_title);
        String desc = a.getString(R.styleable.ListItem_description);
        Drawable src = a.getDrawable(R.styleable.ListItem_src);
        a.recycle();

        boolean isSingleLine = desc == null || desc.isEmpty();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        if (isSingleLine) inflater.inflate(R.layout.item_single_line, this, true);
        else inflater.inflate(R.layout.item_two_line, this, true);

        ButterKnife.bind(this);
        mTitleText.setText(title);
        if (!isSingleLine) mDescText.setText(desc);
        mIcon.setImageDrawable(src);
    }
}
