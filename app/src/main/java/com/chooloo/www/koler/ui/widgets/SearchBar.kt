package com.chooloo.www.koler.ui.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.text.InputType
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.chooloo.www.koler.R
import com.chooloo.www.koler.util.getAttrColor
import com.chooloo.www.koler.util.sizeInDp
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

@SuppressLint("UseCompatTextViewDrawableApis")
class SearchBar : TextInputLayout {
    private var _textInputEditText: TextInputEditText
    private var _onTextChangedListener: ((text: String) -> Unit?)? = null

    private val colorSecondary by lazy { context.getAttrColor(R.attr.colorSecondary) }
    private val colorOnSecondary by lazy { context.getAttrColor(R.attr.colorOnSecondary) }
    private val spacing by lazy { resources.getDimensionPixelSize(R.dimen.default_spacing) }
    private val spacingBig by lazy { resources.getDimensionPixelSize(R.dimen.default_spacing_big) }
    private val spacingSmall by lazy { resources.getDimensionPixelSize(R.dimen.default_spacing_small) }

    var text: String?
        get() = _textInputEditText.text.toString()
        set(value) {
            _textInputEditText.setText(value ?: "")
        }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet? = null) : this(context, attrs, 0)
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleRes: Int = 0
    ) : super(context, attrs, defStyleRes) {
        _textInputEditText = TextInputEditText(context, attrs, defStyleRes).apply {
            compoundDrawablePadding = 8
            isFocusableInTouchMode = true
            gravity = Gravity.CENTER_VERTICAL
            inputType = InputType.TYPE_CLASS_TEXT
            hint = resources.getString(R.string.hint_search)
            layoutParams = LayoutParams(MATCH_PARENT, spacingBig)
            hintTextColor = ColorStateList.valueOf(colorOnSecondary)
            compoundDrawableTintList = ColorStateList.valueOf(colorOnSecondary)

            setTextColor(colorOnSecondary)
            setTextAppearance(R.style.Koler_Text_Subtitle1)
            setHintTextColor(ColorStateList.valueOf(colorOnSecondary))
            setPadding(
                spacingSmall,
                context.sizeInDp(2),
                spacingSmall,
                context.sizeInDp(2)
            )

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
        _textInputEditText.setOnFocusChangeListener { _, isFocused -> showIcon(!isFocused) }

        showIcon(true)
        setEndIconTintList(ColorStateList.valueOf(colorOnSecondary))
    }

    override fun setHint(hint: CharSequence?) {
        _textInputEditText?.hint = hint // do not remove the ?
    }

    override fun getHint() = _textInputEditText?.hint.toString() // do not remove the ?

    private fun showIcon(isShow: Boolean) {
        _textInputEditText.setCompoundDrawablesWithIntrinsicBounds(
            if (isShow) {
                ContextCompat.getDrawable(context, R.drawable.ic_search_black_24dp)
            } else {
                null
            }, null, null, null
        )
    }

    fun setOnTextChangedListener(onTextChangedListener: ((text: String) -> Unit?)?) {
        _onTextChangedListener = onTextChangedListener
    }
}