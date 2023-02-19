package com.chooloo.www.chooloolib.ui.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.widget.ImageButton
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.util.getAttrColor


@SuppressLint("CustomViewStyleable", "WrongConstant")
class IconButton : ImageButton {
    @DrawableRes private var _iconDefault: Int? = null
    @DrawableRes private var _iconChecked: Int? = null

    @ColorInt private val _defaultForegroundTint: Int
    @ColorInt private val _defaultBackgroundTint: Int

    private val _colorBackground by lazy { context.getAttrColor(R.attr.colorSecondaryContainer) }
    private val _colorForeground by lazy { context.getAttrColor(R.attr.colorOnSecondaryContainer) }
    private val _colorForegroundDisabled by lazy { context.getAttrColor(R.attr.colorBackgroundVariant) }


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleRes: Int = R.style.Chooloo_Button_Secondary
    ) : super(context, attrs, defStyleRes) {
        context.obtainStyledAttributes(attrs, R.styleable.Chooloo_IconButton, 0, defStyleRes).also {
            val iconRes = it.getResourceId(R.styleable.Chooloo_IconButton_icon, NO_ID)
            val checkedIconRes = it.getResourceId(R.styleable.Chooloo_IconButton_checkedIcon, NO_ID)

            _iconDefault = if (iconRes == NO_ID) null else iconRes
            _iconChecked = if (checkedIconRes == NO_ID) null else checkedIconRes
        }.recycle()

        _defaultForegroundTint = imageTintList?.defaultColor ?: _colorForeground
        _defaultBackgroundTint = backgroundTintList?.defaultColor ?: _colorBackground

        setIcon(_iconDefault)
        refreshColors()
    }

    override fun setActivated(activated: Boolean) {
        super.setActivated(activated)
        (if (isActivated) _iconChecked else _iconDefault)?.let(::setIcon)
        refreshColors()
    }

    private fun refreshColors() {
        imageTintList =
            ColorStateList.valueOf(
                if (isEnabled) {
                    if (isActivated) _defaultBackgroundTint else _defaultForegroundTint
                } else {
                    _colorForegroundDisabled
                }
            )
        backgroundTintList =
            ColorStateList.valueOf(if (isActivated) _defaultForegroundTint else _defaultBackgroundTint)
    }

    fun setIcon(@DrawableRes iconRes: Int?) {
        iconRes?.let(::setImageResource)
    }

    fun setDefaultIcon(@DrawableRes iconRes: Int?) {
        _iconDefault = iconRes
    }

    fun setCheckedIcon(@DrawableRes iconRes: Int?) {
        _iconChecked = iconRes
    }
}