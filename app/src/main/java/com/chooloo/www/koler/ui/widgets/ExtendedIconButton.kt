package com.chooloo.www.koler.ui.widgets

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class ExtendedIconButton : ExtendedFloatingActionButton {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleRes: Int = 0
    ) : super(context, attrs, defStyleRes) {
    }
}