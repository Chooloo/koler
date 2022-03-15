package com.chooloo.www.chooloolib.ui.widgets

import android.content.Context
import android.content.res.ColorStateList
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.util.getAttrColor

class DialpadEditText : AppCompatEditText {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet? = null) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet? = null, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleRes
    ) {
        maxLines = 1
        isSingleLine = true
        clipToOutline = true
        gravity = Gravity.CENTER
        showSoftInputOnFocus = false
        textAlignment = TEXT_ALIGNMENT_CENTER
        inputType = InputType.TYPE_CLASS_PHONE
        background = ContextCompat.getDrawable(context, R.drawable.round_outline)
        backgroundTintList = ColorStateList.valueOf(context.getAttrColor(R.attr.colorSurface))
        filters = arrayOf(InputFilter { source, _, _, _, _, _ ->
            source.filter { char -> char.isDigit() || char in arrayOf('*', '#', '+') }
        })

        setPadding(resources.getDimension(R.dimen.default_spacing_small).toInt())
        canScrollHorizontally(LAYOUT_DIRECTION_RTL or LAYOUT_DIRECTION_LTR)
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(text, type)
        text?.length?.let { setSelection(it) }
    }

    private fun getTextWatcher(onTextChangedListener: (String) -> Unit?) =
        object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                onTextChangedListener.invoke(p0.toString())
            }
        }
}