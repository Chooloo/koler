package com.chooloo.www.koler.ui.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.text.InputType
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.core.widget.addTextChangedListener
import com.chooloo.www.koler.R
import com.chooloo.www.koler.util.getAttrColor
import com.chooloo.www.koler.util.sizeInDp
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

@SuppressLint("UseCompatTextViewDrawableApis")
class SearchBar : TextInputLayout {

    private var _onTextChangedListener: ((text: String) -> Unit?)? = null
    private var _textInputEditText: TextInputEditText

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet? = null) : this(context, attrs, 0)
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleRes: Int = 0
    ) : super(context, attrs, defStyleRes) {
        _textInputEditText = TextInputEditText(context, attrs, defStyleRes).apply {
            isFocusableInTouchMode = true
            gravity = Gravity.CENTER_VERTICAL
            inputType = InputType.TYPE_CLASS_TEXT
            hint = resources.getString(R.string.search_hint)
            compoundDrawablePadding = context.sizeInDp(5)
            compoundDrawableTintList =
                ColorStateList.valueOf(context.getAttrColor(R.attr.colorSecondaryVariant))
            layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)

            setTextAppearance(R.style.Koler_Text_Subtitle1)
            setTextColor(context.getAttrColor(R.attr.colorSecondaryVariant))
            setHintTextColor(context.getAttrColor(R.attr.colorSecondaryVariant))
            setOnFocusChangeListener { _, isFocused -> showIcon(!isFocused) }
            setPadding(context.sizeInDp(5))

            addTextChangedListener(
                afterTextChanged = {},
                beforeTextChanged = { _, _, _, _ -> },
                onTextChanged = { text, _, _, _ -> _onTextChangedListener?.invoke(text.toString()) }
            )
        }.also {
            addView(it)
        }

        isHintEnabled = false
        endIconMode = END_ICON_CLEAR_TEXT
        background = ContextCompat.getDrawable(context, R.drawable.bubble_background)
        endIconDrawable = ContextCompat.getDrawable(context, R.drawable.ic_close_black_24dp)

        showIcon(true)
        setEndIconTintList(ColorStateList.valueOf(context.getAttrColor(R.attr.colorSecondaryVariant)))
    }

    private fun showIcon(isShow: Boolean) {
        _textInputEditText.setCompoundDrawablesWithIntrinsicBounds(
            if (isShow) ContextCompat.getDrawable(
                context,
                R.drawable.ic_search_black_24dp
            ) else null, null, null, null
        )
    }

    fun setOnTextChangedListener(onTextChangedListener: ((text: String) -> Unit?)?) {
        _onTextChangedListener = onTextChangedListener
    }
}