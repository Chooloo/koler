package com.chooloo.www.callmanager.activity;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.chooloo.www.callmanager.CallManager;
import com.chooloo.www.callmanager.R;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OngoingCallActivity extends AppCompatActivity {
    @BindView(R.id.answer_btn)
    Button mAnswerButton;
    @BindView(R.id.deny_btn)
    Button mDenyButton;
    @BindView(R.id.text_status)
    TextView mStatusText;
    @BindView(R.id.caller_text)
    TextView mCallerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongoing_call);

        //This activity needs to show even if the screen is off or locked
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
            if (km != null) {
                km.requestDismissKeyguard(this, null);
            }
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        }

        ButterKnife.bind(this);

        mStatusText.setText(getResources().getString(R.string.status_incoming_call));
        mCallerText.setText(CallManager.getPhoneNumber());

        String contactName = CallManager.getContactName(this);
        if (contactName != null) {
            mCallerText.setText(contactName);
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
