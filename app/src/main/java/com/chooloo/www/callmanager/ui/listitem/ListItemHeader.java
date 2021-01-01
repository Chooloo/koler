package com.chooloo.www.callmanager.ui.listitem;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.chooloo.www.callmanager.databinding.ListItemHeaderBinding;

public class ListItemHeader extends ListItem {
    private ListItemHeaderBinding binding;

    public ListItemHeader(Context context) {
        super(context);
        setUp();
    }

    public ListItemHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUp();
    }

    @Override
    void setUp() {
        binding = ListItemHeaderBinding.inflate(LayoutInflater.from(mContext), this, true);
        setClickable(false);
        setFocusable(false);
    }

    public void setHeader(String text) {
        binding.listItemHeader.setText(text);
    }
}
