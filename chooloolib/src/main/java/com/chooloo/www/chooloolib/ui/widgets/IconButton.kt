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
import com.google.android.material.internal.ViewUtils

@SuppressLint("CustomViewStyleable", "WrongConstant")
class IconButton : FloatingActionButton {
    @DrawableRes
    private var _iconDefault: Int? = null

    @DrawableRes
    private var _iconActivated: Int? = null

    private var _imageTintList: ColorStateList?
    private var _backgroundTintList: ColorStateList?
    private val _alterActivatedBackground: Boolean

    private val dimenPadding by lazy { ViewUtils.dpToPx(context, 15).toInt() }
    private val dimenSizeBig by lazy { ViewUtils.dpToPx(context, 70).toInt() }
    private val dimenSizeMini by lazy { ViewUtils.dpToPx(context, 10).toInt() }
    private val dimenSizeDefault by lazy { ViewUtils.dpToPx(context, 60).toInt() }
    private val dimenCornerSize by lazy { context.resources.getDimension(R.dimen.corner_radius) }
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
    ) : super(context, attrs.apply {

    }, defStyleRes) {
        context.obtainStyledAttributes(attrs, R.styleable.Chooloo_IconButton, defStyleRes, 0).also {
            size = it.getInteger(R.styleable.Chooloo_IconButton_size, 0)
            _iconDefault = it.getResourceId(R.styleable.Chooloo_IconButton_icon, NO_ID)
            _iconActivated = it.getResourceId(R.styleable.Chooloo_IconButton_activatedIcon, NO_ID)
            _alterActivatedBackground =
                it.getBoolean(R.styleable.Chooloo_IconButton_alterActivatedBackground, true)
        }.recycle()

        compatElevation = 0f
        _backgroundTintList = backgroundTintList
        imageTintList = imageTintList ?: ColorStateList.valueOf(colorOnSecondary)
        _imageTintList = imageTintList
        _imageTintList?.defaultColor?.let { rippleColor = it }
        shapeAppearanceModel =
            shapeAppearanceModel.toBuilder().setAllCornerSizes(dimenCornerSize).build()
        customSize = when (size) {
            SIZE_BIG -> dimenSizeBig
            SIZE_MINI -> dimenSizeMini
            else -> dimenSizeDefault
        }

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
        if (_alterActivatedBackground) {
            imageTintList = if (isActivated) _backgroundTintList else _imageTintList
            backgroundTintList = if (isActivated) _imageTintList else _backgroundTintList
        }
    }


    companion object {
        private const val SIZE_BIG = 2
    }
}