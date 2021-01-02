package com.chooloo.www.callmanager.ui.listitem;

import android.content.Context;
import android.util.AttributeSet;

import androidx.constraintlayout.widget.ConstraintLayout;

public abstract class ListItem extends ConstraintLayout {
    protected final Context mContext;

    public ListItem(Context context) {
        super(context);
        mContext = context;
        setUp();
    }

    public ListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setUp();
    }

    abstract void setUp();
}
