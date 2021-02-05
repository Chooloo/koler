package com.chooloo.www.callmanager.ui.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.text.InputType
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatEditText
import com.chooloo.www.callmanager.util.Utilities
import java.util.*

class DialpadEditText constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleRes: Int = 0
) : AppCompatEditText(context, attrs, defStyleRes) {

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
        get() = Utilities.getOnlyNumbers(Objects.requireNonNull(this.text).toString())

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