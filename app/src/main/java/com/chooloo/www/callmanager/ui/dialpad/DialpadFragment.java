package com.chooloo.www.callmanager.ui.dialpad;

import android.os.Bundle;
import android.os.Handler;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.ui.base.BaseFragment;
import com.chooloo.www.callmanager.ui.widgets.Dialpad;
import com.chooloo.www.callmanager.ui.widgets.DialpadKey;
import com.chooloo.www.callmanager.ui.widgets.EditText;
import com.chooloo.www.callmanager.util.AudioUtils;
import com.chooloo.www.callmanager.util.CallManager;
import com.chooloo.www.callmanager.util.Utilities;
import com.chooloo.www.callmanager.viewmodel.SharedDialViewModel;
import com.chooloo.www.callmanager.viewmodel.SharedIntentViewModel;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class DialpadFragment extends BaseFragment implements DialpadMvpView {

    private DialpadPresenter<DialpadMvpView> mPresenter;
    public static final String ARG_DIALER = "dialer";

    private boolean mIsDialer = true;

    private HashMap<Integer, Integer> mKeyIdToKeyCode;

    private OnKeyDownListener mOnKeyDownListener = null;

    private SharedDialViewModel mSharedDialViewModel;

    private AudioUtils mAudioUtils;

    @BindView(R.id.digits_edit_text) EditText mDigits;
    @BindView(R.id.button_call) ImageView mCallButton;
    @BindView(R.id.button_delete) ImageView mDelButton;
    @BindView(R.id.dialpad) TableLayout mNumbersTable;
    @BindView(R.id.dialpad_view) Dialpad mDialpadView;

    public static DialpadFragment newInstance(boolean isDialer) {
        Bundle args = new Bundle();
        args.putBoolean(ARG_DIALER, isDialer);
        DialpadFragment fragment = new DialpadFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.dialpad_fragment, container, false);
        fragmentView.buildLayer();
        return fragmentView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDetach();
    }

    @OnClick({R.id.key_0, R.id.key_1, R.id.key_2, R.id.key_3, R.id.key_4, R.id.key_5,
            R.id.key_6, R.id.key_7, R.id.key_8, R.id.key_9, R.id.key_star, R.id.key_hex, R.id.button_delete})
    public void keyClick(View view) {
        mPresenter.onKeyClick(((DialpadKey) view).getKeyCode());
    }

    @OnClick(R.id.button_call)
    public void callClick(View view) {
        mPresenter.onCallClick();
    }

    @OnClick(R.id.digits_edit_text)
    public void onDigitsClick(View view) {
        mPresenter.onDigitsClick();
    }

    @OnLongClick(R.id.key_1)
    public boolean longOneClick(View view) {
        mPresenter.onLongOneClick();
        return true;
    }

    @OnLongClick(R.id.key_0)
    public boolean longZeroClick(View view) {
        mPresenter.onLongZeroClick();
        return true;
    }

    @OnLongClick(R.id.button_delete)
    public boolean longDeleteClick(View view) {
        mPresenter.onLongDeleteClick();
        return true;
    }

    public boolean isDialer() {
        return mIsDialer;
    }

    @Override
    public void setUp() {
        mPresenter = new DialpadPresenter<>();
        mPresenter.onAttach(this, getLifecycle());

        mAudioUtils = new AudioUtils();

        mIsDialer = getArgsSafely().getBoolean(ARG_DIALER);
        mCallButton.setVisibility(mIsDialer ? View.VISIBLE : View.GONE);
        mDelButton.setVisibility(mIsDialer ? View.VISIBLE : View.GONE);

        mSharedDialViewModel = ViewModelProviders.of(mActivity).get(SharedDialViewModel.class);
        SharedIntentViewModel sharedIntentViewModel = ViewModelProviders.of(mActivity).get(SharedIntentViewModel.class);
        sharedIntentViewModel.getData().observe(getViewLifecycleOwner(), data -> mPresenter.onIntentDataChanged(data));

        mDigits.addTextChangedListener(new PhoneNumberFormattingTextWatcher(Utilities.sLocale.getCountry()));
        mDigits.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPresenter.onTextChanged(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void setNumber(String number) {
        mDigits.setText(number);
    }

    @Override
    public void setViewModelNumber(String number) {
        mSharedDialViewModel.setNumber(number.equals("") ? null : number);
    }

    @Override
    public void call() {
        String number = mSharedDialViewModel.getNumber().getValue();
        if (number.equals("") || number.isEmpty()) {
            Toast.makeText(getContext(), getString(R.string.please_enter_a_number), Toast.LENGTH_SHORT).show();
        } else {
            CallManager.call(mActivity, mDigits.getNumbers());
        }
    }

    @Override
    public void callVoicemail() {
        CallManager.callVoicemail(mActivity);
    }

    @Override
    public void requestFocus() {
        mDigits.requestFocus();
    }

    @Override
    public void toggleToneGenerator(boolean toggle) {
        mAudioUtils.toggleToneGenerator(toggle);
    }

    @Override
    public void stopTone() {
        mAudioUtils.stopTone();
    }

    @Override
    public void playTone(int keyCode) {
        mAudioUtils.playToneByKey(keyCode, mActivity);
    }

    @Override
    public void toggleCursor(boolean isShow) {
        if (isShow && mDigits.isEmpty()) {
            mDigits.setCursorVisible(true);
        } else if (!isShow) {
            final int length = mDigits.length();
            if (length == mDigits.getSelectionStart() && length == mDigits.getSelectionEnd()) {
                mDigits.setCursorVisible(false);
            }
        }
    }

    @Override
    public void registerKeyEvent(int keyCode) {
        KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
        mDigits.onKeyDown(keyCode, event);
        if (mOnKeyDownListener != null) mOnKeyDownListener.onKeyPressed(keyCode, event);
    }

    @Override
    public void vibrate() {
        Utilities.vibrate(mActivity, Utilities.SHORT_VIBRATE_LENGTH);
    }

    @Override
    public void setDigitsCanBeEdited(boolean isCanBeEdited) {
        Handler handler = new Handler();
        handler.postDelayed(() -> mDialpadView.setDigitsCanBeEdited(isCanBeEdited), 2000);
    }

    @Override
    public void showVoicemailButton(boolean isShow) {
        Handler handler = new Handler();
        handler.postDelayed(() -> mDialpadView.setShowVoicemailButton(isShow), 2000);
    }

    public void setOnKeyDownListener(OnKeyDownListener onKeyDownListener) {
        mOnKeyDownListener = onKeyDownListener;
    }

    public interface OnKeyDownListener {
        void onKeyPressed(int keyCode, KeyEvent event);
    }
}
