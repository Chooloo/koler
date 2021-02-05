package com.chooloo.www.callmanager.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.chooloo.www.callmanager.R
import com.chooloo.www.callmanager.databinding.DialpadKeyLayoutBinding

class DialpadKey constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyleRes) {
    var keyCode = 0

    private var _binding: DialpadKeyLayoutBinding
    private var _letters: String? = null

    init {
        _binding = DialpadKeyLayoutBinding.inflate(LayoutInflater.from(context), this, true)
        val digit = context.obtainStyledAttributes(attrs, R.styleable.DialpadKey).getString(R.styleable.DialpadKey_digit)
        updateDigit(digit)
    }

    private var letters: String?
        get() = _letters
        set(letters) {
            _letters = letters
            _binding.letters.text = letters
        }

    private fun updateDigit(digit: String?) {
        when (digit) {
            "0" -> {
                keyCode = KeyEvent.KEYCODE_0
                letters = context.getString(R.string.dialpad_0_letters)
            }
            "1" -> {
                keyCode = KeyEvent.KEYCODE_1
                letters = context.getString(R.string.dialpad_0_letters)
            }
            "2" -> {
                keyCode = KeyEvent.KEYCODE_2
                letters = context.getString(R.string.dialpad_2_letters)
            }
            "3" -> {
                keyCode = KeyEvent.KEYCODE_3
                letters = context.getString(R.string.dialpad_3_letters)
            }
            "4" -> {
                keyCode = KeyEvent.KEYCODE_4
                letters = context.getString(R.string.dialpad_4_letters)
            }
            "5" -> {
                keyCode = KeyEvent.KEYCODE_5
                letters = context.getString(R.string.dialpad_5_letters)
            }
            "6" -> {
                keyCode = KeyEvent.KEYCODE_6
                letters = context.getString(R.string.dialpad_6_letters)
            }
            "7" -> {
                keyCode = KeyEvent.KEYCODE_7
                letters = context.getString(R.string.dialpad_7_letters)
            }
            "8" -> {
                keyCode = KeyEvent.KEYCODE_8
                letters = context.getString(R.string.dialpad_8_letters)
            }
            "9" -> {
                keyCode = KeyEvent.KEYCODE_9
                letters = context.getString(R.string.dialpad_9_letters)
            }
            "*" -> {
                keyCode = KeyEvent.KEYCODE_STAR
                letters = context.getString(R.string.dialpad_star_letters)
            }
            "#" -> {
                keyCode = KeyEvent.KEYCODE_POUND
                letters = context.getString(R.string.dialpad_pound_letters)
            }
            else -> {
            }
        }
    }
}