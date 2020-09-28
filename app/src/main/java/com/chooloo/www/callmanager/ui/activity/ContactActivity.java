package com.chooloo.www.callmanager.ui.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.util.ThemeUtils;

public class ContactActivity extends AbsThemeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setThemeType(ThemeUtils.TYPE_NORMAL);
        setContentView(R.layout.activity_contact);


    }
}