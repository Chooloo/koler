package com.chooloo.www.chooloolib.ui.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.setPadding
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.util.getSelectableItemBackgroundBorderlessDrawable
import com.chooloo.www.chooloolib.util.getSizeInDp


@SuppressLint("Recycle", "CustomViewStyleable")
class DialpadKey : LinearLayout {
    private var _char: Char = '0'
    private var _digitTextView: TextView
    private var _lettersTextView: TextView

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleRes: Int = 0
    ) : super(context, attrs, defStyleRes) {
        gravity = Gravity.CENTER_HORIZONTAL

        _digitTextView = TextView(context, attrs, defStyleRes).apply {
            layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)

            setTextAppearance(R.style.Chooloo_Text_Headline2)
            typeface = ResourcesCompat.getFont(context, R.font.google_sans_bold)
        }.also {
            addView(it)
        }

        _lettersTextView = TextView(context, attrs, defStyleRes).apply {
            layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)

            setTextAppearance(R.style.Chooloo_Text_Caption)
            setPadding(0, context.getSizeInDp(2), 0, 0)
            typeface = ResourcesCompat.getFont(context, R.font.google_sans_medium)
        }.also {
            addView(it)
        }

        orientation = VERTICAL
        layoutParams = LayoutParams(WRAP_CONTENT, MATCH_PARENT)
        background = context.getSelectableItemBackgroundBorderlessDrawable()
        char = context.obtainStyledAttributes(attrs, R.styleable.Chooloo_DialpadKey)
            .getString(R.styleable.Chooloo_DialpadKey_digit)?.get(0) ?: '0'

        setPadding(context.getSizeInDp(7))
    }

    var char: Char
        get() = _digitTextView.text.toString().toCharArray()[0]
        set(value) {
            _digitTextView.text = value.toString()
            _char = value
            when (value) {
                '0' -> _lettersTextView.text = context.getString(R.string.dialpad_0_letters)
                '1' -> _lettersTextView.setBackgroundResource(R.drawable.round_voicemail_20)
                '2' -> _lettersTextView.text = context.getString(R.string.dialpad_2_letters)
                '3' -> _lettersTextView.text = context.getString(R.string.dialpad_3_letters)
                '4' -> _lettersTextView.text = context.getString(R.string.dialpad_4_letters)
                '5' -> _lettersTextView.text = context.getString(R.string.dialpad_5_letters)
                '6' -> _lettersTextView.text = context.getString(R.string.dialpad_6_letters)
                '7' -> _lettersTextView.text = context.getString(R.string.dialpad_7_letters)
                '8' -> _lettersTextView.text = context.getString(R.string.dialpad_8_letters)
                '9' -> _lettersTextView.text = context.getString(R.string.dialpad_9_letters)
                '*' -> _lettersTextView.text = context.getString(R.string.dialpad_star_letters)
                '#' -> _lettersTextView.text = context.getString(R.string.dialpad_pound_letters)
                else -> {}
            }
        }
}