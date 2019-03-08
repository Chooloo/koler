package com.chooloo.www.callmanager.activity;

import android.annotation.SuppressLint;
import android.graphics.Outline;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.TextView;

import com.chooloo.www.callmanager.R;
import com.google.android.material.appbar.AppBarLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("Registered")
public class AppBarActivity extends AppCompatActivity {

    @BindView(R.id.appbar) AppBarLayout mAppBarLayout;
    @BindView(R.id.toolbar) Toolbar mToolbar;
//    @BindView(R.id.toolbar_title) TextView mTextTitle;

    @Override
    protected void onStart() {
        super.onStart();
        ButterKnife.bind(this);

//        mTextTitle.setText(getTitle());

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");

//        ViewOutlineProvider outlineProvider = new ViewOutlineProvider() {
//            @Override
//            public void getOutline(View view, Outline outline) {
//                float radiusDp = getResources().getDimension(R.dimen.app_bar_corner_radius);
//                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), radiusDp);
//            }
//        };
//        mAppBarLayout.setOutlineProvider(outlineProvider);
    }
}
