package com.chooloo.www.callmanager.activity;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
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

import java.util.Locale;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

@SuppressLint("ClickableViewAccessibility")
public class OngoingCallActivity extends AppCompatActivity {

    // Handler variables
    private static final int TIME_START = 1;
    private static final int TIME_STOP = 0;
    private static final int TIME_UPDATE = 2;
    private static final int REFRESH_RATE = 100;

    // Listeners
    View.OnTouchListener mDefaultListener = (v, event) -> false;
    LongClickOptionsListener mRejectLongClickListener;
    LongClickOptionsListener mAnswerLongClickListener;
    Callback mCallback = new Callback();
    ActionTimer mActionTimer = new ActionTimer();

    // Layouts
    @BindView(R.id.ongoingcall_layout) ConstraintLayout mParentLayout;

    // Text views
    @BindView(R.id.text_status) TextView mStatusText;
    @BindView(R.id.text_caller) TextView mCallerText;
    @BindView(R.id.text_reject_call_timer_desc) TextView mRejectCallTimerText;
    @BindView(R.id.text_answer_call_timer_desc) TextView mAnswerCallTimerText;
    @BindView(R.id.text_action_time_left) TextView mActionTimeLeftText;
    @BindView(R.id.text_timer_indicator) TextView mTimerIndicatorText;
    @BindView(R.id.text_stopwatch) TextView mTimeText;

    // Action buttons
    @BindView(R.id.answer_btn) FloatingActionButton mAnswerButton;
    @BindView(R.id.deny_btn) FloatingActionButton mDenyButton;
    @BindView(R.id.button_mute) FloatingActionButton mMuteButton;
    @BindView(R.id.button_keypad) FloatingActionButton mKeypadButton;
    @BindView(R.id.button_speaker) FloatingActionButton mSpeakerButton;
    @BindView(R.id.button_add_call) FloatingActionButton mAddCallButton;
    @BindView(R.id.button_reject_call_timer) FloatingActionButton mRejectCallTimerButton;
    @BindView(R.id.button_send_sms) FloatingActionButton mSendSMSButton;
    @BindView(R.id.button_cancel) FloatingActionButton mCancelButton;

    // Overlays
    @BindView(R.id.overlay_reject_call_options) ViewGroup mRejectCallOverlay;
    @BindView(R.id.overlay_answer_call_options) ViewGroup mAnswerCallOverlay;
    @BindView(R.id.overlay_action_timer) ViewGroup mActionTimerOverlay;

    // Stopwatch related
    Stopwatch mCallTimer = new Stopwatch();
    @SuppressLint("HandlerLeak") Handler mCallTimeHandler = new Handler() { // Handles the call timer
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
        mRejectCallTimerButton.hide();
        mRejectLongClickListener = new LongClickOptionsListener(this, mRejectCallOverlay);
        mAnswerLongClickListener = new LongClickOptionsListener(this, mAnswerCallOverlay);

        //Hide all overlays
        mActionTimerOverlay.setAlpha(0.0f);
        mAnswerCallOverlay.setAlpha(0.0f);
        mRejectCallOverlay.setAlpha(0.0f);

        //Listen for call state changes
        CallManager.registerCallback(mCallback);
        updateUI(CallManager.getState());

        // Set the caller name text view
        String phoneNumber = CallManager.getDisplayName();
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

        //Set the correct text for the TextViews
        String rejectCallSeconds = PreferenceUtils.getInstance().getString(R.string.pref_reject_call_timer_key);
        String rejectCallText = mRejectCallTimerText.getText() + " " + rejectCallSeconds + "s";
        mRejectCallTimerText.setText(rejectCallText);

        String answerCallSeconds = PreferenceUtils.getInstance().getString(R.string.pref_answer_call_timer_key);
        String answerCallText = mAnswerCallTimerText.getText() + " " + answerCallSeconds + "s";
        mAnswerCallTimerText.setText(answerCallText);

        //mActionTimer = new ActionTimer(millis, REFRESH_RATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CallManager.unregisterCallback(mCallback); //The activity is gone, no need to listen to changes
        mActionTimer.cancel();
    }

    /**
     * Answers incoming call
     */
    @OnClick(R.id.answer_btn)
    public void answer(View view) {
        activateCall();
    }

    /**
     * Denies incoming call / Ends active call*
     */
    @OnClick(R.id.deny_btn)
    public void deny(View view) {
        endCall();
    }

    //TODO remove the ability to click buttons under the overlay
    //TODO silence the ringing
    @OnClick(R.id.button_reject_call_timer)
    public void startEndCallTimer(View view) {
        int seconds = Integer.parseInt(PreferenceUtils.getInstance().getString(R.string.pref_reject_call_timer_key));
        mActionTimer.setData(seconds * 1000, true);
        mActionTimer.start();
        mActionTimerOverlay.animate().alpha(1.0f);
    }

    @OnClick(R.id.button_answer_call_timer)
    public void startAnswerCallTimer(View view) {
        int seconds = Integer.parseInt(PreferenceUtils.getInstance().getString(R.string.pref_answer_call_timer_key));
        mActionTimer.setData(seconds * 1000, false);
        mActionTimer.start();
    }

    //TODO add functionality to the send SMS Button
    @OnClick(R.id.button_send_sms)
    public void sendSMS(View view) {
        Toast.makeText(this, "Supposed to do something here", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.button_cancel_timer)
    public void cancelTimer(View view) {
        mActionTimer.cancel();
    }

    /**
     * Update the current call time ui
     */
    private void updateTimeUI() {
        mTimeText.setText(mCallTimer.getStringTime());
    }

    /**
     * Answers incoming call and changes the ui accordingly
     */
    private void activateCall() {
        CallManager.sAnswer();
        switchToCallingUI();
    }

    /**
     * End current call / Incoming call and changes the ui accordingly
     */
    private void endCall() {
        mCallTimeHandler.sendEmptyMessage(TIME_STOP);
        changeBackgroundColor(R.color.call_ended_background);
        CallManager.sReject();
        finish();
    }

    /**
     * Changes the current background color
     *
     * @param colorRes the color to change to
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

    /**
     * Moves the deny button to the middle
     */
    private void moveDenyToMiddle() {
        float parentCenterX = mParentLayout.getX() + mParentLayout.getWidth() / 2;
        float parentCenterY = mParentLayout.getY() + mParentLayout.getHeight() / 2;
        mAnswerButton.animate().translationX(parentCenterX - mAnswerButton.getWidth() / 2).translationY(parentCenterY - mAnswerButton.getHeight() / 2);
    }

    /**
     * Switches the ui to an active call ui
     */
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
     * @param state the current call state
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
            mAnswerButton.setOnTouchListener(mDefaultListener);
        } else {
            mDenyButton.setOnTouchListener(mRejectLongClickListener);
            mAnswerButton.setOnTouchListener(mAnswerLongClickListener);
        }

        if (state == Call.STATE_DISCONNECTED) endCall();
    }

    /**
     * Callback class
     * Listens to the call and do stuff when something changes
     */
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

    class ActionTimer {

        CountDownTimer mTimer = null;
        boolean mIsRejecting = true;

        private void setData(long millisInFuture, boolean isRejecting) {
            mIsRejecting = isRejecting;
            @ColorRes int textColorRes;
            @StringRes int textIndicator;
            if (isRejecting) {
                textColorRes = R.color.red_phone;
                textIndicator = R.string.reject_timer_indicator;
            }
            else{
                textColorRes = R.color.green_phone;
                textIndicator = R.string.answer_timer_indicator;
            }

            @ColorInt int textColor = ContextCompat.getColor(OngoingCallActivity.this, textColorRes);
            mActionTimeLeftText.setTextColor(textColor);
            mTimerIndicatorText.setText(textIndicator);

            mTimer = new CountDownTimer(millisInFuture, REFRESH_RATE) {
                Locale mLocale = Locale.getDefault();

                @Override
                public void onTick(long millisUntilFinished) {
                    int secondsUntilFinished = (int) (millisUntilFinished / 1000);
                    String timer = String.format(mLocale, "00:%02d", secondsUntilFinished);
                    mActionTimeLeftText.setText(timer);
                }

                @Override
                public void onFinish() {
                    if (mIsRejecting) endCall();
                    else activateCall();
                    mActionTimerOverlay.animate().alpha(0.0f);
                }
            };
        }

        private void start() {
            if (mTimer != null) mTimer.start();
            else Timber.w("Couldn't start action timer (timer is null)");

            if (mActionTimerOverlay != null) mActionTimerOverlay.animate().alpha(1.0f);
        }

        private void cancel() {
            if (mTimer != null) mTimer.cancel();
            else Timber.w("Couldn't cancel action timer (timer is null)");

            if (mActionTimerOverlay != null) mActionTimerOverlay.animate().alpha(0.0f);
        }
    }
}
