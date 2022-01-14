package com.chooloo.www.chooloolib.ui.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat.getDrawable
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.util.getAttrColor
import com.google.android.material.floatingactionbutton.FloatingActionButton

@SuppressLint("CustomViewStyleable", "WrongConstant")
class IconButton : FloatingActionButton {
    @DrawableRes
    private var _iconDefault: Int? = null

    @DrawableRes
    private var _iconActivated: Int? = null

    private var _imageTintList: ColorStateList?
    private var _backgroundTintList: ColorStateList?

    private val colorOnSecondary by lazy { context.getAttrColor(R.attr.colorOnSecondary) }

    var iconDefault: Int?
        get() = _iconDefault
        set(value) {
            _iconDefault = value
            refreshResources()
        }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleRes: Int = 0
    ) : super(context, attrs, defStyleRes) {
        context.obtainStyledAttributes(attrs, R.styleable.Koler_IconButton, defStyleRes, 0).also {
            _iconDefault = it.getResourceId(R.styleable.Koler_IconButton_icon, NO_ID)
            _iconActivated = it.getResourceId(R.styleable.Koler_IconButton_activatedIcon, NO_ID)
        }.recycle()

        elevation = 0f
        compatElevation = 0f
        _backgroundTintList = backgroundTintList
        imageTintList = imageTintList ?: ColorStateList.valueOf(colorOnSecondary)
        _imageTintList = imageTintList
        _imageTintList?.defaultColor?.let { rippleColor = it }

        if (_iconDefault != NO_ID) {
            _iconDefault?.let { setImageDrawable(getDrawable(context, it)) }
        }
    }

    override fun setActivated(activated: Boolean) {
        super.setActivated(activated)
        refreshResources()
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        imageAlpha = if (isEnabled) 255 else 40
    }

    private fun refreshResources() {
        if (_iconActivated != NO_ID) {
            (if (isActivated) _iconActivated else _iconDefault)?.let { setImageResource(it) }
        }
        imageTintList = if (isActivated) _backgroundTintList else _imageTintList
        backgroundTintList = if (isActivated) _imageTintList else _backgroundTintList
    }
}