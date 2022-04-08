@file:Suppress("KotlinDeprecation")

package com.chooloo.www.chooloolib.ui.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.text.InputFilter
import android.text.InputType
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.util.getAttrColor
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


@SuppressLint("UseCompatTextViewDrawableApis")
class SearchBar : TextInputLayout {
    private var _textInputEditText: TextInputEditText
    private var _onTextChangedListener: ((text: String) -> Unit?)? = null

    private val colorBackground by lazy { context.getAttrColor(R.attr.colorLightBackground) }
    private val colorForeground by lazy { context.getAttrColor(R.attr.colorLightForeground) }
    private val spacing by lazy { resources.getDimensionPixelSize(R.dimen.default_spacing) }
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
            isFocusableInTouchMode = true
            gravity = Gravity.CENTER_VERTICAL
            inputType = InputType.TYPE_CLASS_TEXT
            hint = resources.getString(R.string.hint_search)
            layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            compoundDrawableTintList = ColorStateList.valueOf(colorForeground)
            filters = arrayOf(InputFilter { source, _, _, _, _, _ ->
                source.forEach { char ->
                    if (!(char.isLetterOrDigit() || char == ' ')) {
                        return@InputFilter ""
                    }
                }
                return@InputFilter null
            })

            setTextAppearance(R.style.Chooloo_Text_Subtitle1)
            setPadding(spacing, 0, spacingSmall, 0)
            setHintTextColor(ColorStateList.valueOf(colorForeground))
            setTextColor(context.getAttrColor(R.attr.colorOnSurface))

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
        clipToOutline = true
        background = ContextCompat.getDrawable(context, R.drawable.bubble_background)
        backgroundTintList = ColorStateList.valueOf(context.getAttrColor(R.attr.colorSurface))
        endIconDrawable = ContextCompat.getDrawable(context, R.drawable.round_close_24)
        startIconDrawable = ContextCompat.getDrawable(context, R.drawable.round_search_24)

        setPadding(spacingSmall, 0, 0, 0)
        setEndIconTintList(ColorStateList.valueOf(colorForeground))
        setStartIconTintList(ColorStateList.valueOf(colorForeground))
    }

    override fun setHint(hint: CharSequence?) {
        _textInputEditText?.hint = hint // do not remove the ?
    }

    override fun getHint() = _textInputEditText?.hint.toString() // do not remove the ?

    fun setOnTextChangedListener(onTextChangedListener: ((text: String) -> Unit?)?) {
        _onTextChangedListener = onTextChangedListener
    }
}