package com.chooloo.www.chooloolib.ui.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.util.getAttrColor


@SuppressLint("CustomViewStyleable", "WrongConstant")
class IconButton : androidx.appcompat.widget.AppCompatImageButton {
    @DrawableRes private var _iconDefault: Int? = null
    @DrawableRes private var _iconChecked: Int? = null
    private val _colorSurface by lazy { context.getAttrColor(R.attr.colorSurface) }
    private val _colorOnPrimary by lazy { context.getAttrColor(R.attr.colorOnPrimary) }
    private val _colorSurfaceDisabled by lazy { context.getAttrColor(R.attr.colorSurfaceDisabled) }
    private val _currentForegroundColor
        get() = if (isActivated) _colorOnPrimary else _colorSurface
    private val _currentBackgroundColor
        get() = if (isActivated) _colorSurface else _colorSurfaceDisabled

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleRes: Int = R.style.Chooloo_Button
    ) : super(context, attrs, defStyleRes) {
        context.obtainStyledAttributes(attrs, R.styleable.Chooloo_IconButton, 0, defStyleRes).also {
            val iconRes = it.getResourceId(R.styleable.Chooloo_IconButton_icon, NO_ID)
            val checkedIconRes = it.getResourceId(R.styleable.Chooloo_IconButton_checkedIcon, NO_ID)
            _iconDefault = if (iconRes == NO_ID) null else iconRes
            _iconChecked = if (checkedIconRes == NO_ID) null else checkedIconRes
        }.recycle()

        setIcon(_iconDefault)
        refreshColors()
    }

    override fun setActivated(activated: Boolean) {
        super.setActivated(activated)
        _iconChecked?.let {
            (if (isActivated) _iconChecked else _iconDefault)?.let(::setIcon)
        }
        refreshColors()
    }

    private fun refreshColors() {
        if (isActivated) {
            imageTintList = ColorStateList.valueOf(_currentForegroundColor)
            backgroundTintList = ColorStateList.valueOf(_currentBackgroundColor)
        }
    }

    fun setIcon(@DrawableRes iconRes: Int?) {
        iconRes?.let(::setImageResource)
    }
}