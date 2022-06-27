package com.chooloo.www.chooloolib.ui.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.core.content.ContextCompat.getDrawable
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.util.getAttrColor
import com.google.android.material.button.MaterialButton


@SuppressLint("CustomViewStyleable", "WrongConstant")
class IconButton : MaterialButton {
    private var _iconDefault: Drawable? = null
    private var _iconChecked: Drawable? = null


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleRes: Int = 0
    ) : super(context, attrs, defStyleRes) {
        context.obtainStyledAttributes(attrs, R.styleable.Chooloo_IconButton, defStyleRes, 0).also {
            val iconRes = it.getResourceId(R.styleable.Chooloo_IconButton_checkedIcon, NO_ID)
            _iconChecked = if (iconRes == NO_ID) null else getDrawable(context, iconRes)
        }.recycle()

        _iconDefault = icon

        if (isCheckable && !isChecked && isEnabled) {
            iconTint = ColorStateList.valueOf(context.getAttrColor(R.attr.colorPrimary))
        }
    }

    override fun setChecked(checked: Boolean) {
        super.setChecked(checked)
        _iconChecked?.let {
            (if (isChecked) _iconChecked else _iconDefault)?.let { icon = it }
        }
    }
}