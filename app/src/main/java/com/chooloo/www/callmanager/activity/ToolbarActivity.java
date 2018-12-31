package com.chooloo.www.callmanager.activity;

import android.annotation.SuppressLint;
import android.widget.TextView;

import com.chooloo.www.callmanager.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("Registered")
public class ToolbarActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView title;

    @Override
    protected void onStart() {
        super.onStart();
        ButterKnife.bind(this);

        title.setText(getTitle());

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
    }
}
