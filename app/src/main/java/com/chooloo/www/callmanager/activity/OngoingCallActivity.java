package com.chooloo.www.callmanager.activity;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.res.ColorStateList;
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

import androidx.annotation.ColorRes;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OngoingCallActivity extends AppCompatActivity {

    @BindView(R.id.ongoingcall_layout)
    ConstraintLayout mParentLayout;
    Callback mCallback = new Callback();

    @BindView(R.id.answer_btn) FloatingActionButton mAnswerButton;
    @BindView(R.id.deny_btn) FloatingActionButton mDenyButton;
    @BindView(R.id.text_status) TextView mStatusText;
    @BindView(R.id.caller_text) TextView mCallerText;
    @BindView(R.id.button_mute) FloatingActionButton mMuteButton;
    @BindView(R.id.button_keypad) FloatingActionButton mKeypadButton;
    @BindView(R.id.button_speaker) FloatingActionButton mSpeakerButton;
    @BindView(R.id.button_add_call) FloatingActionButton mAddCallButton;

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
        activateCall();
    }

    @OnClick(R.id.deny_btn)
    public void deny(View view) {
        endCall();
    }

    private void activateCall() {
        CallManager.sAnswer();
        switchToCallingUI();
    }

    private void endCall() {
        changeBackgroundColor(R.color.call_ended_background);
        CallManager.sReject();
        finish();
    }

    private void switchToCallingUI() {
        changeBackgroundColor(R.color.call_in_progress_background);

        mAnswerButton.hide();
        mMuteButton.show();
        mKeypadButton.show();
        mSpeakerButton.show();
        mAddCallButton.show();
    }

    private void changeBackgroundColor(@ColorRes int colorRes) {
        int backgroundColor = ContextCompat.getColor(this, colorRes);
        mParentLayout.setBackgroundColor(backgroundColor);
        Window window = getWindow();
        if (window != null) window.setStatusBarColor(backgroundColor);

        ColorStateList stateList = new ColorStateList(new int[][]{}, new int[]{backgroundColor});
        mMuteButton.setBackgroundTintList(stateList);
        mKeypadButton.setBackgroundTintList(stateList);
        mSpeakerButton.setBackgroundTintList(stateList);
        mAddCallButton.setBackgroundTintList(stateList);
    }

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

        if (state == Call.STATE_ACTIVE || state == Call.STATE_DIALING) switchToCallingUI();
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
