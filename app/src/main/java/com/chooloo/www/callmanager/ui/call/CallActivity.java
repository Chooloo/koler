package com.chooloo.www.callmanager.ui.call;

import android.media.AudioManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telecom.Call;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.databinding.ActivityCallBinding;
import com.chooloo.www.callmanager.entity.Contact;
import com.chooloo.www.callmanager.ui.base.BaseActivity;
import com.chooloo.www.callmanager.ui.dialpad.DialpadBottomDialogFragment;
import com.chooloo.www.callmanager.util.CallManager;
import com.chooloo.www.callmanager.util.Utilities;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.chooloo.www.callmanager.util.BiometricUtils.showBiometricPrompt;

public class CallActivity extends BaseActivity implements CallMvpView {
    private CallMvpPresenter<CallMvpView> mPresenter;

    private ActivityCallBinding binding;

    private DialpadBottomDialogFragment mDialpadFragment;

    private AudioManager mAudioManager;

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

        mDialpadFragment = DialpadBottomDialogFragment.newInstance(false);
        mDialpadFragment.setOnKeyDownListener((keyCode, event) -> mPresenter.onDialpadKeyClick(keyCode, event));

        updateCallerInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDetach();
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
    public void showDialpad(boolean isShow) {
        if (isShow) {
            mDialpadFragment.show(getSupportFragmentManager(), mDialpadFragment.getTag());
        } else {
            mDialpadFragment.dismiss();
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
    public void updateCallerInfo() {
        Contact caller = CallManager.getDisplayContact(this);
        boolean hasPhoto = caller.getPhotoUri() != null;

        binding.textCaller.setText(caller.getName());
        binding.imagePlaceholder.setVisibility(hasPhoto ? VISIBLE : GONE);
        binding.imagePhoto.setVisibility(hasPhoto ? VISIBLE : GONE);
        binding.itemImageLayout.setVisibility(hasPhoto ? VISIBLE : GONE);
    }

    @Override
    public void updateState(int state) {
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
        binding.textStatus.setText(statusTextRes);
    }

    @Override
    public void moveHangupButtonToMiddle() {

    }

    @Override
    public void switchToActiveCallUI() {

    }

    @Override
    public void acquireWakeLock() {

    }

    @Override
    public void releaseWakeLock() {

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
}