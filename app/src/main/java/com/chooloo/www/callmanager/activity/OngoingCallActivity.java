package com.chooloo.www.callmanager.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chooloo.www.callmanager.CallManager;
import com.chooloo.www.callmanager.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OngoingCallActivity extends AppCompatActivity {

    @BindView(R.id.answer_btn) FloatingActionButton mAnswerButton;
    @BindView(R.id.deny_btn) FloatingActionButton mDenyButton;
    @BindView(R.id.text_status) TextView mStatusText;
    @BindView(R.id.caller_text) TextView mCallerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongoing_call);
        ButterKnife.bind(this);

        mStatusText.setText(getResources().getString(R.string.status_incoming_call));
        if (CallManager.getContactName(this) == null) {
            mCallerText.setText(CallManager.getPhoneNumber());
        } else {
            mCallerText.setText(CallManager.getContactName(this));
        }
    }

    @OnClick(R.id.answer_btn)
    public void answer(View view) {
        CallManager.sAnswer();
        mStatusText.setText(getResources().getString(R.string.status_ongoing_call));
    }

    @OnClick(R.id.deny_btn)
    public void deny(View view) {
        mStatusText.setText(getResources().getString(R.string.status_call_ended));
        CallManager.sReject();
        finish();
    }

    //TODO Change the UI depending on the state (active/calling/hold...)
    private void updateUI() {
        switch (CallManager.sCall.getState()) {

        }
    }
}
