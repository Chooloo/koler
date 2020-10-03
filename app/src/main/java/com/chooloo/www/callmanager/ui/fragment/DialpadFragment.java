package com.chooloo.www.callmanager.ui.fragment;

import android.annotation.SuppressLint;
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
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.ViewModelProviders;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.ui.fragment.base.AbsBaseFragment;
import com.chooloo.www.callmanager.ui.widgets.DialpadView;
import com.chooloo.www.callmanager.ui.widgets.DigitsEditText;
import com.chooloo.www.callmanager.util.AudioUtils;
import com.chooloo.www.callmanager.util.CallManager;
import com.chooloo.www.callmanager.util.PreferenceUtils;
import com.chooloo.www.callmanager.util.Utilities;
import com.chooloo.www.callmanager.viewmodel.SharedDialViewModel;
import com.chooloo.www.callmanager.viewmodel.SharedIntentViewModel;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;
import timber.log.Timber;

import static android.telephony.PhoneNumberUtils.WAIT;

public class DialpadFragment extends AbsBaseFragment {

    public static final String ARG_DIALER = "dialer";
    private static final String TAG = DialpadFragment.class.getSimpleName();

    @BindView(R.id.digits_edit_text) DigitsEditText mDigits;
    @BindView(R.id.button_call) ImageView mCallButton;
    @BindView(R.id.button_delete) ImageView mDelButton;
    @BindView(R.id.dialpad) TableLayout mNumbersTable;
    @BindView(R.id.dialpad_view) DialpadView mDialpadView;

    private OnKeyDownListener mOnKeyDownListener = null;
    private SharedDialViewModel mSharedDialViewModel;
    private SharedIntentViewModel mSharedIntentViewModel;
    private PhoneNumberFormattingTextWatcher mPhoneNumberFormattingTextWatcher;
    private AudioUtils mAudioUtils = new AudioUtils();

    private boolean mIsDialer = true;

    public static DialpadFragment newInstance(boolean isDialer) {
        Bundle args = new Bundle();
        args.putBoolean(ARG_DIALER, isDialer);
        DialpadFragment fragment = new DialpadFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // view models
        mSharedDialViewModel = ViewModelProviders.of(getActivity()).get(SharedDialViewModel.class);
        mSharedIntentViewModel = ViewModelProviders.of(getActivity()).get(SharedIntentViewModel.class);
        mSharedIntentViewModel.getData().observe(this, d -> setNumber(d));

        // formats the phone number text in realtime
        mPhoneNumberFormattingTextWatcher = new PhoneNumberFormattingTextWatcher(Utilities.sLocale.getCountry());
//        mDigits.addTextChangedListener(mPhoneNumberFormattingTextWatcher);
        mDigits.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mSharedDialViewModel.setNumber(s == "" ? null : Utilities.getOnlyNumbers(mDigits.getText().toString()));
                Timber.i("TEXT CHANGED: %s", mDigits.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onFragmentReady() {
        Bundle args = getArguments();
        if (args == null)
            throw new IllegalArgumentException("You must create this fragment with newInstance()");

        mIsDialer = args.getBoolean(ARG_DIALER);

        mCallButton.setVisibility(mIsDialer ? View.VISIBLE : View.GONE);
        mDelButton.setVisibility(mIsDialer ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) mDigits.requestFocus();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceUtils.getInstance(getContext());
        Utilities.setUpLocale(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.dialpad_fragment, container, false);
        fragmentView.buildLayer();
        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mAudioUtils.toggleToneGenerator(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        mAudioUtils.stopTone();
        mAudioUtils.toggleToneGenerator(false);
    }

    /**
     * Returns true of the newDigit parameter can be added at the current selection
     * point, otherwise returns false.
     * Only prevents input of WAIT and PAUSE digits at an unsupported position.
     * Fails early if start == -1 or start is larger than end.
     */
    @VisibleForTesting
    /* package */ static boolean canAddDigit(CharSequence digits, int start, int end,
                                             char newDigit) {
        // False if no selection, or selection is reversed (end < start)
        if (start == -1 || end < start) return false;

        // unsupported selection-out-of-bounds state
        if (start > digits.length() || end > digits.length()) return false;

        // Special digit cannot be the first digit
        if (start == 0) return false;
        if (newDigit == WAIT) {
            if (digits.charAt(start - 1) == WAIT) return false; // preceding char is ';' (WAIT)
            return (digits.length() <= end) || (digits.charAt(end) != WAIT); // next char is ';' (WAIT)
        }
        return true;
    }

    /**
     * Dialer buttons click handler
     *
     * @param view is the button number
     */
    @OnClick({R.id.key_0, R.id.key_1, R.id.key_2, R.id.key_3, R.id.key_4, R.id.key_5,
            R.id.key_6, R.id.key_7, R.id.key_8, R.id.key_9, R.id.key_star, R.id.key_hex})
    public void addChar(View view) {
        switch (view.getId()) {
            case R.id.key_1: {
                keyPressed(KeyEvent.KEYCODE_1);
                break;
            }
            case R.id.key_2: {
                keyPressed(KeyEvent.KEYCODE_2);
                break;
            }
            case R.id.key_3: {
                keyPressed(KeyEvent.KEYCODE_3);
                break;
            }
            case R.id.key_4: {
                keyPressed(KeyEvent.KEYCODE_4);
                break;
            }
            case R.id.key_5: {
                keyPressed(KeyEvent.KEYCODE_5);
                break;
            }
            case R.id.key_6: {
                keyPressed(KeyEvent.KEYCODE_6);
                break;
            }
            case R.id.key_7: {
                keyPressed(KeyEvent.KEYCODE_7);
                break;
            }
            case R.id.key_8: {
                keyPressed(KeyEvent.KEYCODE_8);
                break;
            }
            case R.id.key_9: {
                keyPressed(KeyEvent.KEYCODE_9);
                break;
            }
            case R.id.key_0: {
                keyPressed(KeyEvent.KEYCODE_0);
                break;
            }
            case R.id.key_hex: {
                keyPressed(KeyEvent.KEYCODE_POUND);
                break;
            }
            case R.id.key_star: {
                keyPressed(KeyEvent.KEYCODE_STAR);
                break;
            }
        }
    }

    /**
     * Deletes a number from the keypad's input when the delete button is clicked
     */
    @OnClick(R.id.button_delete)
    public void delNum(View view) {
        keyPressed(KeyEvent.KEYCODE_DEL);
    }

    /**
     * Calls the number in the keypad's input
     */
    @OnClick(R.id.button_call)
    public void call(View view) {
        String number = mSharedDialViewModel.getNumber().getValue();
        if (number.equals("") || number.isEmpty()) {
            Toast.makeText(getContext(), getString(R.string.please_enter_a_number), Toast.LENGTH_SHORT).show();
        } else {
            CallManager.call(getActivity(), mDigits.getNumbers());
        }
    }

    @OnClick(R.id.digits_edit_text)
    public void onDigitsClick(View view) {
        if (mDigits.isEmpty()) mDigits.setCursorVisible(true);
    }

    /**
     * Deletes the whole keypad's input when the delete button is long clicked
     */
    @OnLongClick(R.id.button_delete)
    public boolean delAllNum(View view) {
        setNumber("");
        return true;
    }

    /**
     * Starts a call to voice mail when the 1 button is long clicked
     */
    @OnLongClick(R.id.key_1)
    public boolean startVoiceMail(View view) {
        if (!mIsDialer) return false;
        return CallManager.callVoicemail(getContext());
    }

    @OnLongClick(R.id.key_0)
    public boolean addPlus(View view) {
        keyPressed(KeyEvent.KEYCODE_PLUS);
        return mIsDialer;
    }

    // -- Setters -- //

    /**
     * Act when a key is pressed (Plays tune and sets the text)
     *
     * @param keyCode KeyEvent key code
     */
    private void keyPressed(int keyCode) {
        if (getView().getTranslationY() != 0) return;

        Utilities.vibrate(getContext(), Utilities.SHORT_VIBRATE_LENGTH);
        mAudioUtils.playToneByKey(keyCode, getActivity());
        toggleCursor();

        KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
        mDigits.onKeyDown(keyCode, event);
        if (mOnKeyDownListener != null) mOnKeyDownListener.onKeyPressed(keyCode, event);
    }

    // -- Utils -- //

    /**
     * If the cursor is at the end of the text we hide it.
     */
    private void toggleCursor() {
        final int length = mDigits.length();
        if (length == mDigits.getSelectionStart() && length == mDigits.getSelectionEnd()) {
            mDigits.setCursorVisible(false);
        }
    }

    /**
     * Sets the input number to a given number
     * This function for now will be called due to list item click (contact)
     *
     * @param number number to put in mDigits
     */
    public void setNumber(String number) {
        mDigits.setText(number);
        mSharedDialViewModel.setNumber(number);
    }

    public void setDigitsCanBeEdited(boolean canBeEdited) {
        Handler handler = new Handler();
        handler.postDelayed(() -> mDialpadView.setDigitsCanBeEdited(canBeEdited), 2000);
    }

    public void setShowVoicemailButton(boolean show) {
        Handler handler = new Handler();
        handler.postDelayed(() -> mDialpadView.setShowVoicemailButton(show), 2000);
    }

    public void setOnKeyDownListener(OnKeyDownListener onKeyDownListener) {
        mOnKeyDownListener = onKeyDownListener;
    }

    public interface OnKeyDownListener {
        void onKeyPressed(int keyCode, KeyEvent event);
    }
}
