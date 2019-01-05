package com.chooloo.www.callmanager.activity;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.chooloo.www.callmanager.CallManager;
import com.chooloo.www.callmanager.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OngoingCallActivity extends AppCompatActivity {

    Callback mCallback = new Callback();

    @BindView(R.id.answer_btn) FloatingActionButton mAnswerButton;
    @BindView(R.id.deny_btn) FloatingActionButton mDenyButton;
    @BindView(R.id.text_status) TextView mStatusText;
    @BindView(R.id.caller_text) TextView mCallerText;

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
        CallManager.registerCallback(mCallback);
        updateUI(CallManager.getState());

        mCallerText.setText(CallManager.getPhoneNumber());

        String contactName = CallManager.getContactName(this);
        if (contactName != null) {
            mCallerText.setText(contactName);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CallManager.unregisterCallback(mCallback);
    }

    @OnClick(R.id.answer_btn)
    public void answer(View view) {
        CallManager.sAnswer();
    }

    @OnClick(R.id.deny_btn)
    public void deny(View view) {
        endCall();
    }

    private void endCall() {
        CallManager.sReject();
        finish();
    }

    //TODO Change the UI depending on the state (active/calling/hold...)
    private void updateUI(int state) {
        @StringRes int statusTextRes;
        switch (state) {
            case Call.STATE_ACTIVE:
                statusTextRes = R.string.status_ongoing_call;
                break;
            case Call.STATE_DISCONNECTED:
                statusTextRes = R.string.status_call_ended;
                break;
            case Call.STATE_RINGING:
                statusTextRes = R.string.status_incoming_call;
                break;
            case Call.STATE_DIALING:
                statusTextRes = R.string.status_outgoing_call;
                break;
            case Call.STATE_CONNECTING:
                statusTextRes = R.string.status_outgoing_call;
                break;
            case Call.STATE_HOLDING:
                statusTextRes = R.string.status_holding_call;
                break;
            default:
                statusTextRes = R.string.status_ongoing_call;
                break;
        }
        mStatusText.setText(statusTextRes);

        if (state == Call.STATE_DISCONNECTED) endCall();
    }

    class Callback extends Call.Callback {
        @Override
        public void onStateChanged(Call call, int state) {
            super.onStateChanged(call, state);
            updateUI(state);
        }
    }
}
