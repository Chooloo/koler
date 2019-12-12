package com.chooloo.www.callmanager.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.telecom.Call;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.transition.ChangeBounds;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.database.entity.Contact;
import com.chooloo.www.callmanager.listener.AllPurposeTouchListener;
import com.chooloo.www.callmanager.listener.LongClickOptionsListener;
import com.chooloo.www.callmanager.listener.NotificationActionReceiver;
import com.chooloo.www.callmanager.ui.fragment.DialpadFragment;
import com.chooloo.www.callmanager.util.CallManager;
import com.chooloo.www.callmanager.util.PreferenceUtils;
import com.chooloo.www.callmanager.util.Stopwatch;
import com.chooloo.www.callmanager.util.ThemeUtils;
import com.chooloo.www.callmanager.util.Utilities;
import com.chooloo.www.callmanager.viewmodels.SharedDialViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;
import static com.chooloo.www.callmanager.util.BiometricUtils.showBiometricPrompt;

@SuppressLint("ClickableViewAccessibility")
//TODO Fix the buttons
public class OngoingCallActivity extends AbsThemeActivity implements DialpadFragment.OnKeyDownListener {

    // Finals
    private static final long END_CALL_MILLIS = 1500;
    private static final String CHANNEL_ID = "notification";
    private static final int NOTIFICATION_ID = 42069;
    public static final String ACTION_ANSWER = "ANSWER";
    public static final String ACTION_HANGUP = "HANGUP";

    // Handler variables
    private static final int TIME_START = 1;
    private static final int TIME_STOP = 0;
    private static final int TIME_UPDATE = 2;
    private static final int REFRESH_RATE = 100;

    // Call State
    private static int mState;
    private static String mStateText;

    // Fragments
    private DialpadFragment mDialpadFragment;

    // ViewModels
    private SharedDialViewModel mSharedDialViewModel;

    // BottomSheet
    BottomSheetBehavior mBottomSheetBehavior;

    //  Current states
    boolean mIsCallingUI = false;
    boolean mIsCreatingUI = true;

    // Utilities
    Stopwatch mCallTimer = new Stopwatch();
    Callback mCallback = new Callback();
    ActionTimer mActionTimer = new ActionTimer();

    // Listeners
    LongClickOptionsListener mRejectLongClickListener;
    LongClickOptionsListener mAnswerLongClickListener;

    // PowerManager
    PowerManager powerManager;
    PowerManager.WakeLock wakeLock;
    private int field = 0x00000020;

    // Audio
    AudioManager mAudioManager;

    // Handlers
    Handler mCallTimeHandler = new CallTimeHandler();

    // Swipes Listeners
    AllPurposeTouchListener mSmsOverlaySwipeListener;
    AllPurposeTouchListener mIncomingCallSwipeListener;

    // Notification
    NotificationCompat.Builder mBuilder;
    NotificationManager mNotificationManager;

    // Edit Texts
    @BindView(R.id.edit_sms) TextInputEditText mEditSms;

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
    @BindView(R.id.reject_btn) FloatingActionButton mRejectButton;

    // Image Views
    @BindView(R.id.image_placeholder) ImageView mPlaceholderImage;
    @BindView(R.id.image_photo) ImageView mPhotoImage;
    @BindView(R.id.button_hold) ImageView mHoldButton;
    @BindView(R.id.button_mute) ImageView mMuteButton;
    @BindView(R.id.button_keypad) ImageView mKeypadButton;
    @BindView(R.id.button_speaker) ImageView mSpeakerButton;
    @BindView(R.id.button_add_call) ImageView mAddCallButton;
    @BindView(R.id.button_send_sms) Button mSendSmsButton;

    // Floating Action Buttons
    @BindView(R.id.button_floating_reject_call_timer)
    FloatingActionButton mFloatingRejectCallTimerButton;
    @BindView(R.id.button_floating_answer_call_timer)
    FloatingActionButton mFloatingAnswerCallTimerButton;
    @BindView(R.id.button_floating_send_sms) FloatingActionButton mFloatingSendSMSButton;
    @BindView(R.id.button_floating_cancel_overlay)
    FloatingActionButton mFloatingCancelOverlayButton;
    @BindView(R.id.button_cancel_sms) FloatingActionButton mFloatingCancelSMS;
    @BindView(R.id.button_cancel_timer) FloatingActionButton mFloatingCancelTimerButton;

    // Layouts and overlays
    @BindView(R.id.frame) ViewGroup mRootView;
    @BindView(R.id.dialer_fragment) View mDialerFrame;
    @BindView(R.id.ongoing_call_layout) ConstraintLayout mOngoingCallLayout;
    @BindView(R.id.overlay_reject_call_options) ViewGroup mRejectCallOverlay;
    @BindView(R.id.overlay_answer_call_options) ViewGroup mAnswerCallOverlay;
    @BindView(R.id.overlay_action_timer) ViewGroup mActionTimerOverlay;
    @BindView(R.id.overlay_send_sms) ViewGroup mSendSmsOverlay;
    @Nullable ViewGroup mCurrentOverlay = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setThemeType(ThemeUtils.TYPE_TRANSPARENT_STATUS_BAR);
        setContentView(R.layout.activity_ongoing_call);
        PreferenceUtils.getInstance(this);
        Utilities.setUpLocale(this);

        ButterKnife.bind(this);

        // This activity needs to show even if the screen is off or locked
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

        //Detect a nav bar and adapt layout accordingly
        boolean hasNavBar = Utilities.hasNavBar(this);
        int navBarHeight = Utilities.navBarHeight(this);
        if (hasNavBar) {
            mOngoingCallLayout.setPadding(0, 0, 0, navBarHeight);
            mAnswerCallOverlay.setPadding(0, 0, 0, navBarHeight);
            mRejectCallOverlay.setPadding(0, 0, 0, navBarHeight);
            mDialerFrame.setPadding(0, 0, 0, navBarHeight);
        }

        // Display caller information
        displayInformation();

        // Initiate PowerManager and WakeLock (turn screen on/off according to distance from face)
        try {
            field = PowerManager.class.getField("PROXIMITY_SCREEN_OFF_WAKE_LOCK").getInt(null);
        } catch (Throwable ignored) {
        }
        powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(field, getLocalClassName());

        // Audio Manager
        mAudioManager = (AudioManager) getApplicationContext().getSystemService(AUDIO_SERVICE);

        // Fragments
        mDialpadFragment = DialpadFragment.newInstance(false);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.dialer_fragment, mDialpadFragment)
                .commit();
        mDialpadFragment.setDigitsCanBeEdited(false);
        mDialpadFragment.setShowVoicemailButton(false);
        mDialpadFragment.setOnKeyDownListener(this);

        View.OnClickListener rejectListener = v -> endCall();
        View.OnClickListener answerListener = v -> activateCall();
        LongClickOptionsListener.OverlayChangeListener overlayChangeListener = new LongClickOptionsListener.OverlayChangeListener() {
            @Override
            public boolean setOverlay(@NotNull ViewGroup view) {
                if (mCurrentOverlay == null) {
                    OngoingCallActivity.this.setOverlay(view);
                    return true;
                }
                return false;
            }

            @Override
            public void removeOverlay(@NotNull ViewGroup view) {
                OngoingCallActivity.this.removeOverlay(view);
            }
        };

        // Set OnLongClick listeners for answer/reject listeners
        mRejectLongClickListener = new LongClickOptionsListener(this, mRejectCallOverlay, rejectListener, overlayChangeListener);
        mAnswerLongClickListener = new LongClickOptionsListener(this, mAnswerCallOverlay, answerListener, overlayChangeListener);

        // Set OnTouch listeners for answer/reject buttons
        mRejectButton.setOnTouchListener(mRejectLongClickListener);
        mAnswerButton.setOnTouchListener(mAnswerLongClickListener);

        // Hide
        hideOverlays();
        hideButtons();

        // Set the correct text for the TextView
        String rejectCallSeconds = PreferenceUtils.getInstance().getString(R.string.pref_reject_call_timer_key);
        String rejectCallText = mRejectCallTimerText.getText() + " " + rejectCallSeconds + "s";
        mRejectCallTimerText.setText(rejectCallText);

        String answerCallSeconds = PreferenceUtils.getInstance().getString(R.string.pref_answer_call_timer_key);
        String answerCallText = mAnswerCallTimerText.getText() + " " + answerCallSeconds + "s";
        mAnswerCallTimerText.setText(answerCallText);

        // Initiate Swipe listener
        mIncomingCallSwipeListener = new AllPurposeTouchListener(this) {
            @Override
            public void onSwipeRight() {
                activateCall();
            }

            @Override
            public void onSwipeLeft() {
                endCall();
            }

            @Override
            public void onSwipeTop() {
                if (mKeypadButton.isShown()) {
                    mKeypadButton.performClick();
                }
            }
        };
        mOngoingCallLayout.setOnTouchListener(mIncomingCallSwipeListener);

        // Sms swipe listener
        mSmsOverlaySwipeListener = new AllPurposeTouchListener(this) {
            @Override
            public void onSwipeTop() {
                sendSMS(mFloatingSendSMSButton);
                removeOverlay(mSendSmsOverlay);
                mSendSmsOverlay.setOnTouchListener(null);
            }

            @Override
            public void onSwipeBottom() {
                removeOverlay(mSendSmsOverlay);
                mSendSmsOverlay.setOnTouchListener(null);
            }
        };

        // Instantiate ViewModels
        mSharedDialViewModel = ViewModelProviders.of(this).get(SharedDialViewModel.class);
        mSharedDialViewModel.getNumber().observe(this, s -> {
            if (s != null && !s.isEmpty()) {
                char c = s.charAt(s.length() - 1);
                CallManager.keypad(c);
            }
        });

        // Bottom Sheet Behaviour
        mBottomSheetBehavior = BottomSheetBehavior.from(mDialerFrame); // Set the bottom sheet behaviour
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN); // Hide the bottom sheet

        createNotificationChannel();
        createNotification();
    }

    // -- Overrides -- //

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        //Listen for call state changes
        CallManager.registerCallback(mCallback);
        updateUI(CallManager.getState());
    }

    /**
     * To disable back button
     */
    @Override
    public void onBackPressed() {
        // In case the dialpad is opened, pressing the back button will close it
        if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        // You cant press the back button in order to get out of the call
    }

    @Override
    protected void onStart() {
        super.onStart();
        mIsCreatingUI = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CallManager.unregisterCallback(mCallback); //The activity is gone, no need to listen to changes
        mActionTimer.cancel();
        releaseWakeLock();
        cancelNotification();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Utilities.PERMISSION_RC && Utilities.checkPermissionsGranted(grantResults)) {
            setSmsOverlay(mFloatingSendSMSButton);
        }
    }

    @Override
    public void onKeyPressed(int keyCode, KeyEvent event) {
        CallManager.keypad((char) event.getUnicodeChar());
    }
    // -- On Clicks -- //

    //TODO silence the ringing
    @OnClick(R.id.button_floating_reject_call_timer)
    public void startEndCallTimer(View view) {
        int seconds = Integer.parseInt(PreferenceUtils.getInstance().getString(R.string.pref_reject_call_timer_key));
        mActionTimer.setData(seconds * 1000, true);
        mActionTimer.start();
        setOverlay(mActionTimerOverlay);
    }

    /**
     * Starts call timer (answer)
     *
     * @param view
     */
    @OnClick(R.id.button_floating_answer_call_timer)
    public void startAnswerCallTimer(View view) {
        int seconds = Integer.parseInt(PreferenceUtils.getInstance().getString(R.string.pref_answer_call_timer_key));
        mActionTimer.setData(seconds * 1000, false);
        mActionTimer.start();
    }

    /**
     * Turns on mute according to current state (if already on/off)
     *
     * @param view
     */
    @OnClick(R.id.button_mute)
    public void toggleMute(View view) {
        Utilities.toggleViewActivation(view);
        if (view.isActivated()) {
            mMuteButton.setImageResource(R.drawable.ic_mic_off_black_24dp);
        } else {
            mMuteButton.setImageResource(R.drawable.ic_mic_black_24dp);
        }
        mAudioManager.setMicrophoneMute(view.isActivated());
    }

    /**
     * Turns on/off the speaker according to current state (if already on/off)
     *
     * @param view
     */
    @OnClick(R.id.button_speaker)
    public void toggleSpeaker(View view) {
        Utilities.toggleViewActivation(view);
        mAudioManager.setSpeakerphoneOn(view.isActivated());
    }

    /**
     * Puts the call on hold
     *
     * @param view
     */
    @OnClick(R.id.button_hold)
    public void toggleHold(View view) {
        Utilities.toggleViewActivation(view);
        CallManager.hold(view.isActivated());
    }

    //TODO add functionality to the Keypad button
    @OnClick(R.id.button_keypad)
    public void toggleKeypad(View view) {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    //TODO add functionality to the Add call button
    @OnClick(R.id.button_add_call)
    public void addCall(View view) {
//        CallManager.addCall(// a call);
    }

    /**
     * Cancel the call timer
     *
     * @param view
     */
    @OnClick(R.id.button_cancel_timer)
    public void cancelTimer(View view) {
        mActionTimer.cancel();
    }

    /**
     * Set the sms overlay (from which you can send sms)
     *
     * @param view
     */
    @OnClick(R.id.button_floating_send_sms)
    public void setSmsOverlay(View view) {
        if (Utilities.checkPermissionsGranted(this, Manifest.permission.SEND_SMS)) {
            setOverlay(mSendSmsOverlay);
            mSendSmsButton.setVisibility(View.VISIBLE);
            mSendSmsOverlay.setOnTouchListener(mSmsOverlaySwipeListener);
        } else {
            Utilities.askForPermission(this, Manifest.permission.SEND_SMS);
        }
    }

    /**
     * Send sms
     *
     * @param view
     */
    @OnClick(R.id.button_send_sms)
    public void sendSMS(View view) {
        String msg = mEditSms.getText().toString();
        String phoneNum = CallManager.getDisplayContact(this).getMainPhoneNumber();
        Utilities.sendSMS(this, phoneNum, msg);
        removeOverlay();
    }

    /**
     * Cancel sms overlay
     *
     * @param view
     */
    @OnClick(R.id.button_cancel_sms)
    public void cancelSMS(View view) {
        removeOverlay();
    }

    /**
     * Changes the color of the icon according to button status (activated or not)
     *
     * @param view
     */
    @OnClick({R.id.button_speaker, R.id.button_hold, R.id.button_mute})
    public void changeColors(View view) {
        ImageView imageButton = (ImageView) view;
        if (view.isActivated())
            imageButton.setColorFilter(getResources().getColor(R.color.white));
        else
            imageButton.setColorFilter(getResources().getColor(R.color.soft_black));
    }

    // -- Call Actions -- //

    /**
     * /*
     * Answers incoming call and changes the ui accordingly
     */
    private void activateCall() {
        CallManager.answer();
        switchToCallingUI();
    }

    /**
     * End current call / Incoming call and changes the ui accordingly
     */
    private void endCall() {
        mCallTimeHandler.sendEmptyMessage(TIME_STOP);
        CallManager.reject();
        releaseWakeLock();
        if (CallManager.isAutoCalling()) {
            finish();
            CallManager.nextCall(this);
        } else {
            (new Handler()).postDelayed(this::finish, END_CALL_MILLIS); // Delay the closing of the call
        }
    }

    // -- UI -- //

    private void displayInformation() {
        // Display the information about the caller
        Contact callerContact = CallManager.getDisplayContact(this);
        if (!callerContact.getName().isEmpty()) {
            if (callerContact.getName() != null) mCallerText.setText(callerContact.getName());
            if (callerContact.getPhotoUri() != null) {
                mPlaceholderImage.setVisibility(View.INVISIBLE);
                mPhotoImage.setVisibility(View.VISIBLE);
                mPhotoImage.setImageURI(Uri.parse(callerContact.getPhotoUri()));
            }
        } else {
            mCallerText.setText(callerContact.getMainPhoneNumber());
        }
    }

    /**
     * Updates the ui given the call state
     *
     * @param state the current call state
     */
    private void updateUI(int state) {
        @StringRes int statusTextRes;
        switch (state) {
            case Call.STATE_ACTIVE: // Ongoing
                statusTextRes = R.string.status_call_active;
                break;
            case Call.STATE_DISCONNECTED: // Ended
                statusTextRes = R.string.status_call_disconnected;
                break;
            case Call.STATE_RINGING: // Incoming
                statusTextRes = R.string.status_call_incoming;
                showBiometricPrompt(this);
                break;
            case Call.STATE_DIALING: // Outgoing
                statusTextRes = R.string.status_call_dialing;
                break;
            case Call.STATE_CONNECTING: // Connecting (probably outgoing)
                statusTextRes = R.string.status_call_dialing;
                break;
            case Call.STATE_HOLDING: // On Hold
                statusTextRes = R.string.status_call_holding;
                break;
            default:
                statusTextRes = R.string.status_call_active;
                break;
        }
        mStatusText.setText(statusTextRes);
        if (state != Call.STATE_RINGING && state != Call.STATE_DISCONNECTED) switchToCallingUI();
        if (state == Call.STATE_DISCONNECTED) endCall();
        mState = state;
        mStateText = getString(statusTextRes);
        mBuilder.setContentText(mStateText);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    /**
     * Update the current call time ui
     */
    private void updateTimeUI() {
        mTimeText.setText(mCallTimer.getStringTime());
    }

    /**
     * Switches the ui to an active call ui.
     */
    private void switchToCallingUI() {
        if (mIsCallingUI) return;
        else mIsCallingUI = true;
        mAudioManager.setMode(AudioManager.MODE_IN_CALL);
        acquireWakeLock();
        mCallTimeHandler.sendEmptyMessage(TIME_START); // Starts the call timer

        // Change the buttons layout
        mAnswerButton.hide();
        mHoldButton.setVisibility(View.VISIBLE);
        mMuteButton.setVisibility(View.VISIBLE);
        mKeypadButton.setVisibility(View.VISIBLE);
        mSpeakerButton.setVisibility(View.VISIBLE);
        mAddCallButton.setVisibility(View.VISIBLE);
        moveRejectButtonToMiddle();
    }

    /**
     * Moves the reject button to the middle
     */
    private void moveRejectButtonToMiddle() {
        ConstraintSet ongoingSet = new ConstraintSet();
        ongoingSet.clone(mOngoingCallLayout);
        ongoingSet.connect(R.id.reject_btn, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.END);
        ongoingSet.connect(R.id.reject_btn, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.START);
        ongoingSet.setHorizontalBias(R.id.reject_btn, 0.5f);

        ongoingSet.setMargin(R.id.reject_btn, ConstraintSet.END, 0);

        ConstraintSet overlaySet = new ConstraintSet();
        overlaySet.clone(this, R.layout.correction_overlay_reject_call_options);

        if (!mIsCreatingUI) { //Don't animate if the activity is just being created
            Transition transition = new ChangeBounds();
            transition.setInterpolator(new AccelerateDecelerateInterpolator());
            transition.addTarget(mRejectCallOverlay);
            transition.addTarget(mRejectButton);
            TransitionManager.beginDelayedTransition(mOngoingCallLayout, transition);
        }

        ongoingSet.applyTo(mOngoingCallLayout);
        overlaySet.applyTo((ConstraintLayout) mRejectCallOverlay);

        mFloatingRejectCallTimerButton.hide();
        mFloatingCancelOverlayButton.hide();
        mFloatingSendSMSButton.hide();

        mRootView.removeView(mAnswerCallOverlay);
    }

    /**
     * Sets the action button (call actions) as clickable/not clickable
     *
     * @param clickable
     */
    private void setActionButtonsClickable(boolean clickable) {
        for (int i = 0; i < mOngoingCallLayout.getChildCount(); i++) {
            View v = mOngoingCallLayout.getChildAt(i);
            if (v instanceof FloatingActionButton || v instanceof ImageView) {
                v.setClickable(clickable);
                v.setFocusable(false);
            }
        }
    }

    /**
     * Hide all buttons
     */
    private void hideButtons() {
        // hide buttons
        mFloatingRejectCallTimerButton.hide();
        mFloatingAnswerCallTimerButton.hide();
        mFloatingCancelOverlayButton.hide();
        mFloatingSendSMSButton.hide();
        mFloatingCancelSMS.hide();
        mFloatingCancelTimerButton.hide();
        mSendSmsButton.setVisibility(View.GONE);
    }

    /**
     * Hide all overlays
     */
    private void hideOverlays() {
        //Hide all overlays
        mActionTimerOverlay.setAlpha(0.0f);
        mAnswerCallOverlay.setAlpha(0.0f);
        mRejectCallOverlay.setAlpha(0.0f);
        mSendSmsOverlay.setAlpha(0.0f);
    }

    // -- Overlays -- //

    private void setLayerEnabled(@NotNull ViewGroup layer, boolean isEnabled) {
        for (int i = 0; i < layer.getChildCount(); i++) {
            View v = layer.getChildAt(i);
            if (isEnabled) { // If we want to enable it
                if (v instanceof FloatingActionButton) {
                    ((FloatingActionButton) v).show();
                } else {
                    v.setVisibility(View.VISIBLE);
                }
            } else { // If we don't
                if (v instanceof FloatingActionButton) { // Make it non-clickable
                    ((FloatingActionButton) v).hide();
                } else if (v instanceof Button) {
                    v.setVisibility(View.GONE);
                } else { // Don't move any other views that are constrained to it
                    v.setVisibility(View.INVISIBLE);
                }
                v.setHovered(false);
            }
        }
    }

    /**
     * Set a given overlay as visible
     *
     * @param overlay
     */
    private void setOverlay(@NotNull ViewGroup overlay) {
        if (mCurrentOverlay != null) {
            removeOverlay(mCurrentOverlay);
        }
        setActionButtonsClickable(false);

        mCurrentOverlay = overlay;
        mCurrentOverlay.setAlpha(0.0f);
        mCurrentOverlay.animate().alpha(1.0f);

        setLayerEnabled(overlay, true);
    }

    /**
     * Removes a given overlay as visible
     *
     * @param overlay
     */
    private void removeOverlay(@NotNull ViewGroup overlay) {
        if (overlay == mCurrentOverlay) {
            setActionButtonsClickable(true);
            //Animate fade, and then 'remove' the overlay
            overlay.animate()
                    .alpha(0.0f);

            mCurrentOverlay.setOnTouchListener(null);
            mCurrentOverlay = null;
            setLayerEnabled(overlay, false);
        }
    }

    /**
     * Removes the current overlay as the visible overlay
     */
    private void removeOverlay() {
        if (mCurrentOverlay != null) {
            removeOverlay(mCurrentOverlay);
        }
    }


    // -- Wake Lock -- //

    /**
     * Acquires the wake lock
     */
    private void acquireWakeLock() {
        if (!wakeLock.isHeld()) {
            wakeLock.acquire(10 * 60 * 1000L /*10 minutes*/);
        }
    }

    /**
     * Releases the wake lock
     */
    private void releaseWakeLock() {
        if (wakeLock.isHeld()) {
            wakeLock.release();
        }
    }

    // -- Classes -- //

    class ActionTimer {

        CountDownTimer mTimer = null;
        boolean mIsRejecting = true;

        Integer oldVolume;

        private void setData(long millisInFuture, boolean isRejecting) {
            mIsRejecting = isRejecting;
            @ColorRes int textColorRes;
            @StringRes int textIndicator;
            if (isRejecting) {
                textColorRes = R.color.red_phone;
                textIndicator = R.string.reject_timer_indicator;
            } else {
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
                    end();
                }
            };
        }

        private void start() {
            oldVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_RING);
            mAudioManager.setStreamVolume(AudioManager.STREAM_RING, 0, 0);
            if (mTimer != null) mTimer.start();
            else Timber.w("Couldn't start action timer (timer is null)");

            if (mActionTimerOverlay != null) setOverlay(mActionTimerOverlay);
        }

        private void cancel() {
            if (mTimer != null) mTimer.cancel();
            else Timber.w("Couldn't cancel action timer (timer is null)");

            finalEndCommonMan();
        }

        private void end() {
            if (mIsRejecting) endCall();
            else activateCall();

            finalEndCommonMan();
        }

        private void finalEndCommonMan() {
            if (oldVolume != null)
                mAudioManager.setStreamVolume(AudioManager.STREAM_RING, oldVolume, 0);
            removeOverlay();
        }
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

    @SuppressLint("HandlerLeak")
    class CallTimeHandler extends Handler {
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
    }

    // -- Notification -- //
    public void createNotification() {

        Contact callerContact = CallManager.getDisplayContact(this);
        String callerName = callerContact.getName();

        Intent touchNotification = new Intent(this, OngoingCallActivity.class);
        touchNotification.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, touchNotification, 0);

        // Answer Button Intent
        Intent answerIntent = new Intent(this, NotificationActionReceiver.class);
        answerIntent.setAction(ACTION_ANSWER);
        answerIntent.putExtra(EXTRA_NOTIFICATION_ID, 0);
        PendingIntent answerPendingIntent = PendingIntent.getBroadcast(this, 0, answerIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        // Hangup Button Intent
        Intent hangupIntent = new Intent(this, NotificationActionReceiver.class);
        hangupIntent.setAction(ACTION_HANGUP);
        hangupIntent.putExtra(EXTRA_NOTIFICATION_ID, 0);
        PendingIntent hangupPendingIntent = PendingIntent.getBroadcast(this, 1, hangupIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_full_144)
                .setContentTitle(callerName)
                .setContentText(mStateText)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setColor(ThemeUtils.getAccentColor(this))
                .setOngoing(true)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(0, 1))
                .setAutoCancel(true);

        // Adding the action buttons
        mBuilder.addAction(R.drawable.ic_call_black_24dp, getString(R.string.action_answer), answerPendingIntent);
        mBuilder.addAction(R.drawable.ic_call_end_black_24dp, getString(R.string.action_hangup), hangupPendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    /**
     * Creates the notification channel
     * Which allows and manages the displaying of the notification
     */
    public void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            mNotificationManager = getSystemService(NotificationManager.class);
            mNotificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Removes the notification
     */
    public void cancelNotification() {
        String ns = this.NOTIFICATION_SERVICE;
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(ns);
        notificationManager.cancel(NOTIFICATION_ID);
    }

}
