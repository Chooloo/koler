package com.chooloo.www.callmanager.ui.dialpad;

import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.databinding.FragmentDialpadBinding;
import com.chooloo.www.callmanager.entity.Contact;
import com.chooloo.www.callmanager.service.CallManager;
import com.chooloo.www.callmanager.ui.base.BaseFragment;
import com.chooloo.www.callmanager.ui.widgets.DialpadKey;
import com.chooloo.www.callmanager.util.AudioUtils;
import com.chooloo.www.callmanager.util.ContactUtils;
import com.chooloo.www.callmanager.util.Utilities;
import com.chooloo.www.callmanager.viewmodel.SharedDialViewModel;
import com.chooloo.www.callmanager.viewmodel.SharedIntentViewModel;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.chooloo.www.callmanager.util.AnimationUtils.showView;

public class DialpadFragment extends BaseFragment implements DialpadMvpView {

    public static final String TAG = "dialpad_bottom_dialog_fragment";
    public static final String ARG_DIALER = "dialer";

    private boolean mIsDialer = true;

    private DialpadMvpPresenter<DialpadMvpView> mPresenter;

    private OnKeyDownListener mOnKeyDownListener = null;

    private SharedDialViewModel mSharedDialViewModel;

    private AudioUtils mAudioUtils;

    private FragmentDialpadBinding binding;

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
        binding = FragmentDialpadBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.onAttach(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.onPause();
    }

    public boolean isDialer() {
        return mIsDialer;
    }

    @Override
    public void onSetup() {
        mPresenter = new DialpadPresenter<>();
        mPresenter.onAttach(this);

        mAudioUtils = new AudioUtils();

        setIsDialer(getArgsSafely().getBoolean(ARG_DIALER));

        mSharedDialViewModel = new ViewModelProvider(mActivity).get(SharedDialViewModel.class);

        SharedIntentViewModel sharedIntentViewModel = new ViewModelProvider(mActivity).get(SharedIntentViewModel.class);
        sharedIntentViewModel.getData().observe(getViewLifecycleOwner(), data -> mPresenter.onIntentDataChanged(data));

        binding.dialpadEditText.addTextChangedListener(new PhoneNumberFormattingTextWatcher(Utilities.sLocale.getCountry()));
        binding.dialpadEditText.addTextChangedListener(new TextWatcher() {
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

        View.OnClickListener keyClickListener = view -> mPresenter.onKeyClick(((DialpadKey) view).getKeyCode());
        binding.key0.setOnClickListener(keyClickListener);
        binding.key1.setOnClickListener(keyClickListener);
        binding.key2.setOnClickListener(keyClickListener);
        binding.key3.setOnClickListener(keyClickListener);
        binding.key4.setOnClickListener(keyClickListener);
        binding.key5.setOnClickListener(keyClickListener);
        binding.key6.setOnClickListener(keyClickListener);
        binding.key7.setOnClickListener(keyClickListener);
        binding.key8.setOnClickListener(keyClickListener);
        binding.key9.setOnClickListener(keyClickListener);
        binding.keyHex.setOnClickListener(keyClickListener);
        binding.keyStar.setOnClickListener(keyClickListener);
        binding.dialpadEditText.setOnClickListener(view -> mPresenter.onDigitsClick());
        binding.dialpadButtonDelete.setOnClickListener(view -> mPresenter.onDeleteClick());

        binding.key0.setOnLongClickListener(view -> mPresenter.onLongZeroClick());
        binding.key1.setOnLongClickListener(view -> mPresenter.onLongOneClick());
        binding.dialpadButtonDelete.setOnLongClickListener(view -> mPresenter.onLongDeleteClick());
        binding.dialpadButtonAddContact.setOnClickListener(view -> mPresenter.onAddContactClick());
    }

    @Override
    public void setNumber(String number) {
        binding.dialpadEditText.setText(number);
    }

    @Override
    public void setIsDialer(boolean isDialer) {
        mIsDialer = isDialer;
        binding.dialpadButtonDelete.setVisibility(isDialer ? VISIBLE : GONE);
        binding.dialpadButtonCall.setVisibility(isDialer ? VISIBLE : GONE);
        binding.dialpadEditText.setClickable(isDialer);
        binding.dialpadEditText.setLongClickable(isDialer);
        binding.dialpadEditText.setFocusableInTouchMode(isDialer);
        binding.dialpadEditText.setCursorVisible(isDialer);
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
            CallManager.call(mActivity, binding.dialpadEditText.getNumbers());
        }
    }

    @Override
    public void callVoicemail() {
        CallManager.callVoicemail(mActivity);
    }

    @Override
    public void toggleCursor(boolean isShow) {
        if (isShow && binding.dialpadEditText.isEmpty()) {
            binding.dialpadEditText.setCursorVisible(true);
        } else if (!isShow) {
            final int length = binding.dialpadEditText.length();
            if (length == binding.dialpadEditText.getSelectionStart() && length == binding.dialpadEditText.getSelectionEnd()) {
                binding.dialpadEditText.setCursorVisible(false);
            }
        }
    }

    @Override
    public void registerKeyEvent(int keyCode) {
        KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
        binding.dialpadEditText.onKeyDown(keyCode, event);
        if (mOnKeyDownListener != null) mOnKeyDownListener.onKeyPressed(keyCode, event);
    }

    @Override
    public void backspace() {
        String number = binding.dialpadEditText.getNumbers();
        if (number != null && number.length() > 0) {
            binding.dialpadEditText.setText(number.substring(0, number.length() - 1));
        }
    }

    @Override
    public void addContact() {
        Contact contact = ContactUtils.lookupContact(mActivity, binding.dialpadEditText.getNumbers());
        ContactUtils.addContact(mActivity, contact);
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
        if (binding.dialpadButtonDelete.getVisibility() == (isShow ? GONE : VISIBLE)) {
            showView(binding.dialpadButtonDelete, isShow);
        }
    }

    @Override
    public void showAddContactButton(boolean isShow) {
        if (binding.dialpadButtonAddContact.getVisibility() == (isShow ? GONE : VISIBLE)) {
            showView(binding.dialpadButtonAddContact, isShow);
        }
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
