package com.chooloo.www.callmanager.ui.dialpad;

import android.os.Bundle;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.ui.base.BaseBottomSheetDialogFragment;
import com.chooloo.www.callmanager.ui.base.BaseFragment;
import com.chooloo.www.callmanager.ui.widgets.DialpadEditText;
import com.chooloo.www.callmanager.ui.widgets.DialpadKey;
import com.chooloo.www.callmanager.util.AudioUtils;
import com.chooloo.www.callmanager.util.CallManager;
import com.chooloo.www.callmanager.util.Utilities;
import com.chooloo.www.callmanager.viewmodel.SharedDialViewModel;
import com.chooloo.www.callmanager.viewmodel.SharedIntentViewModel;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class DialpadBottomDialogFragment extends BaseBottomSheetDialogFragment implements DialpadMvpView {

    public static final String TAG = "dialpad_bottom_dialog_fragment";
    public static final String ARG_DIALER = "dialer";

    private boolean mIsDialer = true;

    private DialpadMvpPresenter<DialpadMvpView> mPresenter;
    private OnKeyDownListener mOnKeyDownListener = null;
    private SharedDialViewModel mSharedDialViewModel;
    private AudioUtils mAudioUtils;

    @BindView(R.id.key_0) DialpadKey mKey0;
    @BindView(R.id.key_1) DialpadKey mKey1;
    @BindView(R.id.key_2) DialpadKey mKey2;
    @BindView(R.id.key_3) DialpadKey mKey3;
    @BindView(R.id.key_4) DialpadKey mKey4;
    @BindView(R.id.key_5) DialpadKey mKey5;
    @BindView(R.id.key_6) DialpadKey mKey6;
    @BindView(R.id.key_7) DialpadKey mKey7;
    @BindView(R.id.key_8) DialpadKey mKey8;
    @BindView(R.id.key_9) DialpadKey mKey9;
    @BindView(R.id.key_hex) DialpadKey mKeyHex;
    @BindView(R.id.key_star) DialpadKey mKeyStar;

    @BindView(R.id.dialpad_edit_text) DialpadEditText mDigits;
    @BindView(R.id.dialpad_button_call) ImageView mCallButton;
    @BindView(R.id.dialpad_button_delete) ImageView mDeleteButton;
    @BindView(R.id.dialpad_keys_layout) TableLayout mNumbersTable;

    public static DialpadBottomDialogFragment newInstance(boolean isDialer) {
        Bundle args = new Bundle();
        args.putBoolean(ARG_DIALER, isDialer);
        DialpadBottomDialogFragment fragment = new DialpadBottomDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialpad, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDetach();
    }

    @OnClick({R.id.key_0, R.id.key_1, R.id.key_2, R.id.key_3, R.id.key_4, R.id.key_5,
            R.id.key_6, R.id.key_7, R.id.key_8, R.id.key_9, R.id.key_star, R.id.key_hex})
    public void keyClick(View view) {
        int keyCode = ((DialpadKey) view).getKeyCode();
        mPresenter.onKeyClick(keyCode);
    }

    @OnClick(R.id.dialpad_edit_text)
    public void onDigitsClick(View view) {
        mPresenter.onDigitsClick();
    }

    @OnClick(R.id.dialpad_button_delete)
    public void onDeleteClick(View view) {
        mPresenter.onDeleteClick();
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

    @OnLongClick(R.id.dialpad_button_delete)
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
        mPresenter.onAttach(this);

        mAudioUtils = new AudioUtils();

        setIsDialer(getArgsSafely().getBoolean(ARG_DIALER));

        mViewModelProvider = new ViewModelProvider(this);

        mSharedDialViewModel = mViewModelProvider.get(SharedDialViewModel.class);

        SharedIntentViewModel sharedIntentViewModel = mViewModelProvider.get(SharedIntentViewModel.class);
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
    public void setIsDialer(boolean isDialer) {
        mIsDialer = isDialer;
        mDeleteButton.setVisibility(isDialer ? VISIBLE : GONE);
        mCallButton.setVisibility(isDialer ? VISIBLE : GONE);
        mDigits.setClickable(isDialer);
        mDigits.setLongClickable(isDialer);
        mDigits.setFocusableInTouchMode(isDialer);
        mDigits.setCursorVisible(isDialer);
    }

    @Override
    public void updateViewModel(String number) {
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
    public void backspace() {
        String number = mDigits.getNumbers();
        if (number != null && number.length() > 0) {
            mDigits.setText(number.substring(0, number.length() - 1));
        }
    }

    @Override
    public void vibrate() {
        Utilities.vibrate(mActivity, Utilities.SHORT_VIBRATE_LENGTH);
    }

    @Override
    public void toggleToneGenerator(boolean toggle) {
        mAudioUtils.toggleToneGenerator(toggle);
    }

    @Override
    public void showDeleteButton(boolean isShow) {
        mDeleteButton.setVisibility(isShow ? VISIBLE : GONE);
    }

    @Override
    public void stopTone() {
        mAudioUtils.stopTone();
    }

    @Override
    public void playTone(int keyCode) {
        mAudioUtils.playToneByKey(keyCode, mActivity);
    }


    public void setOnKeyDownListener(OnKeyDownListener onKeyDownListener) {
        mOnKeyDownListener = onKeyDownListener;
    }

    public interface OnKeyDownListener {
        void onKeyPressed(int keyCode, KeyEvent event);
    }
}
