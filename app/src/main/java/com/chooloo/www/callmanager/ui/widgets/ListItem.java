package com.chooloo.www.callmanager.ui.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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

    @BindView(R.id.list_item_header) TextView mHeaderView;
    @BindView(R.id.list_item_big_text) TextView mBigTextView;
    @BindView(R.id.list_item_small_text) TextView mSmallTextView;
    @BindView(R.id.list_item_image) ImageView mImageView;

    private String mBigText;
    private String mSmallText;
    private String mHeaderText;

    public ListItem(Context context) {
        super(context);
        setUp(context, "", null, null, null);
    }

    public ListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ListItem, 0, 0);
        String bigText = a.getString(R.styleable.ListItem_bigText);
        String smallText = a.getString(R.styleable.ListItem_smallText);
        String header = a.getString(R.styleable.ListItem_header);
        Drawable image = a.getDrawable(R.styleable.ListItem_src);
        a.recycle();
        setUp(context, bigText, smallText, image, header);
    }

    public ListItem(Context context, String bigText, @Nullable String smallText, @Nullable Drawable image, @Nullable String header) {
        super(context);
        setUp(context, bigText, smallText, image, header);
    }

    private void setUp(Context context, String bigText, @Nullable String smallText, @Nullable Drawable image, @Nullable String header) {
        LayoutInflater.from(context).inflate(R.layout.list_item, this, true);
        ButterKnife.bind(this);

        setClickable(true);
        setFocusable(true);

        setBigText(bigText);
        if (image != null) {
            setImageDrawable(image);
        }
        if (smallText != null) {
            setSmallText(smallText);
        }
        if (header != null) {
            setHeaderText(header);
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

    public void setHeaderText(String text) {
        mHeaderText = text;
        mHeaderView.setText(text);
        mHeaderView.setVisibility(VISIBLE);
    }

    public void setImageDrawable(Drawable image) {
        mImageView.setImageDrawable(image);
    }

    public void setImageUri(Uri image) {
        mImageView.setImageURI(image);
    }

    public String getBigText() {
        return mBigText;
    }

    public String getSmallText() {
        return mSmallText;
    }

    public String getHeaderText() {
        return mHeaderText;
    }

    public void showHeader(boolean isShow) {
        mHeaderView.setVisibility(isShow ? VISIBLE : INVISIBLE);
    }
}
