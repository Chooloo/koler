package com.chooloo.www.callmanager.activity;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.chooloo.www.callmanager.Stopwatch;
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

    // Listeners
    View.OnTouchListener mDefaultListener = (v, event) -> false;
    LongClickOptionsListener mLongClickListener;

    // Layouts
    @BindView(R.id.ongoingcall_layout) ConstraintLayout mParentLayout;

    // Text views
    @BindView(R.id.text_status) TextView mStatusText;
    @BindView(R.id.caller_text) TextView mCallerText;
    @BindView(R.id.text_end_call_timer) TextView mEndCallTimerText;
    @BindView(R.id.time_text) TextView mTimeText;

    // Action buttons
    @BindView(R.id.answer_btn) FloatingActionButton mAnswerButton;
    @BindView(R.id.deny_btn) FloatingActionButton mDenyButton;
    @BindView(R.id.button_mute) FloatingActionButton mMuteButton;
    @BindView(R.id.button_keypad) FloatingActionButton mKeypadButton;
    @BindView(R.id.button_speaker) FloatingActionButton mSpeakerButton;
    @BindView(R.id.button_add_call) FloatingActionButton mAddCallButton;
    @BindView(R.id.button_end_call_timer) FloatingActionButton mEndTimerButton;
    @BindView(R.id.button_send_sms) FloatingActionButton mSendSMSButton;
    @BindView(R.id.button_cancel) FloatingActionButton mCancelButton;

    // Overlays
    @BindView(R.id.overlay_reject_call_options) ViewGroup mRejectCallOverlay;

    @SuppressLint("ClickableViewAccessibility")

    // Instances of local classes
            Stopwatch mCallTimer = new Stopwatch();
    Callback mCallback = new Callback();

    // Handler variables
    final int TIME_START = 1;
    final int TIME_STOP = 0;
    final int TIME_UPDATE = 2;
    final int REFRESH_RATE = 100;
    public static int sHangUpTime = 10000;

    // Handlers
    Handler mFreeHandler = new Handler();
    Handler mCallTimeHandler = new Handler() { // Handles the call timer
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TIME_START:
                    mCallTimer.start(); // Starts the timer
                    mCallTimeHandler.sendEmptyMessage(TIME_UPDATE); // Starts the time ui updates
                    break;
                case TIME_STOP:
                    mCallTimeHandler.removeMessages(TIME_UPDATE); // No more updates
                    mCallTimer.stop(); // Stops the timer
                    updateTimeUI(); // Updates the time ui
                    break;
                case TIME_UPDATE:
                    updateTimeUI(); // Updates the time ui
                    mCallTimeHandler.sendEmptyMessageDelayed(TIME_UPDATE, REFRESH_RATE); // Text view updates every milisecond (REFRESH RATE)
                    break;
                default:
                    break;
            }
        }
    };

    // Runnables
    private Runnable mHangUpAfterTimeTask = new Runnable() {
        @Override
        public void run() {
            endCall();
        }
    };

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

    // If the app has been closed either by user or been forced to
    @Override
    protected void onDestroy() {
        super.onDestroy();
        CallManager.unregisterCallback(mCallback);
    }

    /**
     * Answers incoming call
     *
     * @param view
     */
    @OnClick(R.id.answer_btn)
    public void answer(View view) {
        activateCall();
    }

    /**
     * Denies incoming call / Ends active call
     *
     * @param view
     */
    @OnClick(R.id.deny_btn)
    public void deny(View view) {
        endCall();
    }

    //TODO add functionality to the different buttons
    @OnClick(R.id.button_end_call_timer)
    public void startEndCallTimer(View view) {
        mFreeHandler.postDelayed(mHangUpAfterTimeTask, sHangUpTime);
        Toast.makeText(this, "Hanging up after " + sHangUpTime / 1000 + " seconds", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.button_send_sms)
    public void sendSMS(View view) {
        Toast.makeText(this, "Supposed to do something here", Toast.LENGTH_SHORT).show();
    }

    // Update the current call time ui
    private void updateTimeUI() {
        mTimeText.setText(mCallTimer.getStringTime());
    }

    // Answers incoming call and changes the ui accordingly
    private void activateCall() {
        CallManager.sAnswer();
        switchToCallingUI();
    }

    // End current call / Incoming call and changes the ui accordingly
    private void endCall() {
        mCallTimeHandler.sendEmptyMessage(TIME_STOP);
        changeBackgroundColor(R.color.call_ended_background);
        CallManager.sReject();
        finish();
    }

    /**
     * Changes the current background color
     *
     * @param colorRes
     */
    private void changeBackgroundColor(@ColorRes int colorRes) {
        int backgroundColor = ContextCompat.getColor(this, colorRes);
        mParentLayout.setBackgroundColor(backgroundColor);

        ColorStateList stateList = new ColorStateList(new int[][]{}, new int[]{backgroundColor});
        mMuteButton.setBackgroundTintList(stateList);
        mKeypadButton.setBackgroundTintList(stateList);
        mSpeakerButton.setBackgroundTintList(stateList);
        mAddCallButton.setBackgroundTintList(stateList);
    }

    // Moves the deny button to the middle (After you answer incoming call)
    private void moveDenyToMiddle() {
        float parentCenterX = mParentLayout.getX() + mParentLayout.getWidth() / 2;
        float parentCenterY = mParentLayout.getY() + mParentLayout.getHeight() / 2;
        mAnswerButton.animate().translationX(parentCenterX - mAnswerButton.getWidth() / 2).translationY(parentCenterY - mAnswerButton.getHeight() / 2);
    }

    // Switches the ui to an active call ui
    private void switchToCallingUI() {
        mCallTimeHandler.sendEmptyMessage(TIME_START); // Starts the call timer
        changeBackgroundColor(R.color.call_in_progress_background);

        moveDenyToMiddle();
        mAnswerButton.hide();
        mMuteButton.show();
        mKeypadButton.show();
        mSpeakerButton.show();
        mAddCallButton.show();
    }

    /**
     * Updates the ui given the call state
     *
     * @param state
     */
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

    /**
     * Callback class
     * Listens to the call and do stuff when something changes
     */
    public class Callback extends Call.Callback {

        /**
         * Listens to the call state
         *
         * @param call
         * @param state
         */
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

        /**
         * Listens to the call's details
         *
         * @param call
         * @param details
         */
        @Override
        public void onDetailsChanged(Call call, Call.Details details) {
            super.onDetailsChanged(call, details);
            Timber.i("Details changed: %s", details.toString());
        }
    }
}
