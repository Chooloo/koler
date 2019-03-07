package com.chooloo.www.callmanager.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chooloo.www.callmanager.OnSwipeTouchListener;
import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.util.Utilities;
import com.google.i18n.phonenumbers.AsYouTypeFormatter;
import com.google.i18n.phonenumbers.PhoneNumberUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import timber.log.Timber;

public class DialFragment extends Fragment {

    SharedDialViewModel mViewModel;
    private String mNumberText = "";
    private AsYouTypeFormatter mAsYouTypeFormatter;

    // Edit Texts
    @BindView(R.id.text_number_input) EditText mNumberInput;

    // Buttons
    @BindView(R.id.button_call) TextView mCallButton;
    @BindView(R.id.button_delete) ImageView mDelButton;
    @BindView(R.id.chip0) TextView mChip0;

    // Layouts
    @BindView(R.id.table_numbers) TableLayout mNumbersTable;
    @Nullable ViewGroup mRootView;

    // Swipe Listeners
    OnSwipeTouchListener mDialerSwipeListener;

    public static DialFragment newInstance() {
        return new DialFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_dial, container, false);
        ButterKnife.bind(this, mRootView);

        // Initiate swipe listener
        mDialerSwipeListener = new OnSwipeTouchListener(getContext()) {
            @Override
            public void onSwipeTop() {
                call(mCallButton);
            }

            @Override
            public void onSwipeLeft() {
                delNum(mNumberInput);
            }
        };
        mNumberInput.setOnTouchListener(mDialerSwipeListener);

        return mRootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(SharedDialViewModel.class);
        mViewModel.getNumber().observe(this, s -> {
            if (!s.equals(mNumberText)) {
                setNumber(s, true);
            }
        });

        mAsYouTypeFormatter = PhoneNumberUtil.getInstance().getAsYouTypeFormatter(Utilities.sLocale.getCountry());
    }

    // -- On Clicks -- //

    /**
     * Dialer buttons click handler
     *
     * @param view is the button number
     */
    @OnClick({R.id.chip0, R.id.chip1, R.id.chip2, R.id.chip3, R.id.chip4, R.id.chip5,
            R.id.chip6, R.id.chip7, R.id.chip8, R.id.chip9, R.id.chip_star, R.id.chip_hex})
    public void addChar(View view) {
        String id = getResources().getResourceEntryName(view.getId());
        char toAdd;
        if (id.contains("chip_star")) toAdd = '*';
        else if (id.contains("chip_hex")) toAdd = '#';
        else toAdd = id.charAt(4);

        mNumberText += toAdd;
        setNumber(mAsYouTypeFormatter.inputDigit(toAdd));

        vibrate();
    }

    /**
     * Deletes a number from the keypad's input when the delete button is clicked
     */
    @OnClick(R.id.button_delete)
    public void delNum(View view) {
        if (mNumberText.length() <= 0) return;
        mNumberText = mNumberText.substring(0, mNumberText.length() - 1);
        mAsYouTypeFormatter.clear();
        for (int i = 0; i < mNumberText.length(); i++) {
            String s = mAsYouTypeFormatter.inputDigit(mNumberText.charAt(i));
            if (i == mNumberText.length() - 1) setNumber(s);
        }

        if (mNumberText.length() == 0) {
            delAllNum(view);
        }

        vibrate();
    }

    /**
     * Deletes the whole keypad's input when the delete button is long clicked
     */
    @OnLongClick(R.id.button_delete)
    public boolean delAllNum(View view) {
        mNumberText = "";
        mAsYouTypeFormatter.clear();
        setNumber("");
        return true;
    }

    /**
     * When the user long clicks the number input field
     *
     * @param view
     * @return true
     */
    @OnLongClick(R.id.text_number_input)
    public boolean onInputLongClick(View view) {
        delAllNum(mNumberInput);
        return true;
    }

    /**
     * When the mNumberInput is selected
     *
     * @param view which in this case is the mNumberInput
     */
    @OnClick(R.id.text_number_input)
    public void editTextSelected(View view) {
        hideKeyboard(mNumberInput);
    }

    /**
     * Starts a call to voice mail when the 1 button is long clicked
     */
    @OnLongClick(R.id.chip1)
    public boolean startVoiceMail(View view) {
        try {
            Uri uri = Uri.parse("voicemail:1");
            Intent voiceMail = new Intent(Intent.ACTION_CALL, uri);
            startActivity(voiceMail);
            return true;
        } catch (SecurityException e) {
            Toast.makeText(getContext(), "Couldn't start Voicemail", Toast.LENGTH_LONG).show();
            Timber.e(e);
            return false;
        }
    }

    @OnLongClick(R.id.chip0)
    public boolean addPlus(View view) {
        mNumberText += "+";
        setNumber(mAsYouTypeFormatter.inputDigit('+'));
        return true;
    }

    /**
     * Calls the number in the keypad's input
     */
    @OnClick(R.id.button_call)
    public void call(View view) {
        Timber.i("Trying to call: " + mNumberText);
        if (mNumberText.isEmpty()) {
            Toast.makeText(getContext(), "Calling without a number huh? U little shit", Toast.LENGTH_LONG).show();
        } else {
            try {
                // Set the data for the call
                String uri = "tel:" + mNumberText;
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(uri));
                // Start the call
                startActivity(callIntent);
            } catch (SecurityException e) {
                Toast.makeText(getContext(), "Couldn't call " + mNumberText, Toast.LENGTH_LONG).show();
                Timber.e(e, "Couldn't call %s", mNumberText);
            }
        }
    }

    // -- Setters -- //

    public void setNumber(String number, boolean updateAll) {
        if (updateAll) {
            mNumberText = number;
            mNumberInput.setText(number);
            mAsYouTypeFormatter.clear();
            for (int i = 0; i < number.length(); i++) {
                mAsYouTypeFormatter.inputDigit(number.charAt(i));
            }

            mViewModel.setNumber(mNumberText);
        } else {
            setNumber(number);
        }
    }

    /**
     * Sets the input number to a given number
     * This function for now will be called due to list item click (contact)
     *
     * @param number
     */
    public void setNumber(String number) {
        mNumberInput.setText(number);
        mViewModel.setNumber(mNumberText);
    }

    private void vibrate() {
        Utilities.vibrate(getContext(), Utilities.SHORT_VIBRATE_LENGTH);
    }

    /**
     * Hides the keyboard based on the focused view (most likely EditText)
     *
     * @param view is the focused view
     */
    private void hideKeyboard(EditText view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
