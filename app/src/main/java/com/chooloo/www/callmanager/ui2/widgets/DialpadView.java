/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chooloo.www.callmanager.ui2.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chooloo.www.callmanager.R;

import butterknife.BindView;

/**
 * View that displays a twelve-key phone dialpad.
 */
public class DialpadView extends LinearLayout {

    private static final String TAG = DialpadView.class.getSimpleName();

    @BindView(R.id.digits_edit_text) EditText mDigits;
    @BindView(R.id.button_delete) ImageButton mDeleteButton;
    @BindView(R.id.button_call) ImageButton mCallButton;
    @BindView(R.id.dialpad_key_voicemail) ImageView mKeyVoicemail;

    public DialpadView(Context context) {
        this(context, null);
    }

    public DialpadView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DialpadView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        setupKeypad();
        super.onFinishInflate();
    }

    private void setupKeypad() {

        final int[] buttonIds = new int[]{R.id.key_0, R.id.key_1, R.id.key_2, R.id.key_3, R.id.key_4,
                R.id.key_5, R.id.key_6, R.id.key_7, R.id.key_8, R.id.key_9, R.id.key_star, R.id.key_hex};

        final int[] numberIds = new int[]{
                R.string.dialpad_0_number,
                R.string.dialpad_1_number,
                R.string.dialpad_2_number,
                R.string.dialpad_3_number,
                R.string.dialpad_4_number,
                R.string.dialpad_5_number,
                R.string.dialpad_6_number,
                R.string.dialpad_7_number,
                R.string.dialpad_8_number,
                R.string.dialpad_9_number,
                R.string.dialpad_star_number,
                R.string.dialpad_pound_number};

        final int[] letterIds = new int[]{
                R.string.dialpad_0_letters,
                R.string.dialpad_1_letters,
                R.string.dialpad_2_letters,
                R.string.dialpad_3_letters,
                R.string.dialpad_4_letters,
                R.string.dialpad_5_letters,
                R.string.dialpad_6_letters,
                R.string.dialpad_7_letters,
                R.string.dialpad_8_letters,
                R.string.dialpad_9_letters,
                R.string.dialpad_star_letters,
                R.string.dialpad_pound_letters
        };

        final int[] keyCodes = new int[]{
                KeyEvent.KEYCODE_0,
                KeyEvent.KEYCODE_1,
                KeyEvent.KEYCODE_2,
                KeyEvent.KEYCODE_3,
                KeyEvent.KEYCODE_4,
                KeyEvent.KEYCODE_5,
                KeyEvent.KEYCODE_6,
                KeyEvent.KEYCODE_7,
                KeyEvent.KEYCODE_8,
                KeyEvent.KEYCODE_9,
                KeyEvent.KEYCODE_STAR,
                KeyEvent.KEYCODE_POUND
        };

        final Resources resources = getContext().getResources();

        for (int i = 0; i < buttonIds.length; i++) {
            DialpadKeyButton dialpadKey = findViewById(buttonIds[i]);
            dialpadKey.setNumber(resources.getString(numberIds[i]));
            dialpadKey.setLetters(resources.getString(letterIds[i]));
            dialpadKey.setKeyCode(keyCodes[i]);
        }

        setDigitsCanBeEdited(true);
    }

    public void setShowVoicemailButton(boolean show) {
        if (mKeyVoicemail != null) {
            mKeyVoicemail.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        }
    }

    /**
     * Whether or not the digits above the dialer can be edited.
     *
     * @param canBeEdited If true, the backspace button will be shown and the digits EditText
     *                    will be configured to allow text manipulation.
     */
    public void setDigitsCanBeEdited(boolean canBeEdited) {
        mDeleteButton.setVisibility(canBeEdited ? View.VISIBLE : View.GONE);
        mCallButton.setVisibility(canBeEdited ? View.VISIBLE : View.GONE);
        mDigits.setClickable(canBeEdited);
        mDigits.setLongClickable(canBeEdited);
        mDigits.setFocusableInTouchMode(canBeEdited);
        mDigits.setCursorVisible(canBeEdited);
    }

    /**
     * Always returns true for onHoverEvent callbacks, to fix problems with accessibility due to
     * the dialpad overlaying other fragments.
     */
    @Override
    public boolean onHoverEvent(MotionEvent event) {
        return true;
    }
}