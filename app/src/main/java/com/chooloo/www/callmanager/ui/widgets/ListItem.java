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

    private boolean mIsSingleLine;

    @BindView(R.id.list_item_big_text) TextView mBigTextView;
    @BindView(R.id.list_item_small_text) TextView mSmallTextView;
    @BindView(R.id.list_item_image) ImageView mImageView;

    private String mBigText;
    private String mSmallText;
    private Drawable mImage;

    public ListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ListItem, 0, 0);
        String bigText = a.getString(R.styleable.ListItem_title);
        String smallText = a.getString(R.styleable.ListItem_description);
        Drawable image = a.getDrawable(R.styleable.ListItem_src);
        a.recycle();
        setUp(context, bigText, smallText, image);
    }

    public ListItem(Context context, String bigText, @Nullable String smallText, Drawable image) {
        super(context);
        setUp(context, bigText, smallText, image);
    }

    private void setUp(Context context, String bigText, @Nullable String smallText, Drawable image) {
        LayoutInflater.from(context).inflate(R.layout.list_item, this, true);
        ButterKnife.bind(this);

        setClickable(true);
        setFocusable(true);

        setBigText(bigText);
        setImage(image);
        if (smallText != null) {
            setSmallText(smallText);
        }
    }

    public void setBigText(String text) {
        mBigText = text;
        mBigTextView.setText(text);
    }

    public void setSmallText(String text) {
        mSmallText = text;
        mSmallTextView.setText(text);
        mSmallTextView.setVisibility(VISIBLE);
    }

    public void setImage(Drawable image) {
        mImage = image;
        mImageView.setImageDrawable(drawable);
    }

    public String getBigText() {
        return mBigText;
    }

    public String getSmallText() {
        return mSmallText;
    }

    public Drawable getImage() {
        return mImage;
    }
}
