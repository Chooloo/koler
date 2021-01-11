package com.chooloo.www.callmanager.ui.call;

import android.app.KeyguardManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.telecom.Call;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.transition.ChangeBounds;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.databinding.ActivityCallBinding;
import com.chooloo.www.callmanager.entity.Contact;
import com.chooloo.www.callmanager.ui.base.BaseActivity;
import com.chooloo.www.callmanager.ui.dialpad.DialpadBottomDialogFragment;
import com.chooloo.www.callmanager.service.CallManager;
import com.chooloo.www.callmanager.util.Utilities;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.view.WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD;
import static android.view.WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
import static android.view.WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON;
import static com.chooloo.www.callmanager.util.BiometricUtils.showBiometricPrompt;

public class CallActivity extends BaseActivity implements CallMvpView {
    private CallMvpPresenter<CallMvpView> mPresenter;

    private ActivityCallBinding binding;

    private DialpadBottomDialogFragment mBottomDialpadFragment;

    private AudioManager mAudioManager;

    private PowerManager.WakeLock mWakeLock;

    private Call.Callback mCallCallback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCallBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    public void onSetup() {
        mPresenter = new CallPresenter<>();
        mPresenter.onAttach(this);

        mAudioManager = (AudioManager) getApplicationContext().getSystemService(AUDIO_SERVICE);
        mAudioManager.setMode(AudioManager.MODE_IN_CALL);

        mBottomDialpadFragment = DialpadBottomDialogFragment.newInstance(false);
        mBottomDialpadFragment.mDialpadFragment.setOnKeyDownListener((keyCode, event) -> mPresenter.onDialpadKeyClick(keyCode, event));

        // TODO move this to a util class
        mWakeLock = ((PowerManager) getSystemService(POWER_SERVICE)).newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, getLocalClassName());
        if (!mWakeLock.isHeld()) {
            mWakeLock.acquire(10 * 60 * 1000L /*10 minutes*/);
        }

        binding.answerBtn.setOnClickListener(view -> mPresenter.onRejectClick());
        binding.rejectBtn.setOnClickListener(view -> mPresenter.onAnswerClick());
        binding.buttonAddCall.setOnClickListener(view -> mPresenter.onAddCallClick());
        binding.buttonKeypad.setOnClickListener(view -> mPresenter.onKeypadClick());
        binding.buttonMute.setOnClickListener(view -> mPresenter.onMuteClick(view.isActivated()));
        binding.buttonSpeaker.setOnClickListener(view -> mPresenter.onSpeakerClick(view.isActivated()));
        binding.buttonHold.setOnClickListener(view -> mPresenter.onHoldClick(view.isActivated()));

        mCallCallback = new Call.Callback() {
            @Override
            public void onStateChanged(Call call, int state) {
                super.onStateChanged(call, state);
                mPresenter.onStateChanged();
            }

            @Override
            public void onDetailsChanged(Call call, Call.Details details) {
                super.onDetailsChanged(call, details);
                mPresenter.onDetailsChanged();
            }
        };
        CallManager.registerCallback(mCallCallback);

        updateCall();
        updateState();

        if (Utilities.hasNavBar(this)) {
            binding.getRoot().setPadding(0, 0, 0, Utilities.navBarHeight(this));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
        } else {
            getWindow().addFlags(FLAG_SHOW_WHEN_LOCKED | FLAG_TURN_SCREEN_ON);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ((KeyguardManager) getSystemService(KEYGUARD_SERVICE)).requestDismissKeyguard(this, null);
        } else {
            getWindow().addFlags(FLAG_DISMISS_KEYGUARD);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDetach();
        CallManager.unregisterCallback(mCallCallback);
        if (mWakeLock.isHeld()) {
            mWakeLock.release();
        }
    }

    @Override
    public void onBackPressed() {
        if (!mPresenter.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public void answer() {
        CallManager.answer();
    }

    @Override
    public void reject() {
        CallManager.reject();
    }

    @Override
    public void toggleHold(boolean isHold) {
        CallManager.hold(isHold);
    }

    @Override
    public void toggleMute(boolean isMute) {
        binding.buttonMute.setImageResource(isMute ? R.drawable.ic_mic_off_black_24dp : R.drawable.ic_mic_black_24dp);
        mAudioManager.setMicrophoneMute(isMute);
    }

    @Override
    public void toggleSpeaker(boolean isSpeaker) {
        mAudioManager.setSpeakerphoneOn(isSpeaker);
    }

    @Override
    public void toggleDialpad(boolean isShow) {
        if (isShow) {
            mBottomDialpadFragment.show(getSupportFragmentManager(), mBottomDialpadFragment.getTag());
        } else {
            mBottomDialpadFragment.dismiss();
        }
    }

    @Override
    public void pressDialpadKey(char keyChar) {
        CallManager.keypad(keyChar);
    }

    @Override
    public void addCall() {
        // TODO implement
    }

    @Override
    public void updateCall() {
        Contact caller = CallManager.getDisplayContact(this);
        boolean hasPhoto = caller.getPhotoUri() != null;

        binding.textCaller.setText(caller.getName());
        binding.imagePlaceholder.setVisibility(hasPhoto ? VISIBLE : GONE);
        binding.imagePhoto.setVisibility(hasPhoto ? VISIBLE : GONE);
        binding.itemImageLayout.setVisibility(hasPhoto ? VISIBLE : GONE);
    }

    @Override
    public void updateState() {
        @StringRes int statusTextRes;
        switch (CallManager.getState()) {
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
        binding.textStatus.setText(statusTextRes);
    }

    @Override
    public void switchToActiveCallUI() {
        mAudioManager.setMode(AudioManager.MODE_IN_CALL);
        moveHangupButtonToMiddle();
        if (!mWakeLock.isHeld()) mWakeLock.acquire(10 * 60 * 1000L);
        // TODO Group all action buttons to be under one layout and set layout gont here
    }

    @Override
    public void moveHangupButtonToMiddle() {
        ConstraintSet ongoingSet = new ConstraintSet();

        ongoingSet.clone(binding.getRoot());
        ongoingSet.connect(binding.rejectBtn.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.END);
        ongoingSet.connect(binding.rejectBtn.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.START);
        ongoingSet.setMargin(binding.rejectBtn.getId(), ConstraintSet.END, 0);
        ongoingSet.setMargin(binding.rejectBtn.getId(), ConstraintSet.START, 0);

        Transition transition = new ChangeBounds();
        transition.setInterpolator(new AccelerateDecelerateInterpolator());
        transition.addTarget(binding.rejectBtn);
        TransitionManager.beginDelayedTransition(binding.getRoot(), transition);

        ongoingSet.applyTo(binding.getRoot());
    }

    @Override
    public void createNotification() {

    }

    @Override
    public void createNotificationChannel() {

    }

    @Override
    public void cancelNotification() {
    }

    @Override
    public boolean isDialpadOpened() {
        return mBottomDialpadFragment.isVisible();
    }
}