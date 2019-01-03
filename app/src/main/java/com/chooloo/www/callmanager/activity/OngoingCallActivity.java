package com.chooloo.www.callmanager.activity;

import android.content.Context;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chooloo.www.callmanager.CallManager;
import com.chooloo.www.callmanager.R;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OngoingCallActivity extends AppCompatActivity {
    @BindView(R.id.answer_button)
    Button mAnswerButton;
    @BindView(R.id.button_deny)
    Button mDenyButton;
    @BindView(R.id.text_status)
    TextView mStatusText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongoing_call);
        ButterKnife.bind(this);
        if (CallManager.getContactName(this) == "") {
            mStatusText.setText(CallManager.getPhoneNumber());
        } else {
            mStatusText.setText(CallManager.getContactName(this));
        }
    }

    @OnClick(R.id.answer_button)
    public void answer(View view) {
        CallManager.sAnswer();
    }

    @OnClick(R.id.button_deny)
    public void deny(View view) {
        CallManager.sReject();
        finish();
    }

    //TODO Change the UI depending on the state (active/calling/hold...)
    private void updateUI() {
        switch (CallManager.sCall.getState()) {

        }
    }
}
