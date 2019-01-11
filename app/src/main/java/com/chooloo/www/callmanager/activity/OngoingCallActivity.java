package com.chooloo.www.callmanager.activity;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.chooloo.www.callmanager.CallManager;
import com.chooloo.www.callmanager.LongClickOptionsListener;
import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.util.PreferenceUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.ColorRes;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class OngoingCallActivity extends AppCompatActivity {

    @BindView(R.id.ongoingcall_layout)
    ConstraintLayout mParentLayout;
    Callback mCallback = new Callback();

    View.OnTouchListener mDefaultListener = (v, event) -> false;
    LongClickOptionsListener mLongClickListener;

    @BindView(R.id.answer_btn) FloatingActionButton mAnswerButton;
    @BindView(R.id.deny_btn) FloatingActionButton mDenyButton;
    @BindView(R.id.text_status) TextView mStatusText;
    @BindView(R.id.caller_text) TextView mCallerText;

    @BindView(R.id.button_mute) FloatingActionButton mMuteButton;
    @BindView(R.id.button_keypad) FloatingActionButton mKeypadButton;
    @BindView(R.id.button_speaker) FloatingActionButton mSpeakerButton;
    @BindView(R.id.button_add_call) FloatingActionButton mAddCallButton;

    @BindView(R.id.overlay_reject_call_options) ViewGroup mRejectCallOverlay;
    @BindView(R.id.button_end_call_timer) FloatingActionButton mEndTimerButton;
    @BindView(R.id.text_end_call_timer) TextView mEndCallTimerText;
    @BindView(R.id.button_send_sms) FloatingActionButton mSendSMSButton;
    @BindView(R.id.button_cancel) FloatingActionButton mCancelButton;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongoing_call);
        PreferenceUtils.getInstance(this);

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
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        ButterKnife.bind(this);
        mCancelButton.hide();
        mSendSMSButton.hide();
        mEndTimerButton.hide();
        mLongClickListener = new LongClickOptionsListener(this, mRejectCallOverlay);

        //Listen for call state changes
        CallManager.registerCallback(mCallback);
        updateUI(CallManager.getState());

        // Set the caller name text view
        String phoneNumber = CallManager.getPhoneNumber();
        if (phoneNumber != null) {
            mCallerText.setText(phoneNumber);
        } else {
            mCallerText.setText(R.string.name_unknown);
        }

        // Get the caller's contact name
        String contactName = CallManager.getContactName(this);
        if (contactName != null) {
            mCallerText.setText(contactName);
        }
        //Set the correct text for the TextView
        String endCallSeconds = PreferenceUtils.getInstance().getString(R.string.pref_end_call_timer_key);
        String endCallText = mEndCallTimerText.getText() + " " + endCallSeconds + "s";
        mEndCallTimerText.setText(endCallText);
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

    //TODO add functionality to the different buttons
    @OnClick(R.id.button_end_call_timer)
    public void startEndCallTimer(View view) {
        Toast.makeText(this, "Supposed to do something here", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.button_send_sms)
    public void sendSMS(View view) {
        Toast.makeText(this, "Supposed to do something here", Toast.LENGTH_SHORT).show();
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

    private void changeBackgroundColor(@ColorRes int colorRes) {
        int backgroundColor = ContextCompat.getColor(this, colorRes);
        mParentLayout.setBackgroundColor(backgroundColor);

        ColorStateList stateList = new ColorStateList(new int[][]{}, new int[]{backgroundColor});
        mMuteButton.setBackgroundTintList(stateList);
        mKeypadButton.setBackgroundTintList(stateList);
        mSpeakerButton.setBackgroundTintList(stateList);
        mAddCallButton.setBackgroundTintList(stateList);
    }

    private void moveDenyToMiddle() {
        float parentCenterX = mParentLayout.getX() + mParentLayout.getWidth() / 2;
        float parentCenterY = mParentLayout.getY() + mParentLayout.getHeight() / 2;
        mAnswerButton.animate().translationX(parentCenterX - mAnswerButton.getWidth() / 2).translationY(parentCenterY - mAnswerButton.getHeight() / 2);
    }

    private void switchToCallingUI() {
        changeBackgroundColor(R.color.call_in_progress_background);

        moveDenyToMiddle();
        mAnswerButton.hide();
        mMuteButton.show();
        mKeypadButton.show();
        mSpeakerButton.show();
        mAddCallButton.show();
    }

    private void updateUI(int state) {
        @StringRes int statusTextRes;
        switch (state) {
            case Call.STATE_ACTIVE:
                statusTextRes = R.string.status_call_active;
                break;
            case Call.STATE_DISCONNECTED:
                statusTextRes = R.string.status_call_disconnected;
                break;
            case Call.STATE_RINGING:
                statusTextRes = R.string.status_call_incoming;
                break;
            case Call.STATE_DIALING:
                statusTextRes = R.string.status_call_dialing;
                break;
            case Call.STATE_CONNECTING:
                statusTextRes = R.string.status_call_dialing;
                break;
            case Call.STATE_HOLDING:
                statusTextRes = R.string.status_call_holding;
                break;
            default:
                statusTextRes = R.string.status_call_active;
                break;
        }
        mStatusText.setText(statusTextRes);

        if (state != Call.STATE_RINGING) {
            switchToCallingUI();
            mDenyButton.setOnTouchListener(mDefaultListener);
        } else {
            mDenyButton.setOnTouchListener(mLongClickListener);
        }

        if (state == Call.STATE_DISCONNECTED) endCall();
    }

    public class Callback extends Call.Callback {

        @Override
        public void onStateChanged(Call call, int state) {
            /*
              Call states:

              1   = Call.STATE_DIALING
              2   = Call.STATE_RINGING
              3   = Call.STATE_HOLDING
              4   = Call.STATE_ACTIVE
              7   = Call.STATE_DISCONNECTED
              8   = Call.STATE_SELECT_PHONE_ACCOUNT
              9   = Call.STATE_CONNECTING
              10  = Call.STATE_DISCONNECTING
              11  = Call.STATE_PULLING_CALL
             */
            super.onStateChanged(call, state);
            Timber.i("State changed: %s", state);
            updateUI(state);
        }

        @Override
        public void onDetailsChanged(Call call, Call.Details details) {
            super.onDetailsChanged(call, details);
            Timber.i("Details changed: %s", details.toString());
        }
    }
}
