package com.chooloo.www.chooloolib.ui.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import androidx.annotation.IntDef
import androidx.annotation.RestrictTo
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.view.setPadding
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.util.getAttrColor


@SuppressLint("CustomViewStyleable", "WrongConstant")
class IconButton : AppCompatImageButton {
    @Size
    private var _size: Int

    @DrawableRes
    private var _iconDefault: Int? = null

    @DrawableRes
    private var _iconActivated: Int? = null

    private var _imageTintList: ColorStateList?
    private var _backgroundTintList: ColorStateList?
    private val _alterActivated: Boolean

    private val colorSecondary by lazy { context.getAttrColor(R.attr.colorSecondary) }
    private val colorOnSecondary by lazy { context.getAttrColor(R.attr.colorOnSecondary) }

    var iconDefault: Int?
        get() = _iconDefault
        set(value) {
            _iconDefault = value
        }


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleRes: Int = 0
    ) : super(context, attrs, defStyleRes) {
        context.obtainStyledAttributes(attrs, R.styleable.Chooloo_IconButton, defStyleRes, 0).also {
            _size = it.getInteger(R.styleable.Chooloo_IconButton_size, SIZE_AUTO)
            _iconDefault = it.getResourceId(R.styleable.Chooloo_IconButton_icon, NO_ID)
            _iconActivated = it.getResourceId(R.styleable.Chooloo_IconButton_activatedIcon, NO_ID)
            _alterActivated = it.getBoolean(R.styleable.Chooloo_IconButton_alterActivated, true)
        }.recycle()

        clipToOutline = true
        scaleType = ScaleType.FIT_CENTER
        background = getDrawable(context, R.drawable.bubble_background)
        imageTintList = imageTintList ?: ColorStateList.valueOf(colorOnSecondary)
        backgroundTintList = backgroundTintList ?: ColorStateList.valueOf(colorSecondary)

        _imageTintList = imageTintList
        _backgroundTintList = backgroundTintList

        if (_iconDefault != NO_ID) {
            _iconDefault?.let { setImageDrawable(getDrawable(context, it)) }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setSize(_size)
    }

    override fun setActivated(activated: Boolean) {
        super.setActivated(activated)
        if (_iconActivated != NO_ID) {
            (if (isActivated) _iconActivated else _iconDefault)?.let { setImageResource(it) }
        }
        if (_alterActivated) {
            imageTintList = if (isActivated) _backgroundTintList else _imageTintList
            backgroundTintList = if (isActivated) _imageTintList else _backgroundTintList
        }
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        imageAlpha = if (isEnabled) 255 else 40
    }

    fun setSize(@Size size: Int) {
        _size = size
        when (_size) {
            SIZE_BIG -> context.resources.getDimensionPixelSize(R.dimen.icon_button_size_big)
            SIZE_SMALL -> context.resources.getDimensionPixelSize(R.dimen.icon_button_size_small)
            SIZE_NORMAL -> context.resources.getDimensionPixelSize(R.dimen.icon_button_size_normal)
            else -> null
        }?.let {
            layoutParams = layoutParams.apply {
                height = it
                width = it
            }
        }
        setPadding(
            when (_size) {
                SIZE_BIG -> context.resources.getDimensionPixelSize(R.dimen.icon_button_padding_big)
                SIZE_SMALL -> context.resources.getDimensionPixelSize(R.dimen.icon_button_padding_small)
                else -> context.resources.getDimensionPixelSize(R.dimen.icon_button_padding_normal)
            }
        )
    }

    fun setDefaultIcon(iconRes: Int) {
        _iconDefault = iconRes
        isActivated = isActivated
    }


    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    @IntDef(
        SIZE_BIG,
        SIZE_AUTO,
        SIZE_SMALL,
        SIZE_NORMAL
    )
    annotation class Size

    companion object {
        const val SIZE_BIG = 2
        const val SIZE_AUTO = 0
        const val SIZE_SMALL = 3
        const val SIZE_NORMAL = 1
    }
}