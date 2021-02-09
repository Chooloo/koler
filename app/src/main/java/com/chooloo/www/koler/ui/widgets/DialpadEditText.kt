package com.chooloo.www.koler.ui.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.text.InputType
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatEditText

class DialpadEditText constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleRes: Int = 0
) : AppCompatEditText(context, attrs, defStyleRes) {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet? = null) : this(context, attrs, 0)

    init {
        showSoftInputOnFocus = true
        isFocusableInTouchMode = true
        isCursorVisible = true
        isSingleLine = true
        maxLines = 1
        textAlignment = TEXT_ALIGNMENT_CENTER
        inputType = InputType.TYPE_CLASS_PHONE
        inputType = inputType or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
        setBackgroundColor(Color.TRANSPARENT)
        setTextIsSelectable(true)
        canScrollHorizontally(LAYOUT_DIRECTION_RTL or LAYOUT_DIRECTION_LTR)
    }

    val numbers: String
        get() = text?.filter { it.isDigit() }.toString()

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive(this)) {
            imm.hideSoftInputFromWindow(applicationWindowToken, 0)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val ret = super.onTouchEvent(event)
        // Must be done after super.onTouchEvent()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive(this)) {
            imm.hideSoftInputFromWindow(applicationWindowToken, 0)
        }
        return ret
    }
}